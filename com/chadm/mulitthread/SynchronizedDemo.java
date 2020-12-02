/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

/**
 * synchronized主要有3种用法：
 *  同步方法：add和add2的代码效果是一样的，都是锁的类的实例对象
 *  同步class类：minus和minus2的代码效果是一样的，都是锁的类的class对象，如SynchronizedDemo.class
 *  同步代码块：一般配合wait/notify来使用，完成通知唤醒的功能
 *
 * @author chadm
 * @date 2020-11-20 21:12
 */
public class SynchronizedDemo {

    private final Object lock = new Object();

    private static volatile int value = 0;

    public static void main(String [] args) {

        SynchronizedDemo demo = new SynchronizedDemo();

        /*
         * 方法都加了同步，thread2在thread1之前启动，所以add2在add2之前执行，minus2在minus之前执行，
         *
         * output:
         *  thread2 call add2 value = 100
         *  thread1 call add value = 110
         *  thread2 call minus2 value = 107
         *  thread1 call minus value = 104
         *
         */
        Thread thread1 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " call add value = " + demo.add());
            System.out.println(Thread.currentThread().getName() + " call minus value = " + demo.minus());
        });
        thread1.setName("thread1");


        Thread thread2 = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " call add2 value = " + demo.add2());
            System.out.println(Thread.currentThread().getName() + " call minus2 value = " + SynchronizedDemo.minus2());
        });
        thread2.setName("thread2");

        // 先启动thread2,然后启动thread1
        thread2.start();
        thread1.start();

        /*
         * output:
         *  thread3 I am sleep
         *  thread4 I am call notify
         *  thread3 I am awake
         */
        Thread thread3 = new Thread(demo::trySleep);
        thread3.setName("thread3");
        thread3.start();

        Thread thread4 = new Thread(demo::tryNotify);
        thread4.setName("thread4");
        thread4.start();

    }

    /**
     * synchronized 方法，效果等同于synchronized (this)
     *
     * @return value + 10
     */
    public synchronized int add() {
        value += 10;
        return value;
    }

    /**
     * synchronized (this) ，效果等同于方法
     *
     * @return value + 100
     */
    public int add2() {
        synchronized (this) {
            value += 100;
            return value;
        }
    }

    /**
     * 同步SynchronizedDemo.class 等同于static synchronized
     *
     * @return value -2
     */
    public int minus() {
        synchronized (SynchronizedDemo.class) {
            value -= 2;
            return --value;
        }
    }

    /**
     * static synchronized 等同于synchronized (SynchronizedDemo.class)
     *
     * @return  value -3
     */
    public static synchronized int minus2() {
        value -= 3;
        return value;
    }

    /**
     * 同步代码块，阻塞在lock上
     */
    public void trySleep() {
        synchronized (lock) {
            try {
                System.out.println(Thread.currentThread().getName() + " I am sleep");
                lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " I am awake");
        }
    }

    /**
     * 唤醒阻塞在lock上的线程
     */
    public void tryNotify() {
        synchronized (lock) {
            System.out.println(Thread.currentThread().getName() +  " I am call notify");
            lock.notify();
        }
    }


}
