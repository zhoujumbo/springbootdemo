package com.java;

import org.apache.http.util.Asserts;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * @ClassName AssertUtilTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/11/20
 * @Version 1.0
 */
public class AssertUtilTest {

    private int pint;
    private Integer pInteger;
    private char pChar;
    private String pString;
    private BigDecimal pBigdecimal;
    private Double pDouble;
    private double pdouble;
    private List<String> pList;
    private Map<String, String> pMap;

    /**
     * apache.http.util.Asserts
     */
    @Test
    public void test01() {
        Asserts.check(true, "");
        Asserts.notBlank(pString, "");
        Asserts.notEmpty(pString, "");
        Asserts.notNull(pList, "");
    }

    /**
     * org.springframework.util.Assert
     */
    @Test
    public void test02() {

//        Assert.isNull(pList, "");
//        Assert.notNull();
//        Assert.isTrue();
//        Assert.doesNotContain();
//        Assert.hasLength();
//        Assert.hasText();
//        Assert.isAssignable();
//        Assert.isInstanceOf();
//        Assert.noNullElements();
//        Assert.notEmpty();
//        Assert.state();

    }

    /**
     *
     */
    @Test
    public void test03() {

        // AssertUtils.notNull()  // 兼容对象和字符串判断
        // AssertUtils.notNull()  // 多参数  Object... expression
        // AssertUtils.isTrue()  //  多参数  Object... expression
        // AssertUtils.isFalse()  // 多参数  Object... expression
        // AssertUtils.isEmpty()  // 扩展
        // AssertUtils.error()  // 扩展
        // AssertUtils.result()  // 扩展 等同于 isTrue()

    }


}
