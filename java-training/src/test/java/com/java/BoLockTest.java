package com.java;

import com.customer.basic.support.commons.business.lock.BoLock;
import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class BoLockTest {

    @Test
    public void bolockTets(){
        AtomicInteger a = BoLock.getLock("123");
        System.out.println("a:"+a);
        AtomicInteger b = BoLock.getLock("123");
        System.out.println("b:"+b);
        System.out.println(a==b);
        System.out.println(a.equals(b));


        AtomicInteger c = BoLock.getLock("345");
        System.out.println("c:"+c);
        AtomicInteger d = BoLock.getLock("345");
        BoLock.getLock("345");BoLock.getLock("345");BoLock.getLock("345");
        System.out.println("d:"+d);
        System.out.println(c==d);
        System.out.println(c.equals(d));

        System.out.println("++++++++++++");
        System.out.println(a==c);
        System.out.println(a.equals(c));
    }


}
