package com.java;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.customer.blindbox.core.common.exception.ArgumentRequirementException;
import lombok.Data;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

/**
 * @ClassName OptionalTest
 * @Description java.util.Optional
 * @Author jb.zhou
 * @Date 2019/11/11
 * @Version 1.0
 */
public class OptionalTest {

    /**
     * 1. isPresent() 与 obj != null 无任何区别, 我们的生活依然在步步惊心. 而没有 isPresent() 作铺垫的 get() 调用在 IntelliJ IDEA中会收到告警。
     * 调用 Optional.get() 前不事先用 isPresent() 检查值是否可用. 假如 Optional 不包含一个值, get() 将会抛出一个异常！
     * 2. 把 Optional 类型用作属性或是方法参数在 IntelliJ IDEA 中更是强力不推荐的！
     * 3. 使用任何像 Optional 的类型作为字段或方法参数都是不可取的. Optional 只设计为类库方法的,
     * 可明确表示可能无值情况下的返回类型. Optional 类型不可被序列化, 用作字段类型会出问题的！！！
     */


    // Ps: isPresent API 是用来检查Optional对象中是否有值。
    // 只有当我们创建了一个含有非空值的Optional时才返回true。在下一部分我们将介绍这个API。
    @Test
    public void test01(){
        Optional<String> empty = Optional.empty();
        assertFalse(empty.isPresent());
    }

    // Optional.of()
    @Test
    public void test02(){
        String name = "baeldung";
        Optional<String> opt = Optional.of(name);
        assertEquals("Optional[baeldung]", opt.toString());

        // 传递给of()的值不可以为空，否则会抛出空指针异常
        String name2 = null;
        Optional<String> opt2 = Optional.of(name2);
    }

    // Optional.ofNullable()
    @Test
    public void test03(){
        String name = "baeldung";
        Optional<String> opt = Optional.ofNullable(name);
        assertEquals("Optional[baeldung]", opt.toString());

        // 使用ofNullable API，则当传递进去一个空值时，
        // 不会抛出异常，而只是返回一个空的Optional对象，如同我们用Optional.empty API
        String name2 = null;
        Optional<String> opt2 = Optional.ofNullable(name2);
        assertEquals("Optional.empty", opt2.toString());
    }


    // Optional.isPresent()
    // 检查一个Optional对象中是否有值，只有值非空才返回true。
    @Test
    public void test04(){
        Optional<String> opt = Optional.of("Baeldung");
        assertTrue(opt.isPresent());

        opt = Optional.ofNullable(null);
        assertFalse(opt.isPresent());

        //
        Optional<String> opt2 = Optional.of("baeldung");

        opt2.ifPresent(name -> System.out.println(name.length()));
    }

    // orEse && orElseGet
    // 检索Optional对象中的值，它被传入一个“默认参数‘。如果对象中存在一个值，则返回它，否则返回传入的“默认参数”
    @Test
    public void test05(){
        // orElse
        String nullName = null;
        String name = Optional.ofNullable(nullName).orElse("john");
        assertEquals("john", name);

        // orElseGet 与orElsel类似，但是这个函数不接收一个“默认参数”，而是一个函数接口
        String name2 = Optional.ofNullable(nullName).orElseGet(() -> "john");
        assertEquals("john", name2);

        // 两者区别
        String text = null;

        System.out.println("Using orElseGet:");
        String defaultText = Optional.ofNullable(text).orElseGet(this::getName);
        assertEquals("Default Value", defaultText);

        System.out.println("Using orElse:");
        defaultText = Optional.ofNullable(text).orElse(getName());
        assertEquals("Default Value", defaultText);
        // 当参数为空时效果一样
        // 当值存在时，orElse相比于orElseGet，多创建了一个对象，
        // 可能从这个实例中你感受不到影响有多大，但考虑当getDefalut不仅仅是个简单函数，而是一个web service之类的，则多创建一个代价是比较大的。


        // orElseThrow  orElseThrow当遇到一个不存在的值的时候，并不返回一个默认值，而是抛出异常

        String name3 = Optional.ofNullable(nullName).orElseThrow(
                IllegalArgumentException::new);
    }

    private String getName(){
        System.out.println("Getting Default Value");
        return "Default Value";
    }


    // get()
    // 使用get() API 也可以返回被包裹着的值。但是必须是值存在时，
    // 当值不存在时，它会抛出一个NoSuchElementException异常
 @Test
    public void test06(){
     Optional<String> opt = Optional.of("baeldung");
     String name = opt.get();

     assertEquals("baeldung", name);

     //当值不存在时，它会抛出一个NoSuchElementException异常
//     Optional<String> opt = Optional.ofNullable(null);
//     String name = opt.get();
     // 因为这个方法与我们使用Optional的目的相违背，所以可以预见在不久将来它或许会被抛弃，建议还是使用其他的方法。
    }

