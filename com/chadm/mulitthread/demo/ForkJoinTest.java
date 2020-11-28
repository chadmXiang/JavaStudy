/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

/**
 * @author chadm
 * @date 2020-11-20 22:10
 */
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveTask;
import java.util.concurrent.atomic.AtomicInteger;


public class ForkJoinTest {

    private static AtomicInteger taskIndex = new AtomicInteger();

    public static void main(String[] args) {
        List<Integer> list = new ArrayList<>();
        for (int i = 1; i < 10; i++) {
            list.add(i);
        }

        /*
         * 从打印可以看出:
         *  task0 分成了task1和task2
         *          task1 又分成了task3 和 task 4
         *              task3和task4完成后，task1才结束并返回结果
         *          task2 又分成了task5 和 task 6
         *              task5和task6完成后，task2才结束并返回结果
         *        task1和task2完成后，task0返回最终的结果
         *
         * Possible output:
         *  main run task 0 :[1,2,3,4,5,6,7,8,9,]
         *  main task = 0 split to task 1 and task 2
         *  main task = 0 wait for task 1 finish ,status false
         *  ForkJoinPool.commonPool-worker-9 run task 1 :[1,2,3,4,]
         *  ForkJoinPool.commonPool-worker-2 run task 2 :[5,6,7,8,9,]
         *  ForkJoinPool.commonPool-worker-9 task = 1 split to task 3 and task 4
         *  ForkJoinPool.commonPool-worker-2 task = 2 split to task 5 and task 6
         *  ForkJoinPool.commonPool-worker-9 task = 1 wait for task 3 finish ,status false
         *  ForkJoinPool.commonPool-worker-11 run task 3 :[1,2,]
         *  ForkJoinPool.commonPool-worker-4 run task 5 :[5,6,]
         *  ForkJoinPool.commonPool-worker-13 run task 6 :[7,8,9,]
         *  ForkJoinPool.commonPool-worker-2 task = 2 wait for task 5 finish ,status false
         *  ForkJoinPool.commonPool-worker-6 run task 4 :[3,4,]
         *  ForkJoinPool.commonPool-worker-6 finish task 4 ; result = 7,status false
         *  ForkJoinPool.commonPool-worker-11 finish task 3 ; result = 3,status false
         *  ForkJoinPool.commonPool-worker-4 finish task 5 ; result = 11,status false
         *  ForkJoinPool.commonPool-worker-9 task = 1 wait for task 4 finish  ,status false
         *  ForkJoinPool.commonPool-worker-13 finish task 6 ; result = 24,status false
         *  ForkJoinPool.commonPool-worker-9 finish task 1 ; result = 10,status false
         *  ForkJoinPool.commonPool-worker-2 task = 2 wait for task 6 finish  ,status false
         *  main task = 0 wait for task 2 finish  ,status false
         *  ForkJoinPool.commonPool-worker-2 finish task 2 ; result = 35,status false
         *  main finish task 0 ; result = 45,status false
         *  Sum is = 45
         */
        Integer sum = ForkJoinPool.commonPool().
                invoke(new FaTask(list));
        System.out.println("Sum is = " + sum);


    }

    @SuppressWarnings("serial")
    private static class FaTask extends RecursiveTask<Integer> {

        // task 编号，方便打印的时候分析
        public int index;

        private static final int THRESHOLD = 3;
        private List<Integer> mList;

        private FaTask(List<Integer> list) {
            mList = list;
            index = taskIndex.getAndIncrement();
        }

        @Override
        protected Integer compute() {
            Integer result;
            System.out.println(Thread.currentThread().getName() + " run " + this.toString());

            if (mList.size() <= THRESHOLD) {
                result = calc();
            } else {
                int mid = mList.size() / 2;

                FaTask leftTask = new FaTask(mList.subList(0, mid));
                FaTask rightTask = new FaTask(mList.subList(mid, mList.size()));

                System.out.println(Thread.currentThread().getName() + " task = " + index + " split to task " + leftTask.index + " and task " + rightTask.index);

                leftTask.fork();
                rightTask.fork();

                System.out.println(Thread.currentThread().getName() + " task = " + index + " wait for task " + leftTask.index + " finish ,status " + this.isDone());
                Integer sumleft = leftTask.join();
                System.out.println(Thread.currentThread().getName() + " task = " + index + " wait for task " + rightTask.index + " finish  ,status " + this.isDone());
                Integer sumright = rightTask.join();

                result = sumleft+sumright;
            }

            System.out.println(Thread.currentThread().getName() + " finish task " + index + " ; result = " + result + ",status " + this.isDone());

            return result;

        }

        private Integer calc() {
            int sum = 0;
            for (Integer integer : mList) {
                sum += integer;
            }
            try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return sum;

        }

        @Override
        public String toString() {
            StringBuilder builder = new StringBuilder();
            builder.append("task ").append(index).append(" :[");
            for (Integer Integer : mList) {
                builder.append(Integer).append(",");
            }
            builder.append("]");
            return builder.toString();
        }
    }

}
