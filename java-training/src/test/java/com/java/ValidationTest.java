package com.java;

import com.fortunetree.basic.support.commons.business.jackson.JacksonUtil;
import com.fortunetree.basic.support.commons.business.validator.DateValidator;
import com.fortunetree.basic.support.commons.business.validator.ValidationUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.IOException;
import java.util.Date;

public class ValidationTest {

    @Test
    public void test1() throws IOException {
        Account account = new Account();
        account.setAlias("kalakala");
        account.setUserName("wokalakala");
        account.setPassWord("密码");
        System.out.println(JacksonUtil.toJson(account));
    }


    @Test
    public void test2() throws IOException {
        Account account = new Account();
        account.setAlias("kalakala");
        account.setUserName("wokalakalaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
        account.setPassWord("密码");
        ValidationUtil.ValidResult validResult = ValidationUtil.validateBean(account);
        if(validResult.hasErrors()){
            String errors = validResult.getErrorsStr();
            System.out.println(errors);
        }

        ValidationUtil.ValidResult validResult2 = ValidationUtil.validateBean(account, Group1.class);
        if(validResult2.hasErrors()){
            String errors = validResult2.getErrorsStr();
            System.out.println(errors);
        }
    }


}

// 注解	释义
// @Null	必须为null
// @NotNull	不能为null
// @AssertTrue	必须为true
// @AssertFalse	必须为false
// @Min	必须为数字，其值大于或等于指定的最小值
// @Max	必须为数字，其值小于或等于指定的最大值
// @DecimalMin	必须为数字，其值大于或等于指定的最小值
// @DecimalMax	必须为数字，其值小于或等于指定的最大值
// @Size	集合的长度
// @Digits	必须为数字，其值必须再可接受的范围内
// @Past	必须是过去的日期
// @Future	必须是将来的日期
// @Pattern	必须符合正则表达式
// @Email	必须是邮箱格式
// @Length	长度范围
// @NotEmpty	不能为null，长度大于0
// @Range	元素的大小范围
// @NotBlank	不能为null，字符串长度大于0(限字符串)


/**
 * PS:
 * AllArgsConstructor 全参构造函数
 * NoArgsConstructor 无参构造函数
 *
 * 建议自定义 错误信息 message = "id不能为空"
 * 多业务环境使用分组校验  分组必须用接口限定 groups = {Group1.class}
 * 自定义校验   @DateValidator(dateFormat = "yyyy-MM-dd",groups = {Group1.class})
 *
 *
 */

@Data
@AllArgsConstructor
@NoArgsConstructor
class Account {

    @NotNull(message = "id不能为空", groups = {Group1.class})
    private String id;

    @NotNull
//    @Length(max = 20,message = "长度必须小于20")
    @Length(max = 10,message = "长度必须小于10", groups = {Group1.class})
    private String userName;

    @NotNull
    @Pattern(regexp = "[A-Z][a-z][0-9]", message = "密码")
    private String passWord;

    @DateTimeFormat(pattern = "yyy-MM-dd")
    private Date createTime;


    @DateValidator(dateFormat = "yyyy-MM-dd",groups = {Group1.class})
    private String editTIME;

    private String alias;

    @Max(10)
    @Min(1)
    private Integer level;

    private Integer vip;

    @AssertTrue
    private Boolean isSpecial;
}

interface Group1{}