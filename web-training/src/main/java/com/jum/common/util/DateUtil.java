package com.jum.common.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateUtil {

	public static final DateTimeFormatter FORMAT_YMDHMS_EASY = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
	public static final DateTimeFormatter FORMAT_YMDHMS = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	public static final DateTimeFormatter DEFAULT_DATEFORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
	public static final DateTimeFormatter FORMAT_YM = DateTimeFormatter.ofPattern("yyyy-MM");

//	public static SimpleDateFormat FORMAT_YMDHMS_EASY = new SimpleDateFormat(
//			"yyyyMMddHHmmss");
//
//	public static SimpleDateFormat FORMAT_YMDHMS = new SimpleDateFormat(
//			"yyyy-MM-dd HH:mm:ss");
//
//	public static final DateFormat DEFAULT_DATEFORMAT = new SimpleDateFormat(
//			"yyyy-MM-dd");
//	public static final DateFormat FORMAT_YM = new SimpleDateFormat(
//			"yyyy-MM");

	/**
	 * 时间转换为字符串yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String ymdhmsFormatEasy() {
		LocalDateTime now = LocalDateTime.now();
		return now.format(FORMAT_YMDHMS_EASY);
//		return FORMAT_YMDHMS_EASY.format(new Date());
	}

	/**
	 * 时间转换为字符串yyyyMMddHHmmss
	 *
	 * @return
	 */
	public static String ymdhmsFormatEasy(Date date) {
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		return localDateTime.format(FORMAT_YMDHMS_EASY);
//		return FORMAT_YMDHMS_EASY.format(date);
	}

	/**
	 * 时间转换为字符串yyyy-MM-dd HH:mm:ss
	 * 
	 * @param date
	 * @return
	 */
	public static String ymdhmsFormat(Date date) {
		if(date == null){
			return null;
		}
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		return localDateTime.format(FORMAT_YMDHMS);
//		return FORMAT_YMDHMS.format(date);
	}

	/**
	 * 时间转换为字符串yyyy-MM-dd
	 * 
	 * @param date
	 * @return
	 */
	public static String ymdFormat(Date date) {
		if(date == null){
			return null;
		}
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		return localDateTime.format(DEFAULT_DATEFORMAT);
//		return DEFAULT_DATEFORMAT.format(date);
	}

	/**
	 * 时间转换为字符串yyyy-MM
	 *
	 * @param date
	 * @return
	 */
	public static String ymFormat(Date date) {
		if(date == null){
			return null;
		}
		LocalDateTime localDateTime = dateToLocalDateTime(date);
		return localDateTime.format(FORMAT_YM);
	}

	public static Timestamp currentTime() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * 获取时间的整点时间
	 * @param date
	 * @return
	 */
	public static String getCurrHour(Date date){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		date = ca.getTime();
		return ymdhmsFormat(date);
//		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH-mm-ss");
//		return sdf.format(date);
	}

	public static Date getCurrHourDate(Date date){
		Calendar ca = Calendar.getInstance();
		ca.set(Calendar.MINUTE, 0);
		ca.set(Calendar.SECOND, 0);
		date = ca.getTime();
		return date;
	}


	/**
	 * @disc 根据日期获取相差的月份
	 * @param date
	 *            :给定的日期
	 * @param days
	 *            :要增加的天数
	 * @return 返回Date对象
	 */
	public static Date getDayAdd(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		return new Date(cal.getTime().getTime());
	}

	/**
	 * @disc 根据日期获取相差的月份
	 * @param hour
	 *            :给定的时间
	 * @return 返回Date对象
	 */
	public static Date getHourBefore(int hour) {
		Calendar cal = Calendar.getInstance();
		/* HOUR_OF_DAY 指示一天中的小时 */
		cal.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY) - hour);
		return cal.getTime();
	}

	/**
	 * @disc 根据日期获取相差的月份
	 * @param hour
	 *            :给定的时间
	 * @return 返回Date对象
	 */
	public static String getHourAdd(int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.HOUR_OF_DAY, hour);
		return ymdhmsFormatEasy(new Date(cal.getTime().getTime()));
	}

	public static Date getDateAfterDay(Date somedate, int day) {
		if (somedate == null)
			return null;
		Calendar cal = Calendar.getInstance();
		cal.setTime(somedate);
		cal.add(Calendar.DAY_OF_MONTH, day);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.SECOND, 0);
		cal.set(Calendar.MILLISECOND, 0);
		return new Date(cal.getTime().getTime());
	}

	public static Timestamp getDateAfterDay(int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		cal.add(Calendar.DAY_OF_MONTH, day);
		return new Timestamp(cal.getTime().getTime());
	}

	/**
	 * 获取两个日期字符串之间的日期集合
	 * @param startTime:String
	 * @param endTime:String
	 * @return list:yyyy-MM-dd
	 */
	public static List<String> getBetweenDate(String startTime, String endTime) throws ParseException {
		// 声明保存日期集合
		List<String> list = new ArrayList<String>();
		// 转化成日期类型
		LocalDate startLocalDate= LocalDate.parse(startTime, DEFAULT_DATEFORMAT);
		LocalDate endLocalDate= LocalDate.parse(endTime, DEFAULT_DATEFORMAT);
		Date startDate = DateUtil.localDateToDate(startLocalDate);
		Date endDate = DateUtil.localDateToDate(endLocalDate);
		//用Calendar 进行日期比较判断
		Calendar calendar = Calendar.getInstance();
		while (startDate.getTime()<=endDate.getTime()){
			// 把日期添加到集合
			list.add(ymdFormat(startDate));
			// 设置日期
			calendar.setTime(startDate);
			//把日期增加一天
			calendar.add(Calendar.DATE, 1);
			// 获取增加后的日期
			startDate=calendar.getTime();
		}
		return list;
	}

	/**
	 * 获取两个日期之间所有的月份集合
	 * @param startTime
	 * @param endTime
	 * @return：YYYY-MM
	 */
	public static List<String> getMonthBetweenDate(String startTime, String endTime){
		// 声明保存日期集合
		List<String> list = new ArrayList<String>();
		// 转化成日期类型
		LocalDate startLocalDate= LocalDate.parse(startTime, FORMAT_YM);
		LocalDate endLocalDate= LocalDate.parse(endTime, FORMAT_YM);
		Date startDate = DateUtil.localDateToDate(startLocalDate);
		Date endDate = DateUtil.localDateToDate(endLocalDate);

		//用Calendar 进行日期比较判断
		Calendar calendar = Calendar.getInstance();
		while (startDate.getTime()<=endDate.getTime()){
			// 把日期添加到集合
			list.add(ymFormat(startDate));
			// 设置日期
			calendar.setTime(startDate);
			//把日期增加一天
			calendar.add(Calendar.MONTH, 1);
			// 获取增加后的日期
			startDate=calendar.getTime();
		}
		return list;
	}


	/**
	   * 将LocalDate日期转化成Date
	   * @param localDate LocalDate对象
	   * @return Date对象
	   */
	public static Date localDateToDate(LocalDate localDate){
		if (localDate == null){
			return null;
		}
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = localDate.atStartOfDay(zoneId);
		Date date = Date.from(zonedDateTime.toInstant());
		return date;
	}

	/**
	 * 将LocalDateTime日期转化成Date
	 * @param localDateTime LocalDate对象
	 * @return Date对象
	 */
	public static Date localDateToDate(LocalDateTime localDateTime){
		if (localDateTime == null){
			return null;
		}
		ZoneId zoneId = ZoneId.systemDefault();
		ZonedDateTime zonedDateTime = localDateTime.atZone(zoneId);
		Date date = Date.from(zonedDateTime.toInstant());
		return date;
	}

    /**
     * 将Date转成LocalDate对象
     * @param date Date对象
     * @return LocalDate对象
     */
    public static LocalDate dateToLocalDate(Date date){
        if (date == null){
            return null;
        }
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = date.toInstant();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
     }

	/**
	 * 将Date转成LocalDateTime对象
	 * @param date Date对象
	 * @return LocalDate对象
	 */
	public static LocalDateTime dateToLocalDateTime(Date date){
		if (date == null){
			return null;
		}
		ZoneId zoneId = ZoneId.systemDefault();
		Instant instant = date.toInstant();
		LocalDateTime localDate = instant.atZone(zoneId).toLocalDateTime();
		return localDate;
	}

}
