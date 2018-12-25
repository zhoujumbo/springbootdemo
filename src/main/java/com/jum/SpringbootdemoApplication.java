package com.jum;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// 手动设置包扫描和服务注册
//@ComponentScan("com.jum.controller,com.jum.exception")   // 默认扫描包
//@ComponentScan(basePackages = {"com.jum.controller","com.jum.exception","com.jum.service"})   // 默认扫描包
//@EnableJpaRepositories("com.jum.dao") // 注册jpa包
//@EntityScan("com.jum.entity")  //添加实例类包扫描
//@EnableAutoConfiguration  // 根据应用所声明的依赖来对 Spring 框架进行自动配置
@MapperScan(basePackages = {"com.jum.db1.dao","com.jum.db2.dao"})   // mybatis
@SpringBootApplication     // 整合了Configuration EnableAutoConfiguration   ComponentScan
public class SpringbootdemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringbootdemoApplication.class, args);
	}
}

