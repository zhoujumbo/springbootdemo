package com.demo.thread.demo1;


public class ProducerConsumer {

    //    @Test
    public void test() {

        Godown godown = new Godown(30);
        Consumer c1 = new Consumer(50, godown);
        Consumer c2 = new Consumer(20, godown);
        Consumer c3 = new Consumer(30, godown);
        Producer p1 = new Producer(10, godown);
        Producer p2 = new Producer(10, godown);
        Producer p3 = new Producer(10, godown);
        Producer p4 = new Producer(10, godown);
        Producer p5 = new Producer(10, godown);
        Producer p6 = new Producer(10, godown);
        Producer p7 = new Producer(10, godown);

        c1.start();
        c2.start();
        c3.start();
        p1.start();
        p2.start();
        p3.start();
        p4.start();
        p5.start();
        p6.start();
        p7.start();
    }


}

/**
 * 仓库
 */
class Godown {

    public static final int MAX_SIZE = 100;
    public int curnum;  // 当前库存量

    Godown() {
    }

    Godown(int curnum) {
        this.curnum = curnum;
    }

    // 生产指定数量的商品
    public synchronized void produce(int neednum) {

        // 测试是否需要生产
        while (neednum + curnum > MAX_SIZE) {
            System.out.println("要生产的产品数量" + neednum + "超过剩余库存量" + (MAX_SIZE - curnum) + "暂时不能生产！");
            try {
                // 当前线程等待
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // 满足生产条件
        curnum += neednum;
        System.out.println("已经生产了" + neednum + "个产品，现在仓库存量为：" + curnum);
        // 唤醒此对象监视器上等待的所有线程
        notifyAll();
    }

    // 消费指定的产品
    public synchronized void consume(int neednum) {

        // 测试是否可消费
        while (curnum < neednum) {
            try {
                // 当前线程等待
                wait();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 满足消费条件进行消费
        curnum -= neednum;
        System.out.println("已经消费了" + neednum + "个产品，现在仓库存量为：" + curnum);
        notifyAll();
    }

}

/**
 * 生产者
 */
class Producer extends Thread {

    private int neednum;
    private Godown godown;

    Producer(int neednum, Godown godown) {
        this.neednum = neednum;
        this.godown = godown;
    }

    public void run() {
        godown.produce(neednum);
    }
}

/**
 * 消费者
 */
class Consumer extends Thread {
    private int neednum;
    private Godown godown;

    Consumer(int neednum, Godown godown) {
        this.neednum = neednum;
        this.godown = godown;
    }

    public void run() {
        godown.consume(neednum);
    }
}