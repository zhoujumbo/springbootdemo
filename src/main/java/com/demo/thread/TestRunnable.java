package com.demo.thread;

import com.demo.thread.demo1.DeadlockRisk;
import com.demo.thread.demo1.DoSomething;
import org.junit.jupiter.api.Test;

/**
 * 简单线程使用
 */
public class TestRunnable {

    @Test
    public void test1() throws InterruptedException {

        DoSomething ds1 = new DoSomething("zhou");
        DoSomething ds2 = new DoSomething("jum");

        Thread t1 = new Thread(ds1);
        Thread t2 = new Thread(ds2);

        t1.start();
        t2.start();
        Thread.sleep(1000);
    }

    @Test
    public void test2() throws InterruptedException{

        DeadlockRisk deadlockRisk = new DeadlockRisk();

        class ThradA extends Thread{
            @Override
            public void run() {
                deadlockRisk.write(1,2);
            }
        }

        class ThradB extends Thread{
            @Override
            public void run() {
                System.out.println(deadlockRisk.read());
            }
        }

        Thread t1 = new ThradA();
        Thread t2 = new ThradB();
        t1.start();
        t2.start();

        Thread.sleep(1000);
    }


}
