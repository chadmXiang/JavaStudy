/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

/**
 * A Semaphore maintains a count (int), known as permits.
 * This count cannot increase more than the specified maximum count.
 * At the time of construction this count is 0.
 *
 * The method Semaphore#acquire() increases the underlying count.
 * If the count is more than the max value, this method blocks
 * until the current value of count becomes less than or equal to max value.
 *
 * The method Semaphore#release() decreases the permit count,
 * which may potentially release a blocking acquire() call.
 *
 * @author chadm
 * @date 2020-12-05 20:03
 */
public class SemaphoreDemo {

    private static final int WORKER_COUNT = 6;
    // PERMITS 为2，每次只有2个线程能执行
    private static final int PERMITS = 2;

    public static void main(String[] args) throws InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(WORKER_COUNT);
        Semaphore semaphore = new Semaphore(PERMITS);


        /*
         * output:
         *  do some work 2020-12-05 20:54:17
         *  do some work 2020-12-05 20:54:17
         *  do some work 2020-12-05 20:54:19
         *  do some work 2020-12-05 20:54:19
         *  do some work 2020-12-05 20:54:21
         *  do some work 2020-12-05 20:54:21
         */
        for (int i = 0; i < WORKER_COUNT; i++) {
            executorService.execute(() -> {
                try {
                    // 获取permit
                    semaphore.acquire();

                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = new Date();
                    // 每间隔2s有2个线程打印
                    System.out.println("do some work " + simpleDateFormat.format(date));
                    Thread.sleep(2000);

                    // 释放获取permit
                    semaphore.release();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            });
        }
        executorService.shutdown();

    }
}
