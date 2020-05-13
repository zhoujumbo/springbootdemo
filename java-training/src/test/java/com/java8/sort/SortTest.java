package com.java8.sort;

import org.junit.Test;

import java.util.Comparator;

/**
 * @ClassName SortTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2020/5/12
 * @Version 1.0
 */
public class SortTest {

    @Test
    public void test01() {

//        //返回 对象集合以类属性一升序排序

//        list.stream().sorted(Comparator.comparing(类::属性一));

//        //返回 对象集合以类属性一降序排序 注意两种写法

//        list.stream().sorted(Comparator.comparing(类::属性一).reversed());//先以属性一升序,结果进行属性一降序

//        list.stream().sorted(Comparator.comparing(类::属性一,Comparator.reverseOrder()));//以属性一降序

//        //返回 对象集合以类属性一升序 属性二升序

//        list.stream().sorted(Comparator.comparing(类::属性一).thenComparing(类::属性二));

//        //返回 对象集合以类属性一降序 属性二升序 注意两种写法

//        list.stream().sorted(Comparator.comparing(类::属性一).reversed().thenComparing(类::属性二));//先以属性一升序,升序结果进行属性一降序,再进行属性二升序

//        list.stream().sorted(Comparator.comparing(类::属性一,Comparator.reverseOrder()).thenComparing(类::属性二));//先以属性一降序,再进行属性二升序

//        //返回 对象集合以类属性一降序 属性二降序 注意两种写法

//        list.stream().sorted(Comparator.comparing(类::属性一).reversed().thenComparing(类::属性二,Comparator.reverseOrder()));//先以属性一升序,升序结果进行属性一降序,再进行属性二降序

//        list.stream().sorted(Comparator.comparing(类::属性一,Comparator.reverseOrder()).thenComparing(类::属性二,Comparator.reverseOrder()));//先以属性一降序,再进行属性二降序

//        //返回 对象集合以类属性一升序 属性二降序 注意两种写法

//        list.stream().sorted(Comparator.comparing(类::属性一).reversed().thenComparing(类::属性二).reversed());//先以属性一升序,升序结果进行属性一降序,再进行属性二升序,结果进行属性一降序属性二降序

//        list.stream().sorted(Comparator.comparing(类::属性一).thenComparing(类::属性二,Comparator.reverseOrder()));//先以属性一升序,再进行属性二降序<br><br><br>

//        通过以上例子我们可以发现

//        1. Comparator.comparing(类::属性一).reversed();

//        2. Comparator.comparing(类::属性一,Comparator.reverseOrder());

//        两种排序是完全不一样的,一定要区分开来 1 是得到排序结果后再排序,2是直接进行排序,很多人会混淆导致理解出错,2更好理解,建议使用2
    }
}
