package com.demo.thread;

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
}
