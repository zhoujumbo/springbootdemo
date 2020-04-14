package com.java;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.junit.Test;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoField;
import java.time.temporal.ChronoUnit;

/**
 * 时间日期 [java8 API]
 * LocalDateTime`：`Date`有的我都有，`Date`没有的我也有
 * @ClassName DateTest
 * @Description TODO
 * @Author jb.zhou
 * @Date 2019/10/21
 * @Version 1.0
 */
public class DateTest {


    @Test
    public void test01(){

        // 获取当前年月日
        LocalDate localDate1 = LocalDate.now();
        System.out.println(localDate1);

        // 构造指定的年月日
        LocalDate localDate2 = LocalDate.of(2019,10,21);

        // 获取年月日
        int year = localDate1.getYear();
        int year1 = localDate1.get(ChronoField.YEAR);
        System.out.println(year + "  " + year1);

        Month month = localDate1.getMonth();
        int month1 = localDate1.get(ChronoField.MONTH_OF_YEAR);
        System.out.println(month + "  " + month1);

        int day1 =  localDate1.getDayOfMonth();
        int day2 = localDate1.get(ChronoField.DAY_OF_MONTH);
        System.out.println(day1 + "  " + day2);

        DayOfWeek dayOfWeek = localDate1.getDayOfWeek();
        int dayOfWeek1 = localDate1.get(ChronoField.DAY_OF_WEEK);
        System.out.println(dayOfWeek + "  " + dayOfWeek1);

    }

    @Test
    public void test02(){

        // LocalTime 只会获取几点几分几秒
        LocalTime localTime = LocalTime.of(13, 51, 10);
        LocalTime localTime1 = LocalTime.now();

        //获取小时
        int hour = localTime.getHour();
        int hour1 = localTime.get(ChronoField.HOUR_OF_DAY);
        //获取分
        int minute = localTime.getMinute();
        int minute1 = localTime.get(ChronoField.MINUTE_OF_HOUR);
        //获取秒
        int second = localTime.getSecond();
        int second1 = localTime.get(ChronoField.SECOND_OF_MINUTE);

    }

    @Test
    public void test03(){

        LocalDate localDate = LocalDate.now();
        LocalTime localTime = LocalTime.now();

        // LocalDateTime  获取年月日时分秒，等于LocalDate+LocalTime
        LocalDateTime localDateTime = LocalDateTime.now();
        LocalDateTime localDateTime1 = LocalDateTime.of(2019, Month.SEPTEMBER, 10, 14, 46, 56);
        LocalDateTime localDateTime2 = LocalDateTime.of(localDate, localTime);
        LocalDateTime localDateTime3 = localDate.atTime(localTime);
        LocalDateTime localDateTime4 = localTime.atDate(localDate);

        // 获取LocalDate
        LocalDate localDate2 = localDateTime.toLocalDate();

        // 获取LocalTime
        LocalTime localTime2 = localDateTime.toLocalTime();
    }

    @Test
    public void test04(){
        // Instant 获取秒数
        Instant instant = Instant.now();

        // 获取秒数
        long currentSecond = instant.getEpochSecond();

        // 获取毫秒数
        long currentMilli = instant.toEpochMilli();

        //System.currentTimeMillis()来得更为方便
    }

    // 修改LocalDate、LocalTime、LocalDateTime、Instant
    // LocalDate、LocalTime、LocalDateTime、Instant为不可变对象，修改这些对象对象会返回一个副本
    @Test
    public void test05(){
        // LocalDateTime 增加、减少年数、月数、天数等 以
        LocalDateTime localDateTime = LocalDateTime.of(2019, Month.SEPTEMBER, 10,
                14, 46, 56);
        //增加一年
        localDateTime = localDateTime.plusYears(1);
        localDateTime = localDateTime.plus(1, ChronoUnit.YEARS);
        //减少一个月
        localDateTime = localDateTime.minusMonths(1);
        localDateTime = localDateTime.minus(1, ChronoUnit.MONTHS);

        //  with
        //修改年为2019
        localDateTime = localDateTime.withYear(2020);
        //修改为2022
        localDateTime = localDateTime.with(ChronoField.YEAR, 2022);


        // 时间计算

        // 比如有些时候想知道这个月的最后一天是几号、下个周末是几号，通过提供的时间和日期API可以很快得到答案
        // 比如通过firstDayOfYear()返回了当前日期的第一天日期
        LocalDate localDate = LocalDate.now();
//        LocalDate localDate1 = localDate.with(firstDayOfYear());

        //  格式化时间
//        LocalDate localDate1 = LocalDate.of(2019, 9, 10);
        String s1 = localDate.format(DateTimeFormatter.BASIC_ISO_DATE);
        String s2 = localDate.format(DateTimeFormatter.ISO_LOCAL_DATE);
        // 自定义格式化
        // DateTimeFormatter默认提供了多种格式化方式，如果默认提供的不能满足要求，可以通过DateTimeFormatter的ofPattern方法创建自定义格式化方式
        DateTimeFormatter dateTimeFormatter =   DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String s3 = localDate.format(dateTimeFormatter);

        // 解析时间
        LocalDate localDate2 = LocalDate.parse("20190910", DateTimeFormatter.BASIC_ISO_DATE);
        LocalDate localDate3 = LocalDate.parse("2019-09-10", DateTimeFormatter.ISO_LOCAL_DATE);
//        和SimpleDateFormat相比，DateTimeFormatter是线程安全的
    }

    @Test
    public void test06(){

//        SpringBoot中应用LocalDateTime
//
//        将LocalDateTime字段以时间戳的方式返回给前端 添加日期转化类
//
//        public class LocalDateTimeConverter extends JsonSerializer<LocalDateTime> {
//
//            @Override
//            public void serialize(LocalDateTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
//                gen.writeNumber(value.toInstant(ZoneOffset.of("+8")).toEpochMilli());
//            }
//        }
//        并在
//
//                LocalDateTime
//        字段上添加
//
//        @JsonSerialize(using = LocalDateTimeConverter.class)
//        注解，如下：
//
//        @JsonSerialize(using = LocalDateTimeConverter.class)
//        protected LocalDateTime gmtModified;
//        将LocalDateTime字段以指定格式化日期的方式返回给前端 在
//
//        LocalDateTime
//                字段上添加
//
//        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
//        注解即可，如下：
//
//        @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd HH:mm:ss")
//        protected LocalDateTime gmtModified;
//        对前端传入的日期进行格式化 在
//
//        LocalDateTime
//                字段上添加
//
//        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//        注解即可，如下：
//
//        @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//        protected LocalDateTime gmtModified;
    }

}
