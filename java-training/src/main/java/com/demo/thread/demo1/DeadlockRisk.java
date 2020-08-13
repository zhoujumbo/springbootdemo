package com.demo.thread.demo1;

/**
 * 死锁
 */
public class DeadlockRisk {

    private static class Resource {
        public int value;
    }

    private Resource resourceA = new Resource();
    private Resource resourceB = new Resource();

    public int read() {
        System.out.println("read1");
        synchronized (resourceA) {
            System.out.println("read2");
            synchronized (resourceB) {
                System.out.println("read3");
                return resourceB.value + resourceA.value;
            }
        }
    }

    public void write(int a, int b) {
        System.out.println("write1");
        synchronized (resourceB) {
            System.out.println("write2");
            synchronized (resourceA) {
                System.out.println("write3");
                resourceA.value = a;
                resourceB.value = b;
            }
        }
    }

}
