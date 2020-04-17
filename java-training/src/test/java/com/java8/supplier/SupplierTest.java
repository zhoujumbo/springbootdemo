package com.java8.supplier;

import ch.qos.logback.core.spi.FilterReply;
import lombok.Data;
import org.apache.poi.ss.formula.functions.T;
import org.junit.Test;
import org.springframework.data.util.Lazy;
import org.springframework.lang.Nullable;

import java.util.Objects;
import java.util.Optional;
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


//    案例一
//    首先思考一个问题：如何输出日志？(So easy)
//测试(开发)环境与线上环境的日志级别一般不同。测试环境为了调试，一般会开启debug级别，输出一些调试信息便于问题排查；而线上环境一般是处于稳定状态，不太需要输出调试信息，再出于性能考虑，一般会开启info级别，过滤掉debug日志。
//
//    再接着，如果输出的日志里，不再仅仅是简单的句子，而有时候需要包含一个对象(例如远程调用的入参、出参)，怎么办？
//
//            log.debug("invoke remote method, return value: {}", JSON.toJSONString(returnVal));
//    稍一疏忽，很容易写出上述代码(大家可以搜一下自己负责的项目，看看是否到处充斥这样的代码)，究其原因，是被log.debug()的外表所欺骗与迷惑：log.debug()只会在开启debug级别的日志下输出日志，而线上日志级别是info，不会输出，因此没有性能问题。
//
//    诚然，在开启info级别时，这条日志并不会输出，但这里容易被忽视的点是，无论开启何种日志级别，JSON.toJSONString(returnVal)这段代码都会首先被执行，返回值做为log.debug入参后，才会根据日志级别判断是否输出日志。也即是说，即便最终判断不输出日志，也会执行一遍序列化方法。这在被对象很大的时候，容易造成性能问题。(曾经见过输出一屏都装不下的日志，序列化耗时50-70ms)
//
//    如何解决？
//
//    if (log.isDebugEnabled()) {
//        log.debug("invoke remote method, return value: {}", JSON.toJSONString(returnVal));
//    }
//    即先判断，再输出
//
//    但是程序员天性懒惰(懒惰是科技进步的动力)，原来一行代码能解决的事，现在三行代码才能完成，不能忍啊！而且如果需要输出的调试日志有很多，就会出现满屏if (log.isDebugEnabled())，代码会很丑陋，阅读代码时候很容易被干扰正常逻辑
//
//    解决方案：Supplier
//
//    首先定义一个Lazy类，用于延迟计算(懒加载)

//    public class Lazy<T> implements Supplier<T> {
//        private Supplier<T> supplier;
//
//        public static <T> Lazy<T> of(Supplier<T> supplier) {
//            Objects.requireNonNull(supplier, "supplier is null");
//            if (supplier instanceof Lazy) {
//                return (Lazy) supplier;
//            } else {
//                return new Lazy(supplier);
//            }
//        }
//
//        private Lazy(Supplier<T> supplier) {
//            this.supplier = supplier;
//        }
//
//        @Override
//        public T get() {
//            return supplier.get();
//        }
//
//        @Override
//        public String toString() {
//            return supplier.get().toString();
//        }
//    }

    //这时候，日志的输出就变成了

//    log.debug("invoke remote method, return value: {}", Lazy.of(() -> JSON.toJSONString(returnVal)));
//    一行代码，实现了原来三行代码才能实现的功能：判断是否满足输出条件，满足，则执行计算，即延迟计算—>序列化；不满足，则不计算，不执行序列化。
//    以Logback中的源码为例
//      public void debug(String format, Object arg) {
//          filterAndLog_1(FQCN, null, Level.DEBUG, format, arg, null);
//      }
//
//    private void filterAndLog_1(final String localFQCN, final Marker marker, final Level level, final String msg, final Object param, final Throwable t) {
//
//        final FilterReply decision = loggerContext.getTurboFilterChainDecision_1(marker, this, level, msg, param, t);
//
//        if (decision == FilterReply.NEUTRAL) {
//            // 不满足输出条件，直接返回
//            if (effectiveLevelInt > level.levelInt) {
//                return;
//            }
//        } else if (decision == FilterReply.DENY) {
//            return;
//        }
//
//        // 满足输出条件，才会执行Lazy.toString()，即supplier.get().toString()
//        buildLoggingEventAndAppend(localFQCN, marker, level, msg, new Object[] { param }, t);
//    }

//    每次执行这一行代码，会生成一个Supplier实例(Lazy)，并做为log.debug入参，在log.debug中进行判断决定是否要使用该Lazy，即调用Lazy.toString()，如此便达到了延迟计算的效果。
//
//    只谈优点不谈缺点有耍流氓的嫌疑：很显然，每次执行会生成一个Supplier实例。但是我们仔细思考一下：
//
//    我们生成的实例对象并不包含复杂的属性，很轻量，一次分配不需要占用太多空间
//    代码所在方法的生命周期一般比较短，符合朝生夕死的特点
//    实例对象因此会在TLAB或者Young Gen上被分配，并且几乎没有机会晋升到Old Gen就会被回收。
//    因此，这个缺点也就不复存在。


//    案例二
    // code1
//    Long price = Optional.ofNullable(sku)
//        .map(Sku::getPrice)
//        .orElse(0L);

    // code2
//    Long price = Optional.ofNullable(sku)
//            .map(Sku::getPrice)
//            .orElseGet(() -> 0L);

//    Optional作为一种判空的优雅解决方案，会在我们的日常开发中经常使用到，上面两种写法，使用更多的应该是code1：sku或者sku.price中只要任意一个为空，最终价格都为0;code2写法，在这种情况下，会显得很鸡肋，而且也不好理解，为什么有了orElse方法，还额外提供一个orElseGet方法。
//
//    再看下面两种方式，稍稍有些区别

    // code3
//    Object object = Optional.ofNullable(getFromCache())
//            .filter(obj -> validate(obj))
//            .orElse(selectFromDB()); // here

    // code4
//    Object object = Optional.ofNullable(getFromCache())
//            .filter(obj -> validate(obj))
//            .orElseGet(() -> selectFromDB()); // here

    // Optional
//    public T orElseGet(Supplier<? extends T> other) {
//        return value != null ? value : other.get();
//    }

//    含义是：先从缓存中获取对象，然后做一下过滤，如果缓存为空或者过滤之后为空，就重新从DB中加载对象。
//
//    这时候，orElse或者orElseGet里提供的对象，不再是一个简单的数值，而是一个需要经过计算的对象(言外之意：有额外的加载成本)。orElseGet 在此处的作用显而易见：code3中，无论什么情况，都会执行一遍selectFromDB方法，而code4只有缓存为空或过滤之后为空，才会执行selectFromDB方法，即延迟计算(懒加载)。

    /**
     * 输出调试日志
     * @throws Exception
     */
    @Test
    public void test03() throws Exception {


    }
}
