package com.java8.consumer;

import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;

import java.util.Objects;
import java.util.function.*;

public class ConsumerTest {

//    Consumer的语义是消费的意思
//    接收一个泛型的参数T，然后调用accept，对这个参数做一系列的操作，没有返回值

//    @FunctionalInterface
//    public interface Consumer<T> {
//
//
//        void accept(T t);
//
//        default Consumer<T> andThen(Consumer<? super T> after) {
//            Objects.requireNonNull(after);
//            return (T t) -> { accept(t); after.accept(t); };
//        }
//    }


//    与Consumer相关的接口
//    BiConsumer<T, U>
//            处理一个两个参数
//
//    DoubleConsumer
//            处理一个double类型的参数
//
//    IntConsumer
//            处理一个int类型的参数
//
//    LongConsumer
//            处理一个long类型的参数
//
//    ObjIntConsumer
//            处理两个参数,且第二个参数必须为int类型
//
//    ObjLongConsumer
//            处理两个参数,且第二个参数必须为long类型


    @Test
    public void test01() throws Exception {

        Consumer<Integer> consumer = x -> {
            int a = x + 2;
            System.out.println(a);// 12
            System.out.println(a + "_");// 12_
        };
        consumer.accept(10);

    }
}
