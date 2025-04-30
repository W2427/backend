package com.ose.util;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 日期工具。
 */
public class DateUtils {

    private static final DateTimeFormatter ISO_DATE_TIME_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
    private static final String REGEXP_DATE_YYYYMMDD = "^(\\d{4})(\\d{2})(\\d{2})$";
    private static final Pattern PATTERN_DATE_YYYYMMDD = Pattern.compile(REGEXP_DATE_YYYYMMDD);
    private static final String REGEXP_DATE_YYYY_M_D = "^(\\d{4})[\\-/](\\d{1,2})[\\-/](\\d{1,2})$";
    private static final Pattern PATTERN_DATE_YYYY_M_D = Pattern.compile(REGEXP_DATE_YYYY_M_D);

    /**
     * 将日期格式化为 ISO 字符串。
     *
     * @param date 日期
     * @return 日期 ISO 字符串
     */
    public static String toISOString(Date date) {
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
    }

    /**
     * 将 ISO 日期字符串转为日期对象。
     *
     * @param isoString ISO 日期字符串
     * @return 日期对象
     */
    public static Date fromISOString(String isoString) {
        return Date.from(
            Instant.from(
                OffsetDateTime.parse(isoString, ISO_DATE_TIME_FORMATTER)
            )
        );
    }

    /**
     * 将日期格式化为字符串。
     *
     * @param date 日期
     * @return 日期字符串
     */
    public static String toNumbers(Date date) {
        DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        df.setTimeZone(TimeZone.getTimeZone("UTC"));
        return df.format(date);
    }

    /**
     * 字符串转换为java.util.Date<br>
     * 支持格式为 yyyy.MM.dd G 'at' hh:mm:ss z 如 '2017-12-12 AD at 22:10:59 PSD'<br>
     * yy/MM/dd HH:mm:ss 如 '2017/12/12 17:55:00'<br>
     * yy/MM/dd HH:mm:ss pm 如 '2017/12/12 17:55:00 pm'<br>
     * yy-MM-dd HH:mm:ss 如 '2017-12-12 17:55:00' <br>
     * yy-MM-dd HH:mm:ss am 如 '2017-12-12 17:55:00 am' <br>
     *
     * @param time String 字符串<br>
     * @return Date 日期<br>
     */
    public static Date stringToDate(String time) {
        SimpleDateFormat formatter;
        int tempPos = time.indexOf("AD");
        time = time.trim();
        formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
        if (tempPos > -1) {
            time = time.substring(0, tempPos) + "公元" + time.substring(tempPos + "AD".length());//china
            formatter = new SimpleDateFormat("yyyy.MM.dd G 'at' hh:mm:ss z");
        }
        tempPos = time.indexOf("-");
        if (tempPos > -1 && (time.indexOf(" ") < 0)) {
            formatter = new SimpleDateFormat("yyyyMMddHHmmssZ");
        } else if ((time.indexOf("/") > -1) && (time.indexOf(" ") > -1)) {
            formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        } else if ((time.indexOf("-") > -1) && (time.indexOf(" ") > -1)) {
            String[] tmpArr = time.split(":");
            if(tmpArr.length==2) {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            } else {
                formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            }
        } else if ((time.indexOf("/") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1)) {
            formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
        } else if ((time.indexOf("-") > -1) && (time.indexOf("am") > -1) || (time.indexOf("pm") > -1)) {
            formatter = new SimpleDateFormat("yyyy-MM-dd KK:mm:ss a");
        } else if ((time.indexOf("/") > -1) && (time.indexOf(" ") == -1)) {
            formatter = new SimpleDateFormat("yyyy/MM/dd");
        } else if ((time.indexOf("-") > -1) && (time.indexOf(" ") == -1)) {
            formatter = new SimpleDateFormat("yyyy-MM-dd");
        }
        ParsePosition pos = new ParsePosition(0);
        Date ctime = formatter.parse(time, pos);
        return ctime;
    }

    /**
     * 取得下一周。
     *
     * @param yearWeek yyyyWW
     * @return 给定周的下一周
     */
    public static int getNextYearWeek(int yearWeek) {

        int year = yearWeek / 100;
        int week = yearWeek % 100;

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.WEEK_OF_YEAR, week + 1);

