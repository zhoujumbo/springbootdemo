package com.java;/**
 * @Description： TODO
 * @Author: zhoujumbo
 * @Date: ${Time} ${Date}
 */

import org.junit.Test;

/**
 * @ClassName MathTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/9/4
 * @Version 1.0
 */
public class MathTest {
    @Test
    public void test() {
        System.out.println(Math.PI);//得到圆周率
        System.out.println(Math.E);//自然对底数
        //2的5次方
        System.out.println(Math.pow(2, 5));
        //使用位运算求2的5次方
        System.out.println(1 << 5);
        //对16开平方根
        System.out.println(Math.sqrt(16));
        //求两值之间最大、最小
        System.out.println(Math.max(5, 2));
        System.out.println(Math.min(5, 2));
        //三角函数
        System.out.println(Math.sin(60));//三角正弦
        System.out.println(Math.cos(60));//三角余弦
        System.out.println(Math.tan(60));//三角正切

    }


}
