package com.demo.thread.demo1;


public class NotifyDemo {

//    @Test
    public void test(){

        ThreadB b = new ThreadB();

        b.start();

        // 取得b的锁
        synchronized (b){
            try{
                System.out.println("等待B完成计算。。。");
                // 放弃b的锁
                b.wait();
            }catch(Exception e){
                e.printStackTrace();
            }
            System.out.println("b计算结果："+b.total);
        }
    }
}

class ThreadB extends Thread{
    int total;

    public void run(){
        // 拿到自己的锁 b
        synchronized (this){
            for(int i=0;i<101;i++){
                total+=i;
            }
            // 计算完成 唤醒 只有该同步结束后才会放弃b锁
            notify();
        }
    }
}