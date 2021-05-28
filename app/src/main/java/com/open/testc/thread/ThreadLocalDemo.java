package com.open.testc.thread;

import android.util.Log;

import androidx.annotation.Nullable;

/**
 * Created by liuml on 2021/4/22 11:20
 */
public class ThreadLocalDemo {

    public static String TAG = "ThreadLocalDemo";

    public static ThreadLocal<Integer> threadLocal = new ThreadLocal<Integer>() {
        @Nullable
        @Override
        protected Integer initialValue() {
            //给默认初始化一个
            return 0;
        }
    };

    public static class MyThread1 extends Thread {

        @Override
        public void run() {
            super.run();

            threadLocal.set(1);

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "线程1 run: " + Thread.currentThread().getName() + "-----" + threadLocal.get());

        }
    }



    public static class MyThread2 extends Thread {
        @Override
        public void run() {
            super.run();
             threadLocal.set(2);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d(TAG, "线程2 run: " + Thread.currentThread().getName() + "-----" + threadLocal.get());
        }
    }


    public static void printMainThread() {
//        try {
//            Thread.sleep(2000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//        threadLocal.set(0);
        Log.d(TAG, "打印主线程 run: " + Thread.currentThread().getName() + "-----" + threadLocal.get());

    }
}