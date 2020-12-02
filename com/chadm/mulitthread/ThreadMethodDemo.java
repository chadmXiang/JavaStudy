/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author chadm
 *
 * 此类主要测试Thread的join/sleep/interrupt方法
 *
 * @date 2020-11-19 22:41
 */
public class ThreadMethodDemo {

    public static void main(String [] args) throws InterruptedException {
        joinTest();
        interruptTest();
    }

    /**
     * join方法表示线程A等待在线程A里面启动的子线程B完成后再执行
     *
     * joinTest1：sub thread在main thread里面join，main thread等待sub thread执行完后才执行
     * joinTest2：main thread在sub thread里面join,sub thread 等待 main thread执行完后才执行
     *
     * @throws InterruptedException when sleep interrupt ,throw InterruptedException
     */
    private static void joinTest() throws InterruptedException {
        joinTest1();
        joinTest2();
    }

    /**
     * output:
     *  sub thread finish
     *  main wait for sub thread finish
     *
     * @throws InterruptedException when sleep interrupt ,throw InterruptedException
     */
    private static void joinTest1() throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println(Thread.currentThread().getName() + " finish");
            try {
                // 当前线程休眠100ms
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        // 设置线程名称，方便后面调试
        thread.setName("sub thread");
        thread.start();
        thread.join();

        System.out.println(Thread.currentThread().getName() + " wait for sub thread finish");
    }

    /**
     * output:
     *  main finish
     *  sub thread wait for main thread finish
     *
     * @throws InterruptedException when sleep interrupt ,throw InterruptedException
     */
    private static void joinTest2() throws InterruptedException {
        final Thread mainThread = Thread.currentThread();
        Thread thread = new Thread(() -> {
            try {
                mainThread.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(Thread.currentThread().getName() + " wait for main thread finish");
        });
        // 设置线程名称，方便后面调试
        thread.setName("sub thread");
        thread.start();
        try {
            // 当前线程休眠100ms
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " finish");
    }

    /**
     * interrupt 方法在如下3种阻塞场景下，会重置interrupt status为false
     * 1.sleep/wait/join等场景，会抛出InterruptedException
     * 2.InterruptibleChannel场景，会抛出ClosedByInterruptException
     * 3.Selector，会直接退出，触发Selector.wakeup
     * 非以上场景，则设置interrupt status为true
     *
     * @throws InterruptedException when sleep interrupt ,throw InterruptedException
     */
    private static void interruptTest() throws InterruptedException {
        interruptTest2();
        interruptTest1();
    }

    /**
     * 一直打印不停止，因为在该方法场景中，interrupt只设置终止状态，不会停止线程
     *
     * output:
     *  interrupt thread1 is running ,isInterrupted = true
     *  interrupt thread1 is running ,isInterrupted = true
     *  interrupt thread1 is running ,isInterrupted = true
     *  interrupt thread1 is running ,isInterrupted = true
     *  interrupt thread1 is running ,isInterrupted = true
     *
     *   @throws InterruptedException when sleep interrupt ,throw InterruptedException
     */
    private static void interruptTest1() throws InterruptedException {
        AtomicBoolean stop = new AtomicBoolean(false);

        Thread thread = new Thread(() -> {
            while (!stop.get()) {
                System.out.println(Thread.currentThread().getName() + " is running ,isInterrupted = " + Thread.currentThread().isInterrupted());
            }
        });
        // 设置线程名称，方便后面调试
        thread.setName("interrupt thread1");
        thread.start();
        Thread.sleep(100);
        thread.interrupt();
    }

    /**
     * 对于线程被block的场景，如被sleep/wait/join等场景，调用interrupt会触发InterruptedException
     * 同时会重置interrupt status，所以在finish的里面，interrupt status为false
     *
     * output:
     *  interrupt thread2 is running true
     *  interrupt thread2 finish false
     */
    private static void interruptTest2() {
        AtomicBoolean stop = new AtomicBoolean(false);
        Thread thread = new Thread(() -> {
            while (!stop.get()) {
                System.out.println(Thread.currentThread().getName() + " is running " + Thread.currentThread().isInterrupted());
                try {
                    // 当前线程休眠100ms
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    stop.set(true);
                }
            }
            System.out.println(Thread.currentThread().getName() + " finish " + Thread.currentThread().isInterrupted());
        });
        // 设置线程名称，方便后面调试
        thread.setName("interrupt thread2");
        thread.start();
        thread.interrupt();
    }

}