        if (calendar.get(Calendar.MONTH) == Calendar.DECEMBER
            && calendar.get(Calendar.WEEK_OF_YEAR) == 1) {
            year += 1;
        }

        return year * 100 + calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 解析日期字符串值。
     *
     * @param date 日期
     * @return Calendar 实例
     */
    public static Calendar toDate(String date) {

        if (date == null) {
            return null;
        }

        Matcher matcher;

        if (date.matches(REGEXP_DATE_YYYYMMDD)) {
            matcher = PATTERN_DATE_YYYYMMDD.matcher(date);
        } else if (date.matches(REGEXP_DATE_YYYY_M_D)) {
            matcher = PATTERN_DATE_YYYY_M_D.matcher(date);
        } else {
            return null;
        }

        if (!matcher.matches()) {
            return null;
        }

        int year = Integer.parseInt(matcher.group(1));
        int month = Integer.parseInt(matcher.group(2));
        int dayOfMonth = Integer.parseInt(matcher.group(3));

        Calendar calendar = Calendar.getInstance();
        calendar.clear();
        calendar.set(year, month - 1, dayOfMonth);

        return calendar;
    }

    /**
     * 获取前一天时间
     *
     * @return
     */
    public static Date getBeforeDate() {
        Date dNow = new Date(); //当前时间
        Date dBefore = new Date();
        Calendar calendar = Calendar.getInstance(); //得到日历
        calendar.setTime(dNow);//把当前时间赋给日历
        calendar.add(Calendar.DAY_OF_MONTH, -1); //设置为前一天
        dBefore = calendar.getTime(); //得到前一天的时间
        return dBefore;
    }

    /**
     * 获取当前时间在一年中的第几周
     *
     * @param date
     * @return
     */
    public static Integer weekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.WEEK_OF_YEAR);
    }

    /**
     * 获取当前时间在一年中的第几周(按周一到周日算)
     *
     * @param date
     * @return
     */
    public static Integer getFixedWeekOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setFirstDayOfWeek(Calendar.MONDAY);
        calendar.setMinimalDaysInFirstWeek(1);
        calendar.setTime(date);
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if (week == 1 && date.getMonth() == 11) {
            // 克隆日历对象并回退7天，以获取上一年的最后一周
            Calendar tempCalendar = (Calendar) calendar.clone();
            tempCalendar.add(Calendar.DAY_OF_YEAR, -7);
            week = tempCalendar.get(Calendar.WEEK_OF_YEAR) + 1;
        }
        return week;
    }

    /**
     * 获取当前时间在一年中的第几天
     *
     * @param date
     * @return
     */
    public static Integer dayOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.DAY_OF_YEAR);
    }

    /**
     * 获取当前时间在一年中的第几月
     *
     * @param date
     * @return
     */
    public static Integer monthOfYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.MONTH);
    }

    /**
     * 获取当前时间在一年中的第几年
     *
     * @param date
     * @return
     */
    public static Integer getYear(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar.get(Calendar.YEAR);
    }

    /**
     * 获取开始时间
     *
     * @param time
     * @return
     */
    public static Date getStartOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    /**
     * 获取结束时间
     *
     * @param time
     * @return
     */
    public static Date getEndOfDay(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    /**
     * 获取开始时间（2点整）
     *
     * @param time
     * @return
     */
    public static Date getFixedTime(Date time) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(time);
        calendar.set(Calendar.HOUR_OF_DAY, 2);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static Date addDate(Date date,long day)  {
        long time = date.getTime(); // 得到指定日期的毫秒数
        day = day*24*60*60*1000; // 要加上的天数转换成毫秒数
        time+=day; // 相加得到新的毫秒数
        return new Date(time); // 将毫秒数转换成日期
    }

    public static void main(String[] args) {
        Date finishedDate = DateUtils.stringToDate("2000-01-10 00:00:00");
        Date oldDate = DateUtils.stringToDate("2000-01-01 00:00:00");
        if(finishedDate.compareTo(oldDate) > 0) {
            finishedDate = new Date();
        }
    }
}
