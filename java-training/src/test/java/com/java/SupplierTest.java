package com.java;

import lombok.Data;
import org.junit.Test;
import org.springframework.lang.Nullable;

import java.util.function.Supplier;

/**
 * @ClassName SupplierTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/11/20
 * @Version 1.0
 */
public class SupplierTest {


    /**
     * Function<T, R> 

     T：入参类型，R：出参类型

     调用方法：R apply(T t); 

     定义函数示例：Function<Integer, Integer> func = p -> p * 10;    // 输出入参的10倍

     调用函数示例：func.apply(10);    // 结果100

     Consumer<T>

     T：入参类型；没有出参

     调用方法：void accept(T t);

     定义函数示例：Consumer<String> consumer= p -> System.out.println(p);    // 因为没有出参，常用于打印、发送短信等消费动作

     调用函数示例：consumer.accept("18800008888");

     Supplier<T>

     T：出参类型；没有入参

     调用方法：T get();

     定义函数示例：Supplier<Integer> supplier= () -> 100;    // 常用于业务“有条件运行”时，符合条件再调用获取结果的应用场景；运行结果须提前定义，但不运行。

     调用函数示例：supplier.get();

     Predicate<T>

     T：入参类型；出参类型是Boolean

     调用方法：boolean test(T t);

     定义函数示例：Predicate<Integer> predicate = p -> p % 2 == 0;    // 判断是否、是不是偶数

     调用函数示例：predicate.test(100);    // 运行结果true
     */


    @Test
    public void test01(){

//创建Supplier容器，声明为TestSupplier类型，此时并不会调用对象的构造方法，即不会创建对象
        Supplier<TestSupplier> sup= TestSupplier::new;
        System.out.println("--------");
        //调用get()方法，此时会调用对象的构造方法，即获得到真正对象
        sup.get();
        //每次get都会调用构造方法，即获取的对象不同
        sup.get();
        System.out.println(sup.get().getVar1());

    }


    @Test
    public void test02(){
//        Supplier<String> message = ()->"";
        String var2 = nullSafeGet(()->{
            System.out.println("do some-thing");
            return "hehehe";
        });
        System.out.println(var2);
    }

    @Nullable
    private static String nullSafeGet(@Nullable Supplier<String> messageSupplier) {
        return messageSupplier != null ? (String)messageSupplier.get() : null;
    }


    @Data
    public class TestSupplier{
        TestSupplier(){
            System.out.println("构造创建");
        }

        private int var1 = 1;

    }


}
