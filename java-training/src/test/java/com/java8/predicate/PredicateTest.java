package com.java8.predicate;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class PredicateTest {

    /**
     * Predicate 接口只有一个参数，返回boolean类型。该接口包含多种默认方法来将Predicate组合成其他复杂的逻辑（比如：与，或，非）
     */

    @Test
    public void test01() {

        Predicate<String> predicate = (s) -> s.length() > 0;

        predicate.test("jum");              // true
        predicate.negate().test("jum");     // false

        Predicate<Boolean> nonNull = Objects::nonNull;
        Predicate<Boolean> isNull = Objects::isNull;

        Predicate<String> isEmpty = String::isEmpty;
        Predicate<String> isNotEmpty = isEmpty.negate();

    }

    /**
     * and，or和negate
     */
    @Test
    public void test02() {

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        List<Integer> list = new ArrayList<>();
        for (int i : numbers) {
            list.add(i);
        }
        Predicate<Integer> p1 = i -> i > 5;
        Predicate<Integer> p2 = i -> i < 20;
        Predicate<Integer> p3 = i -> i % 2 == 0;
        List test = list.stream().filter(p1.and(p2).and(p3)).collect(Collectors.toList());
        System.out.println(test.toString());
        /** print:[6, 8, 10, 12, 14]*/

        /**
         * 我们定义了三个断言p1,p2,p3。现在有一个从1~15的list，我们需要过滤这个list。上述的filter是过滤出所有大于5小于20，并且是偶数的列表。
         *假如突然我们的需求变了，我们现在需要过滤出奇数。那么我不可能直接去改Predicate，因为实际项目中这个条件可能在别的地方也要使用。那么此时我只需要更改filter中Predicate的条件。
         */
        List test2 = list.stream().filter(p1.and(p2).and(p3.negate())).collect(Collectors.toList());
        /** print:[7, 9, 11, 13, 15]*/
    }

    /**
     * isEqual
     */
    @Test
    public void test03() {

        int[] numbers = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12, 13, 14, 15};
        List<Integer> list = new ArrayList<>();
        for (int i : numbers) {
            list.add(i);
        }
        Predicate<Integer> p1 = i -> i > 5;
        Predicate<Integer> p2 = i -> i < 20;
        Predicate<Integer> p3 = i -> i % 2 == 0;
        List test = list.stream()
                .filter(p1.and(p2).and(p3.negate()).and(Predicate.isEqual(7)))
                .collect(Collectors.toList());
        /** print:[7] */

    }
}
