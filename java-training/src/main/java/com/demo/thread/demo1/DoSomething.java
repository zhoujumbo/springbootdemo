package com.demo.thread.demo1;

public class DoSomething implements Runnable {
    private String name;

    public DoSomething(String name) {
        this.name = name;
    }

    public void run() {
        for (int i = 0; i < 5; i++) {
            try {
                Thread.sleep(50);
                System.out.println(name + ":" + i);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


}
