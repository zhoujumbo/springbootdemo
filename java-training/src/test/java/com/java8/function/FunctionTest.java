package com.java8.function;

import org.junit.Test;

import java.util.function.Function;

public class FunctionTest {

    /**
     * apply
     * Function<T, R>  T代表输入参数，R代表返回的结果
     */
    @Test
    public void test01(){
        Function<Integer,Integer> test= i->i+1;
        test.apply(5);
        /** print:6*/

        Function<Integer,Integer> test1=i->i+1;
        Function<Integer,Integer> test2=i->i*i;
        System.out.println(calculate(test1,5));
        System.out.println(calculate(test2,5));

        // 我们通过传入不同的Function，实现了在同一个方法中实现不同的操作。
        // 在实际开发中这样可以大大减少很多重复的代码，比如我在实际项目中有个新增用户的功能，但是用户分为VIP和普通用户，且有两种不同的新增逻辑。那么此时我们就可以先写两种不同的逻辑。除此之外，这样还让逻辑与数据分离开来，我们可以实现逻辑的复用。

        // 当然实际开发中的逻辑可能很复杂，比如两个方法F1,F2都需要两个个逻辑AB，
        // 但是F1需要A->B，F2方法需要B->A。这样的我们用刚才的方法也可以实现

        Function<Integer,Integer> A=i->i+1;
        Function<Integer,Integer> B=i->i*i;
        System.out.println("F1:"+B.apply(A.apply(5)));
        System.out.println("F2:"+A.apply(B.apply(5)));


        // 也很简单呢，但是这还不够复杂，假如我们F1,F2需要四个逻辑ABCD，那我们还这样写就会变得很麻烦了
        // 用test02 可以解决该问题
    }

    public static Integer calculate(Function<Integer,Integer> test,Integer number){
        return test.apply(number);
    }

    /**
     * compose和andThen
     * compose接收一个Function参数，返回时先用传入的逻辑执行apply，然后使用当前Function的apply
     * andThen跟compose正相反，先执行当前的逻辑，再执行传入的逻辑
     * compose等价于B.apply(A.apply(5))，而andThen等价于A.apply(B.apply(5))
     */
    @Test
    public void test02(){
        Function<Integer,Integer> A=i->i+1;
        Function<Integer,Integer> B=i->i*i;
        System.out.println("F1:"+B.apply(A.apply(5)));
        System.out.println("F1:"+B.compose(A).apply(5));
        System.out.println("F2:"+A.apply(B.apply(5)));
        System.out.println("F2:"+B.andThen(A).apply(5));
        /** F1:36 */
        /** F1:36 */
        /** F2:26 */
        /** F2:26 */

        // 上述两个方法的返回值都是一个Function，这样我们就可以使用建造者模式的操作来使用
        B.compose(A).compose(A).andThen(A).apply(5);
    }






//     @FunctionalInterface
//     public interface Function<T, R> {
//         R apply(T t);
//         /**
//          * @return a composed function that first applies the {@code before}
//          * function and then applies this function
//          */
//         default <V> Function<V, R> compose(Function<? super V, ? extends T> before) {
//             Objects.requireNonNull(before);
//             return (V v) -> apply(before.apply(v));
//         }
//         /**
//          * @return a composed function that first applies this function and then
//          * applies the {@code after} function
//          */
//         default <V> Function<T, V> andThen(Function<? super R, ? extends V> after) {
//             Objects.requireNonNull(after);
//             return (T t) -> after.apply(apply(t));
//         }
//     }

}
