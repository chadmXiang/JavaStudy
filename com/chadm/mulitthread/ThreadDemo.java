/*
 * Copyright (c)  chadm All Rights Reserved.
 * @author: chadm.xiang@hotmail.com
 * @version: 1.0
 */

package com.chadm.mulitthread.demo;

/**
 * @author chadm
 *
 * 此类主要介绍创建Thread的2种方式
 * 一种是通过继承Thread类，覆写run实现来实现要执行的任务
 * 第二种是通过创建一个Runnable对象来设置要执行的任务
 *
 * @date 2020-11-19 22:31
 */
public class ThreadDemo {

    public static void main(String [] args) {

        MyThread myThread = new MyThread();
        myThread.start();

        Thread runnable = new Thread(() -> {
            System.out.println("I am create from Runnable");
        });
        runnable.start();
    }

    static class MyThread extends Thread {
        @Override
        public void run() {
            System.out.println("I am created form extends Thread");
        }
    }


}