    // filter()
    // 接收一个函数式接口，当符合接口时，则返回一个Optional对象，否则返回一个空的Optional对象。
    // 这个API作用一般就是拒绝掉不符合条件的
    @Test
    public void test07(){
        Integer year = 2016;
        Optional<Integer> yearOptional = Optional.of(year);
        boolean is2016 = yearOptional.filter(y -> y == 2016).isPresent();
        assertTrue(is2016);
        boolean is2017 = yearOptional.filter(y -> y == 2017).isPresent();
        assertFalse(is2017);

        // 另一种判断对象值范围的过滤 DEMO
        // 例子代码如下注释
//        public class Modem {
//            private Double price;
//
//            public Modem(Double price) {
//                this.price = price;
//            }
//            //standard getters and setters
//        }

//        public boolean priceIsInRange1(Modem modem) {
//            boolean isInRange = false;
//
//            if (modem != null && modem.getPrice() != null
//                    && (modem.getPrice() >= 10
//                    && modem.getPrice() <= 15)) {
//
//                isInRange = true;
//            }
//            return isInRange;
//        }
        // 判断
        //  assertTrue(priceIsInRange1(new Modem(10.0)));
//        assertFalse(priceIsInRange1(new Modem(9.9)));
//        assertFalse(priceIsInRange1(new Modem(null)));
//        assertFalse(priceIsInRange1(new Modem(15.5)));
//        assertFalse(priceIsInRange1(null));
//          如果长时间不用，那么有可能会忘记对null进行检查，那么如果使用Optional，会怎么样呢？
//        public boolean priceIsInRange2(Modem modem2) {
//            return Optional.ofNullable(modem2)
//                    .map(Modem::getPrice)
//                    .filter(p -> p >= 10)
//                    .filter(p -> p <= 15)
//                    .isPresent();
//        }

        // map()仅仅是将一个值转换为另一个值，请谨记在心，这个操作并不会改变原来的值。
//        让我们仔细看看这段代码，首先，当我们传入一个null时，不会发生任何问题。其次，我们在这段code所写的唯一逻辑就如同此方法名所描述。
//        之前的那段code为了其固有的脆弱性，必须做更多，而现在不用了。

    }


    // map()
    // 我们使用filter()来接受/拒绝一个一个值，而使用map()我们可以将一个值转换为另一个值。
    @Test
    public void test08(){

        List<String> companyNames = Arrays.asList(
                "paypal", "oracle", "", "microsoft", "", "apple");

        Optional<List<String>> listOptional = Optional.of(companyNames);
        // 获取list size
        int size = listOptional
                .map(List::size)
                .orElse(0);
        assertEquals(6, size);

        // map()返回的结果也被包裹在一个Optional对象中，我们必须调用合适的方法来查看其中的值。
        // 而map()是使用现有的值进行计算，并且返回一个包裹着计算结果(映射结果)的Optional对象
        String name = "baeldung";
        Optional<String> nameOptional = Optional.of(name);
        // 获取string length
        int len = nameOptional
                .map(String::length).orElse(0);
        assertEquals(8, len);

        // 将filter()与map()一起使用可以做一些很强力的事情。
        // 假设我们现在要检查一个用户的密码，那么我们可以这样做:

        String password = " password ";
        Optional<String> passOpt = Optional.of(password);
        boolean correctPassword = passOpt.filter(
                pass -> pass.equals("password")).isPresent();
        assertFalse(correctPassword);

        correctPassword = passOpt
                .map(String::trim)
                .filter(pass -> pass.equals("password"))
                .isPresent();
        assertTrue(correctPassword);
        // 注意到，如果不进行trim，则会返回false，这里我们可以使用map()进行trim。
    }


    //  flatmap()
    // 可以使用flatmap()替换map()，二者不同之处在于，map()只有当值不被包裹时才进行转换，
    // 而flatmap()接受一个被包裹着的值并且在转换之前对其解包。
    @Test
    public void test09(){

        Person person = new Person("john", 26);
        Optional<Person> personOptional = Optional.of(person);

        Optional<Optional<String>> nameOptionalWrapper
                = personOptional.map(Person::getName);
        Optional<String> nameOptional
                = nameOptionalWrapper.orElseThrow(IllegalArgumentException::new);
        String name1 = nameOptional.orElse("");
        assertEquals("john", name1);

        String name = personOptional
                .flatMap(Person::getName)
                .orElse("");
        assertEquals("john", name);

        // 需要注意，方法getName返回的是一个Optional对象，而不是像trim那样。这样就生成了一个嵌套的Optional对象。
        // 因此使用map，我们还需要再解包一次，而使用flatMap()就不需要了。
    }

    public class Person {
        private String name;
        private int age;

        public Person(String name, int age){
            this.name = name;
            this.age = age;
        }
        public Optional<String> getName() {
            return Optional.ofNullable(name);
        }

        public Optional<Integer> getAge() {
            return Optional.ofNullable(age);
        }

    }


    @Data
    public class User {
        private String name;
        private int age;
    }


    @Test
    public void test10(){

        User user1 = new User();
        user1.setAge(18);
        user1.setName("tom");

        User user2 = new User();
        user2.setAge(1);

        Optional.ofNullable(user1)
                .filter(user -> Optional.ofNullable(user1.getName()).isPresent()
                && Optional.ofNullable(user1.getAge()).isPresent())
                .orElseThrow(() -> new ArgumentRequirementException("111"));

        System.out.println("1-true");
        Optional.ofNullable(user2)
                .filter(user -> Optional.ofNullable(user.getName()).isPresent()
                        && Optional.ofNullable(user.getAge()).isPresent())
                .orElseThrow(() -> new ArgumentRequirementException("111"));
        System.out.println("2-true");
    }

    @Test
    public void test11(){

        User user1 = new User();
        user1.setAge(18);
        user1.setName("tom");

        User user2 = new User();
        user2.setAge(1);
        user2.setName("tom");

        List<User> userList = Lists.newArrayList(user1,user2);
        List<User> userList2 = Lists.newArrayList();

        List<User> result = Optional.ofNullable(userList2)
                .map(ul->ul.stream().map(u->u).collect(Collectors.toList()))
                .orElse(Collections.emptyList());
        System.out.println(JSON.toJSONString(result));

    }


}
