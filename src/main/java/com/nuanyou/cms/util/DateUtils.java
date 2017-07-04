package com.nuanyou.cms.util;

import org.apache.commons.lang3.tuple.Pair;
import org.joda.time.DateTime;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {

    private static final ThreadLocal<DateFormat> Standard = new ThreadLocal<DateFormat>() {
        @Override
        protected DateFormat initialValue() {
            return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        }
    };

    public static Date parse(String str) {
        return parse(str, Standard.get());
    }

    public static Date parse(String str, DateFormat format) {
        try {
            return format.parse(str);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public static String format(Date date) {
        return format(date, Standard.get());
    }

    public static String format(Date date, DateFormat format) {
        if (date == null)
            return null;
        return format.format(date);
    }

    /**
     * 计算年龄
     */
    public static Integer getAge(Date birthday) {
        Calendar cal = Calendar.getInstance();

        if (cal.before(birthday)) {
            return null;
        }

        int yearNow = cal.get(Calendar.YEAR);
        int monthNow = cal.get(Calendar.MONTH) + 1;
        int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);

        cal.setTime(birthday);
        int yearBirth = cal.get(Calendar.YEAR);
        int monthBirth = cal.get(Calendar.MONTH);
        int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);

        int age = yearNow - yearBirth;

        if (monthNow <= monthBirth) {
            if (monthNow == monthBirth) {
                if (dayOfMonthNow < dayOfMonthBirth) {
                    age--;
                }
            } else {
                age--;
            }
        }
        return age;
    }
    static {

    }

    public static Date newDate() {
        return new Date();
    }

    public static Date getTodayDate(){
        Calendar c = Calendar.getInstance();
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);
        return c.getTime();
    }
    public static Date getTomorrowDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DAY_OF_MONTH,1);
        c.set(Calendar.HOUR_OF_DAY,0);
        c.set(Calendar.MINUTE,0);
        c.set(Calendar.SECOND,0);;
        return c.getTime();
    }

    /**
     * 获取当天的起止时间
     *
     * @return
     * @startDate 起始时间，例：2017-6-29 00:00:00
     * @endDate 结束时间，例：2017-6-29 23:59:59
     */
    public static Pair<Date, Date> getDayStartEndTime(Date date) {
        DateTime dateTime = new DateTime(date);
        Date startDate = dateTime.minusDays(1).withTimeAtStartOfDay().toDate();
        Date endDate = dateTime.withTimeAtStartOfDay().minusSeconds(1).toDate();
        return Pair.of(startDate, endDate);
    }
}