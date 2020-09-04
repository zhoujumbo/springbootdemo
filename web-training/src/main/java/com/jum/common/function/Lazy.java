package com.jum.common.function;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * @ClassName Lazy
 * @Description 用于延迟计算(懒加载) 先判断，再输出 依赖 @see:java.util.function.Supplier
 * 具体应用可参照Logback中的源码
 * 每次执行会生成一个Supplier实例
 * Optional 中的 orElseGet方法也是类似实现
 * @use Lazy.of(() -> // 执行的方法调用)
 * @Author jb.zhou
 * @Date 2020/5/13
 * @Version 1.0
 */
public class Lazy<T> implements Supplier<T> {
        private Supplier<T> supplier;

        public static <T> Lazy<T> of(Supplier<T> supplier) {
            Objects.requireNonNull(supplier, "supplier is null");
            if (supplier instanceof Lazy) {
                return (Lazy) supplier;
            } else {
                return new Lazy(supplier);
            }
        }

        private Lazy(Supplier<T> supplier) {
            this.supplier = supplier;
        }

        @Override
        public T get() {
            return supplier.get();
        }

        @Override
        public String toString() {
            return supplier.get().toString();
        }
}
