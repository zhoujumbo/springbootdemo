
> 在《java8实战》这本书中，对Lambda的解释如下：
  
> 可以把Lambda表达式理解为简洁的表示可传递的匿名函数的一种方式：它没有名称，但它有参数列表，函数主体，返回类型，可能还有一个可以抛出的异常列表。

```text
  lambda表达式基础语法：
      java中，引入了一个新的操作符“->”，该操作符在很多资料中，称为箭头操作符，或者lambda操作符；箭头操作符将lambda分成了两个部分：
  
      1.   左侧：lambda表达式的参数列表
      2.   右侧：lambda表达式中所需要执行的功能，即lambda函数体
      3.lambda表达式语法格式；
  
          1）.无参数，无返回值的用法 ：() -> System.out.println("hello lambda");
   
    @Test
	public void test1() {
		Runnable r = new Runnable() {
			@Override
			public void run() {
				System.out.println("hello runnable");
			}
		};
		r.run();
		Runnable r1 = () -> System.out.println("hello lambda");
		r1.run();
	}
```