package com.utils;

import org.springframework.data.mongodb.core.aggregation.ArrayOperators;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {

    /**
     * 日期转字符串
     * @param date
     * @param format
     * @return
     */
    public static String dateToStr(Date date, String format) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 字符转日期
     * @param date
     * @param format
     * @return
     */
    public static Date strToDate(String date, String format) {
        ParsePosition pos = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.parse(date, pos);
    }

    /**
     * 获取下一个日期
     * @param date 当前日期
     * @param dateType 日期类型：1-秒，2-分，3-时，4-天，5-月，6-年
     * @param increment 日期增量（为负数时即为扣减相应时间）
     * @return
     */
    public static Date getNextDate(Date date, int dateType, int increment) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);//设置起时间
        switch (dateType) {
            case 1:
                cal.add(Calendar.SECOND, increment);
                break;
            case 2:
                cal.add(Calendar.MINUTE, increment);
                break;
            case 3:
                cal.add(Calendar.HOUR, increment);
                break;
            case 4:
                cal.add(Calendar.DAY_OF_YEAR, increment);
                break;
            case 5:
                cal.add(Calendar.MONTH, increment);
                break;
            case 6:
                cal.add(Calendar.YEAR, increment);
                break;
            default:
                cal.setTime(null);
                break;
        }
        return cal.getTime();
    }

    /**
     * 获取日期间隔
     * @param startDate
     * @param endDate
     * @param dateType 日期类型：1-秒，2-分，3-时，4-天，5-月，6-年
     * @return
     */
    public static Integer getDiffDate(Date startDate, Date endDate, int dateType) {
        int diffDate = 0;
        Calendar startCal = Calendar.getInstance();
        Calendar endCal = Calendar.getInstance();
        startCal.setTime(startDate);//设置起始时间
        endCal.setTime(endDate);//设置截止时间
        switch (dateType) {
            case 1:
                diffDate = endCal.get(Calendar.SECOND) - startCal.get(Calendar.SECOND);
                break;
            case 2:
                diffDate = endCal.get(Calendar.MINUTE) - startCal.get(Calendar.MINUTE);
                break;
            case 3:
                diffDate = endCal.get(Calendar.HOUR) - startCal.get(Calendar.HOUR);
                break;
            case 4:
                diffDate = endCal.get(Calendar.DAY_OF_YEAR) - startCal.get(Calendar.DAY_OF_YEAR);
                break;
            case 5:
                diffDate = endCal.get(Calendar.MONTH) - startCal.get(Calendar.MONTH);
                break;
            case 6:
                diffDate = endCal.get(Calendar.YEAR) - startCal.get(Calendar.YEAR);
                break;
            default:
                break;
        }
        return diffDate;
    }
}
