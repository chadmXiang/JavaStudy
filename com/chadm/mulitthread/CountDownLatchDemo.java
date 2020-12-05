/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

import java.text.SimpleDateFormat;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * A CountDownLatch is initialized with a given count.
 * The method CountDownLatch#countDown() decreases the count by one.
 * The method CountDownLatch#await() blocks until the underlying count becomes zero.
 *
 * @author chadm
 * @date 2020-12-05 20:58
 */
public class CountDownLatchDemo {

    private static final int LATCH_COUNT = 4;
    private static final int WORKER_COUNT = 3;

    public static void main(String[] args) throws InterruptedException {

        ExecutorService executorService = Executors.newFixedThreadPool(WORKER_COUNT);
        CountDownLatch countDownLatch = new CountDownLatch(LATCH_COUNT);

        /*
         * output:
         *  waiting for count to be zero ,now is 2 2020-12-05 21:55:13
         *  waiting for count to be zero ,now is 1 2020-12-05 21:55:13
         *  waiting for count to be zero ,now is 2 2020-12-05 21:55:13
         *  now count is zero , await return immediately start work at 2020-12-05 21:55:15
         *  pool-1-thread-3 do some work 2020-12-05 21:55:15
         *  pool-1-thread-1 do some work 2020-12-05 21:55:15
         *  pool-1-thread-2 do some work 2020-12-05 21:55:15
         */
        for (int i = 1; i < LATCH_COUNT; i++) {
            executorService.execute(() -> {
                countDownLatch.countDown();
                try {
                    // 当count > 0时，阻塞在await方法
                    System.out.println("waiting for count to be zero ,now is " + countDownLatch.getCount()+ " " + printTime());
                    countDownLatch.await();
                    System.out.println(Thread.currentThread().getName() + " do some work " + printTime());
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }

        // 主线程执行sleep 2s,然后调用countDown，触发所有await的任务一起执行
        Thread.sleep(2000);
        // 当countDown减少为0时，触发所有任务一起执行
        countDownLatch.countDown();
        if(countDownLatch.getCount() == 0 ) {
            countDownLatch.await();
            System.out.println("now count is zero , await return immediately start work at " + printTime());
        }
        Thread.sleep(100);
        executorService.shutdown();
    }

    static String printTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return  simpleDateFormat.format(System.currentTimeMillis());
    }
}
