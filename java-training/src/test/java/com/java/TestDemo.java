package com.java;

import com.customer.basic.support.commons.business.json.util.FastJsonUtil;
import com.customer.blindbox.core.common.constants.OSSImgZoom;
import lombok.Builder;
import lombok.Data;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.util.AntPathMatcher;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

public class TestDemo {


    @Test
    public void oSSImgZoom(){
        System.out.println(OSSImgZoom.getJson());

    }

    @Test
    public void antPathMatcherTest(){
        AntPathMatcher antPathMatcher = new AntPathMatcher();
        assertTrue(antPathMatcher.match("/{spring:\\d+}/{id:\\d+}", "/12/3"));

        // test exact matching
        assertTrue(antPathMatcher.match("test", "test"));
        assertTrue(antPathMatcher.match("/test", "/test"));
        assertTrue(antPathMatcher.match("http://example.org", "http://example.org")); // SPR-14141
        assertFalse(antPathMatcher.match("/test.jpg", "test.jpg"));
        assertFalse(antPathMatcher.match("test", "/test"));
        assertFalse(antPathMatcher.match("/test", "test"));

// test matching with ?'s
        assertTrue(antPathMatcher.match("t?st", "test"));
        assertTrue(antPathMatcher.match("??st", "test"));
        assertTrue(antPathMatcher.match("tes?", "test"));
        assertTrue(antPathMatcher.match("te??", "test"));
        assertTrue(antPathMatcher.match("?es?", "test"));
        assertFalse(antPathMatcher.match("tes?", "tes"));
        assertFalse(antPathMatcher.match("tes?", "testt"));
        assertFalse(antPathMatcher.match("tes?", "tsst"));

// test matching with *'s
        assertTrue(antPathMatcher.match("*", "test"));
        assertTrue(antPathMatcher.match("test*", "test"));
        assertTrue(antPathMatcher.match("test*", "testTest"));
        assertTrue(antPathMatcher.match("test/*", "test/Test"));
        assertTrue(antPathMatcher.match("test/*", "test/t"));
        assertTrue(antPathMatcher.match("test/*", "test/"));
        assertTrue(antPathMatcher.match("*test*", "AnothertestTest"));
        assertTrue(antPathMatcher.match("*test", "Anothertest"));
        assertTrue(antPathMatcher.match("*.*", "test."));
        assertTrue(antPathMatcher.match("*.*", "test.test"));
        assertTrue(antPathMatcher.match("*.*", "test.test.test"));
        assertTrue(antPathMatcher.match("test*aaa", "testblaaaa"));
        assertFalse(antPathMatcher.match("test*", "tst"));
        assertFalse(antPathMatcher.match("test*", "tsttest"));
        assertFalse(antPathMatcher.match("test*", "test/"));
        assertFalse(antPathMatcher.match("test*", "test/t"));
        assertFalse(antPathMatcher.match("test/*", "test"));
        assertFalse(antPathMatcher.match("*test*", "tsttst"));
        assertFalse(antPathMatcher.match("*test", "tsttst"));
        assertFalse(antPathMatcher.match("*.*", "tsttst"));
        assertFalse(antPathMatcher.match("test*aaa", "test"));
        assertFalse(antPathMatcher.match("test*aaa", "testblaaab"));

// test matching with ?'s and /'s
        assertTrue(antPathMatcher.match("/?", "/a"));
        assertTrue(antPathMatcher.match("/?/a", "/a/a"));
        assertTrue(antPathMatcher.match("/a/?", "/a/b"));
        assertTrue(antPathMatcher.match("/??/a", "/aa/a"));
        assertTrue(antPathMatcher.match("/a/??", "/a/bb"));
        assertTrue(antPathMatcher.match("/?", "/a"));

// test matching with **'s
        assertTrue(antPathMatcher.match("/**", "/testing/testing"));
        assertTrue(antPathMatcher.match("/*/**", "/testing/testing"));
        assertTrue(antPathMatcher.match("/**/*", "/testing/testing"));
        assertTrue(antPathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla"));
        assertTrue(antPathMatcher.match("/bla/**/bla", "/bla/testing/testing/bla/bla"));
        assertTrue(antPathMatcher.match("/**/test", "/bla/bla/test"));
        assertTrue(antPathMatcher.match("/bla/**/**/bla", "/bla/bla/bla/bla/bla/bla"));
        assertTrue(antPathMatcher.match("/bla*bla/test", "/blaXXXbla/test"));
        assertTrue(antPathMatcher.match("/*bla/test", "/XXXbla/test"));
        assertFalse(antPathMatcher.match("/bla*bla/test", "/blaXXXbl/test"));
        assertFalse(antPathMatcher.match("/*bla/test", "XXXblab/test"));
        assertFalse(antPathMatcher.match("/*bla/test", "XXXbl/test"));

        assertFalse(antPathMatcher.match("/????", "/bala/bla"));
        assertFalse(antPathMatcher.match("/**/*bla", "/bla/bla/bla/bbb"));

        assertTrue(antPathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing/"));
        assertTrue(antPathMatcher.match("/*bla*/**/bla/*", "/XXXblaXXXX/testing/testing/bla/testing"));
        assertTrue(antPathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing"));
        assertTrue(antPathMatcher.match("/*bla*/**/bla/**", "/XXXblaXXXX/testing/testing/bla/testing/testing.jpg"));

        assertTrue(antPathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing/"));
        assertTrue(antPathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing"));
        assertTrue(antPathMatcher.match("*bla*/**/bla/**", "XXXblaXXXX/testing/testing/bla/testing/testing"));
        assertFalse(antPathMatcher.match("*bla*/**/bla/*", "XXXblaXXXX/testing/testing/bla/testing/testing"));

        assertFalse(antPathMatcher.match("/x/x/**/bla", "/x/x/x/"));

        assertTrue(antPathMatcher.match("/foo/bar/**", "/foo/bar")) ;

        assertTrue(antPathMatcher.match("", ""));

        assertTrue(antPathMatcher.match("/{bla}.*", "/testing.html"));

    }









    @Test
    public void test2(){
        Pattern p=Pattern.compile("^[0-2]{1}$");
        Matcher m=p.matcher("a");
        if(!m.find()){
            System.out.println("参数不对");
        }else{
            System.out.println("参数通过");
        }
    }


    @Test
    public void testJSTErpStore(){

//        System.out.println(jstStoreService.getStoreInfo(JstConstamts.nicks));



//        String str = "{\"shops\":[{\"shop_id\":10497387,\"shop_name\":\"拆呗\",\"shop_site\":\"商家自有商城\",\"shop_url\":\"\",\"created\":\"2019-08-19 17:02:38\",\"nick\":\"拆王\",\"session_expired\":null,\"session_uid\":null,\"short_name\":\"拆呗\"}],\"code\":0,\"issuccess\":true,\"msg\":null}";
//        JsonObject jsonObject = (JsonObject) new JsonParser().parse(str);
//        System.out.println(jsonObject.get("shops").getAsJsonArray());
//        System.out.println(jsonObject.get("shops").getAsJsonArray().get(0));
//        System.out.println(jsonObject.get("shops").getAsJsonArray().get(0).getAsJsonObject().get("shop_id"));
//        System.out.println(jsonObject.get("shops").getAsJsonArray().get(0).getAsJsonObject().get("shop_id").getAsString());
//        jsonObject.get("shops").getAsJsonArray().get(0).getAsJsonObject().get("shop_id").getAsString();

    }


    @Test
    public void testMapFunc(){

        Map<String,List<String>> listMap = new HashMap<>();
//        System.out.println(listMap.putIfAbsent("aaa","bbb"));
//        System.out.println(listMap.computeIfAbsent ("aaa",k->"aaa"));
//        System.out.println(listMap.computeIfAbsent ("aaa",k->"bbb"));
//        System.out.println(listMap.get ("aaa"));
//        System.out.println(listMap.computeIfAbsent ("ccc",k->"ccc"));

        List<String> item = Arrays.asList("a","a","a","d","e","f","g");
        item.parallelStream().forEach(i->{
            listMap.computeIfAbsent(i ,k->new ArrayList<>()).add(i);
        });

        System.out.println(listMap.get("a"));

    }

    @Test
    public void testClassCopy(){

        C c = C.builder()
                .c1("cccccc")
                .c2(1)
                .build();

        B b = B.builder()
                .b1("bbbbbb1111")
                .b2(2)
                .c(c).build();
        A a = A.builder()
                .a1("aaaa001")
                .a2("aaaa002")
                .b(b)
                .c(c).build();
        System.out.println(FastJsonUtil.bean2JsonStr(a));

        D d = D.builder().build();
        System.out.println(FastJsonUtil.bean2JsonStr(d));
        E e = E.builder().build();
        System.out.println(FastJsonUtil.bean2JsonStr(e));

        BeanUtils.copyProperties(a,d);
        System.out.println("a:::"+FastJsonUtil.bean2JsonStr(a));
        System.out.println("d:::"+FastJsonUtil.bean2JsonStr(d));
        BeanUtils.copyProperties(a,e);
        System.out.println("a:::"+FastJsonUtil.bean2JsonStr(a));
        System.out.println("e:::"+FastJsonUtil.bean2JsonStr(e));


//        BeanUtils
    }
}




@Data
@Builder(toBuilder = true)
class A{
    private String a1;
    private String a2;
    private String a3;
    private B b;
    private C c;
}
@Data
@Builder(toBuilder = true)
class B{
    private String b1;
    private int b2;
    private C c;
}

@Data
@Builder(toBuilder = true)
class C{
    private String c1;
    private int c2;

}

@Data
@Builder(toBuilder = true)
class D{
    private String a1;
    private int a2;
    private String a3;
    private B b;
    private C c;
}

@Data
@Builder(toBuilder = true)
class E{
    private String e1;
    private String a3;
    private B bb;
    private C cc;
}
