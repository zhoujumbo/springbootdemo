package com.java.doc;

/**
 * 写在类上的标注一般分为三部分：
 * 概要描述，通常用一句或者一段话简要描述该类的作用，以英文句号作为结束
 * 详细描述，通常用一段或者多段话来详细描述该类的作用，一般每段话都以英文句号作为结束
 * 文档标注，用于标注作者、创建时间、参阅类等信息
 *
 * 单行：
 * Miscellaneous {@link String} utility methods.
 *
 * 多行：
 * Class {@code Object} is the root of the class hierarchy.
 * Every class has {@code Object} as a superclass. All objects,
 * including arrays, implement the methods of this class.
 *
 * 文档标记：
 * @author ： 纯文本 或者添加邮件地址，博客地址
 * @version ：用于标记当前版本，默认为1.0
 * @since ：从以下版本开始
 * @see ：另请参阅，一般用于标记该类相关联的类,@see即可以用在类上，也可以用在方法上。
 * @link ： {@link 包名.类名#方法名(参数类型)} 用于快速链接到相关代码
 *      {@link java.lang.Character} // 完全限定的类名
 *      {@link String} // 省略包名
 *      {@link #length()}  // 省略类名，表示指向当前的某个方法
 *      {@link java.lang.String#charAt(int)} // 包名.类名.方法名(参数类型)
 * @code ： {@code text} 将文本标记为code  会被解析成<code> text </code> 一般在Javadoc中只要涉及到类名或者方法名，
 *          都需要使用@code进行标记。在code内部可以使用 < 、> 等不会被解释成html标签, code标签有自己的样式
 * @param ： 一般类中支持泛型时会通过@param来解释泛型的类型  @param <E> the type of elements in this list
 * @Deprecated ： 废弃该方法，可以用到类或者方法上
 */

/**
 * 写在方法上的文档标注一般分为三段：
 * 第一段：概要描述，通常用一句或者一段话简要描述该方法的作用，以英文句号作为结束
 * 第二段：详细描述，通常用一段或者多段话来详细描述该方法的作用，一般每段话都以英文句号作为结束
 * 第三段：文档标注，用于标注参数、返回值、异常、参阅等
 *
 * 方法详细描述上经常使用html标签来，通常都以p标签开始，而且p标签通常都是单标签，不使用结束标签，其中使用最多的就是p标签和pre标签,ul标签, i标签。
 *
 * pre元素可定义预格式化的文本。被包围在pre元素中的文本通常会保留空格和换行符。而文本也会呈现为等宽字体，pre标签的一个常见应用就是用来表示计算机的源代码。
 *
 * 一般p经常结合pre使用，或者pre结合@code共同使用(推荐@code方式)
 * 一般经常使用pre来举例如何使用方法
 *
 * 注意：pre>标签中如果有小于号、大于号、例如泛型 在生产javadoc时会报错
 *
 * @author ： 纯文本 或者添加邮件地址，博客地址
 * @version ：用于标记当前版本，默认为1.0
 * @since ：从以下版本开始
 * @see ：另请参阅，一般用于标记该方法相关联的类,@see即可以用在类上，也可以用在方法上。
 * @link ： {@link 包名.类名#方法名(参数类型)} 用于快速链接到相关代码
 *      {@link java.lang.Character} // 完全限定的类名
 *      {@link String} // 省略包名
 *      {@link #length()}  // 省略类名，表示指向当前的某个方法
 *      {@link java.lang.String#charAt(int)} // 包名.类名.方法名(参数类型)
 * @code ： {@code text} 将文本标记为code  会被解析成<code> text </code> 一般在Javadoc中只要涉及到类名或者方法名，
 *          都需要使用@code进行标记。在code内部可以使用 < 、> 等不会被解释成html标签, code标签有自己的样式
 * @param ： 后面跟参数名，再跟参数描述  @param str the {@code CharSequence} to check (may be {@code null})
 * @return ：跟返回值的描述  @return {@code true} if the {@code String} is not {@code null}, its
 * @exception ：用于描述方法签名throws对应的异常  @exception IllegalArgumentException if <code>key</code> is null.
 * @throws ：跟异常类型 异常描述 , 用于描述方法内部可能抛出的异常 @throws IllegalArgumentException when the given source contains invalid
 * encoded sequences
 * @value： 用于标注在常量上，{@value} 用于表示常量的值
 * @inheritDoc ：用于注解在重写方法或者子类上，用于继承父类中的Javadoc； 基类的文档注释被继承到了子类；子类可以再加入自己的注释（特殊化扩展）；@return @param @throws 也会被继承
 *
 * @Deprecated ： 废弃该方法，可以用到类或者方法上
 *
 */




/**
 * @ClassName JavaDocTest
 * @Description 记录java doc 注解使用
 * 官网： https://docs.oracle.com/javase/7/docs/technotes/tools/windows/javadoc.html
 *
 * @Author jb.zhou
 * @Date 2020/5/13
 * @Version 1.0
 */
public class JavaDocTest {



}
