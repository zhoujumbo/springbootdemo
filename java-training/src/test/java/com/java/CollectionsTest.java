package com.java;/**
 * @Description： TODO
 * @Author: zhoujumbo
 * @Date: ${Time} ${Date}
 */

import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;

/**
 * @ClassName CollectionsTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/9/4
 * @Version 1.0
 */
public class CollectionsTest {


    @Test
    public void test() {

        ArrayList<Integer> al = new ArrayList<>();
        al.add(18);
        al.add(35);
        al.add(-5);
        al.add(9);
        al.add(15);
        System.out.println("原始元素序列：" + al);
        Collections.sort(al); //对集合元素进行升序排序
        System.out.println("排序之后：" + al);
        Collections.reverse(al);
        System.out.println("集合元素反转：" + al);
        /** 对指定List集合的两个index进行值交换*/
        Collections.swap(al, 1, 2);
        System.out.println("交换index=1,与index=2的值：" + al);
        /**获取集合元素中的最大值*/
        System.out.println("获取元素最大值：" + Collections.max(al));
        System.out.println("获取元素最小值：" + Collections.min(al));
        //元素随机化，shuffle英文意思：洗牌
        Collections.shuffle(al);
        System.out.println("元素随机：" + al);
        //把线程不安全集合类转成安全的集合类,很常用
        Collections.synchronizedList(al);

        /**
         * Collections提供了不少处理集合的方法，很方便。尤其是把线程不安全的集合类转成线程安全集合类，
         * 这样就无需担心那些集合类是安全的还是不安全的。上面的实例只是简要使用了Collecitons的方法，想了解更多用处请自行测试。
         */
    }
}
