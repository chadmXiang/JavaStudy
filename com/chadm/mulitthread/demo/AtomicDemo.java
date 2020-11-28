/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chadm
 * @date 2020-11-22 11:17
 */
public class AtomicDemo {

    static volatile int count;

    static AtomicInteger atomicCount = new AtomicInteger();

    public static void main(String [] args) throws InterruptedException {
        int value = 0;
        value++;
        System.out.println(value);

        /*
         * 每次输出的结果都不一样
         */
        multiAdd();

        /*
         * 每次输出的结果都一样，都是200000
         */
        atomicAdd();

        /*
         * 每次输出的结果都一样，都是200000
         */
        synchronizedAdd();
    }

    private static void multiAdd() throws InterruptedException {
        for (int t = 0; t < 5; t++) {
            count = 0;
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    count++;
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    count++;
                }
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            System.out.println("multiAdd count value = " + count);
        }
    }

    private static void synchronizedAdd() throws InterruptedException {
        Object lock = new Object();
        for (int t = 0; t < 5; t++) {
            count = 0;
            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    synchronized (lock) {
                        count++;
                    }
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    synchronized (lock) {
                        count++;
                    }
                }
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            System.out.println("synchronizedAdd count value = " + count);
        }
    }

    private static void atomicAdd() throws InterruptedException {
        for (int t = 0; t < 5; t++) {
            atomicCount.set(0);

            Thread thread1 = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    atomicCount.incrementAndGet();
                }
            });

            Thread thread2 = new Thread(() -> {
                for (int i = 0; i < 100000; i++) {
                    atomicCount.incrementAndGet();
                }
            });

            thread1.start();
            thread2.start();

            thread1.join();
            thread2.join();

            System.out.println("atomicAdd atomicCount value = " + atomicCount.get());
        }
    }



}
