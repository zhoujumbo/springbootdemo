package com.java;/**
 * @Description： TODO
 * @Author: zhoujumbo
 * @Date: ${Time} ${Date}
 */

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ArraysTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/9/4
 * @Version 1.0
 */
public class ArraysTest {

    @Test
    public void test(){
        int[] arr = {72,52,54,63,42,65,46,12,34,6,43};
        String[][] str = {
                {"A","B","C"}, {"D","E","F","G"} };
        //进行升序排序
        Arrays.sort(arr);
        //返回一个包含指定数组元素的字符串
        String arrStr  = Arrays.toString(arr);
        System.out.println(arrStr);
        //降序获取数组元素
        for(int i = arr.length-1; i >= 0 ; i--) {
            System.out.print(arr[i] + " ");
        }
        //把数组转成一个ArrayList对象,该对象由静态内部类维护
        List li = Arrays.asList(arr);
        //使用二分查找法(按升序)
        System.out.println("\n查询的值所在的index:" + Arrays.binarySearch(arr,46));
//		//对二维数组进行操作会出现错误
//		Arrays.sort(str);

        /**
         * Arrays工具类只能对一维数组进行操作，二维及以上会报异常。该工具类的常用方法sort()和binarySerach()默认是按照升序，
         * 也可以在参数中自定义比较规则。实现降序的方式就有很多种了，可以先排序然后反向遍历。或者转成集合，使用Collections的reserve()进行元素反转，
         * 又转成数组。也可以自己写排序方法
         */
    }
}
