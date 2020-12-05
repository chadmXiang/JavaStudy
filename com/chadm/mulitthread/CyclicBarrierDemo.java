/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

import java.text.SimpleDateFormat;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *  A CyclicBarrier allows a set of threads (parties) to all
 *  wait for each other to reach a common barrier point.
 *
 * A CyclicBarrier is initialized with a given count (the parties).
 * Let's say this count is N.
 *
 * The method CyclicBarrier#await() decreases the count N by 1.
 * The calling threads waits until N becomes zero
 * i.e. all threads (parties) have invoked await() on this barrier.
 *
 * This functionality is similar to CountDownLatch, but main difference with CyclicBarrier is;
 * 1.it does not have method countDown()
 * 2.CyclicBarrier can reset the barrier to its initial state
 *
 * @author chadm
 * @date 2020-12-05 22:00
 */
public class CyclicBarrierDemo {

    private static final int BARRIER_COUNT = 4;
    private static final int WORKER_COUNT = 3;

    public static void main(String[] args) throws InterruptedException, BrokenBarrierException {
        ExecutorService executorService = Executors.newFixedThreadPool(WORKER_COUNT);
        CyclicBarrier cyclicBarrier = new CyclicBarrier(BARRIER_COUNT);

        for (int i = 1; i < BARRIER_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    System.out.println(Thread.currentThread().getName() + " add work at " + printTime());
                    // 如果不是最后一个调用await的线程，则会阻塞在此方法
                    cyclicBarrier.await();
                    System.out.println(Thread.currentThread().getName() + " do some work " + printTime());
                } catch (BrokenBarrierException | InterruptedException e) {
                    e.printStackTrace();
                }
            });

        }
        Thread.sleep(2000);
        System.out.println("the number of parties currently waiting at the barrier is " +  cyclicBarrier.getNumberWaiting() +" ; " + printTime());
        if(cyclicBarrier.getNumberWaiting() == BARRIER_COUNT -1) {
            System.out.println("now start work ,and I always be called before others thread to continue ");
            // 1.最后一个party(main thread)调用await方法，main调用await方法是在其它线程继续执行之前的
            // 2.最后一个party(main thread)调用await后，+  cyclicBarrier.getNumberWaiting() 被reset为0
            // 3. 如果显示调用cyclicBarrier.reset()，则之前阻塞在await方法的线程会抛出BrokenBarrierException
            cyclicBarrier.await();
            System.out.println("the number of parties currently waiting at the barrier will reset after last thread call await");
        }

        Thread.sleep(100);
        executorService.shutdown();
    }

    static String printTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  simpleDateFormat.format(System.currentTimeMillis());
    }
}
