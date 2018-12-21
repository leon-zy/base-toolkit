package com.insaic.toolkit.utils;

/**
 * DateUtil
 * Created by leon_zy on 2018/7/12
 */

import com.insaic.base.utils.Week;
import com.insaic.toolkit.enums.DateStyle;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DateUtils {
    private static final ThreadLocal<SimpleDateFormat> threadLocal = new ThreadLocal();
    private static final Object object = new Object();

    public DateUtils() {
    }

    private static SimpleDateFormat getDateFormat(String pattern) throws RuntimeException {
        SimpleDateFormat dateFormat = (SimpleDateFormat)threadLocal.get();
        if (dateFormat == null) {
            Object var2 = object;
            synchronized(object) {
                if (dateFormat == null) {
                    dateFormat = new SimpleDateFormat(pattern);
                    dateFormat.setLenient(false);
                    threadLocal.set(dateFormat);
                }
            }
        }

        dateFormat.applyPattern(pattern);
        return dateFormat;
    }

    private static int getInteger(Date date, int dateType) {
        int num = 0;
        Calendar calendar = Calendar.getInstance();
        if (date != null) {
            calendar.setTime(date);
            num = calendar.get(dateType);
        }

        return num;
    }

    private static String addInteger(String date, int dateType, int amount) {
        String dateString = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = stringToDate(date, dateStyle);
            myDate = addInteger(myDate, dateType, amount);
            dateString = dateToString(myDate, dateStyle);
        }

        return dateString;
    }

    private static Date addInteger(Date date, int dateType, int amount) {
        Date myDate = null;
        if (date != null) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            calendar.add(dateType, amount);
            myDate = calendar.getTime();
        }

        return myDate;
    }

    private static Date getAccurateDate(List<Long> timestamps) {
        Date date = null;
        long timestamp = 0L;
        Map<Long, long[]> map = new HashMap();
        List<Long> absoluteValues = new ArrayList();
        if (timestamps != null && timestamps.size() > 0) {
            if (timestamps.size() > 1) {
                for(int i = 0; i < timestamps.size(); ++i) {
                    for(int j = i + 1; j < timestamps.size(); ++j) {
                        long absoluteValue = Math.abs(((Long)timestamps.get(i)).longValue() - ((Long)timestamps.get(j)).longValue());
                        absoluteValues.add(absoluteValue);
                        long[] timestampTmp = new long[]{((Long)timestamps.get(i)).longValue(), ((Long)timestamps.get(j)).longValue()};
                        map.put(absoluteValue, timestampTmp);
                    }
                }

                long minAbsoluteValue = -1L;
                if (!absoluteValues.isEmpty()) {
                    minAbsoluteValue = ((Long)absoluteValues.get(0)).longValue();

                    for(int i = 1; i < absoluteValues.size(); ++i) {
                        if (minAbsoluteValue > ((Long)absoluteValues.get(i)).longValue()) {
                            minAbsoluteValue = ((Long)absoluteValues.get(i)).longValue();
                        }
                    }
                }

                if (minAbsoluteValue != -1L) {
                    long[] timestampsLastTmp = (long[])map.get(minAbsoluteValue);
                    long dateOne = timestampsLastTmp[0];
                    long dateTwo = timestampsLastTmp[1];
                    if (absoluteValues.size() > 1) {
                        timestamp = Math.abs(dateOne) > Math.abs(dateTwo) ? dateOne : dateTwo;
                    }
                }
            } else {
                timestamp = ((Long)timestamps.get(0)).longValue();
            }
        }

        if (timestamp != 0L) {
            date = new Date(timestamp);
        }

        return date;
    }

    public static boolean isDate(String date) {
        boolean isDate = false;
        if (date != null && getDateStyle(date) != null) {
            isDate = true;
        }

        return isDate;
    }

    public static DateStyle getDateStyle(String date) {
        DateStyle dateStyle = null;
        Map<Long, DateStyle> map = new HashMap();
        List<Long> timestamps = new ArrayList();
        DateStyle[] var4 = DateStyle.values();
        int var5 = var4.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            DateStyle style = var4[var6];
            if (!style.isShowOnly()) {
                Date dateTmp = null;
                if (date != null) {
                    try {
                        ParsePosition pos = new ParsePosition(0);
                        dateTmp = getDateFormat(style.getValue()).parse(date, pos);
                        if (pos.getIndex() != date.length()) {
                            dateTmp = null;
                        }
                    } catch (Exception var10) {
                        ;
                    }
                }

                if (dateTmp != null) {
                    timestamps.add(dateTmp.getTime());
                    map.put(dateTmp.getTime(), style);
                }
            }
        }

        Date accurateDate = getAccurateDate(timestamps);
        if (accurateDate != null) {
            dateStyle = (DateStyle)map.get(accurateDate.getTime());
        }

        return dateStyle;
    }

    /** @deprecated */
    @Deprecated
    public static Date StringToDate(String date) {
        return stringToDate(date);
    }

    /** @deprecated */
    @Deprecated
    public static Date StringToDate(String date, String pattern) {
        return stringToDate(date, pattern);
    }

    /** @deprecated */
    @Deprecated
    public static Date StringToDate(String date, DateStyle dateStyle) {
        return stringToDate(date, dateStyle);
    }

    public static Date stringToDate(String date) {
        DateStyle dateStyle = getDateStyle(date);
        return stringToDate(date, dateStyle);
    }

    public static Date stringToDate(String date, String pattern) {
        Date myDate = null;
        if (date != null) {
            try {
                myDate = getDateFormat(pattern).parse(date);
            } catch (Exception var4) {
                ;
            }
        }

        return myDate;
    }

    public static Date stringToDate(String date, DateStyle dateStyle) {
        Date myDate = null;
        if (dateStyle != null) {
            myDate = stringToDate(date, dateStyle.getValue());
        }

        return myDate;
    }

    /** @deprecated */
    @Deprecated
    public static String DateToString(Date date, String pattern) {
        return dateToString(date, pattern);
    }

    /** @deprecated */
    @Deprecated
    public static String DateToString(Date date, DateStyle dateStyle) {
        return dateToString(date, dateStyle);
    }

    public static String dateToString(Date date, String pattern) {
        String dateString = null;
        if (date != null) {
            try {
                dateString = getDateFormat(pattern).format(date);
            } catch (Exception var4) {
                ;
            }
        }

        return dateString;
    }

    public static String dateToString(Date date, DateStyle dateStyle) {
        String dateString = null;
        if (dateStyle != null) {
            dateString = dateToString(date, dateStyle.getValue());
        }

        return dateString;
    }

    /** @deprecated */
    @Deprecated
    public static String StringToString(String date, String newPattern) {
        return stringToString(date, newPattern);
    }

    /** @deprecated */
    @Deprecated
    public static String StringToString(String date, DateStyle newDateStyle) {
        return stringToString(date, newDateStyle);
    }

    /** @deprecated */
    @Deprecated
    public static String StringToString(String date, String oldPattern, String newPattern) {
        return stringToString(date, oldPattern, newPattern);
    }

    /** @deprecated */
    @Deprecated
    public static String StringToString(String date, DateStyle oldDteStyle, String newParttern) {
        return stringToString(date, oldDteStyle, newParttern);
    }

    /** @deprecated */
    @Deprecated
    public static String StringToString(String date, String oldPattern, DateStyle newDateStyle) {
        return stringToString(date, oldPattern, newDateStyle);
    }

    /** @deprecated */
    @Deprecated
    public static String StringToString(String date, DateStyle oldDteStyle, DateStyle newDateStyle) {
        return stringToString(date, oldDteStyle, newDateStyle);
    }

    public static String stringToString(String date, String newPattern) {
        DateStyle oldDateStyle = getDateStyle(date);
        return stringToString(date, oldDateStyle, newPattern);
    }

    public static String stringToString(String date, DateStyle newDateStyle) {
        DateStyle oldDateStyle = getDateStyle(date);
        return stringToString(date, oldDateStyle, newDateStyle);
    }

    public static String stringToString(String date, String oldPattern, String newPattern) {
        return dateToString(stringToDate(date, oldPattern), newPattern);
    }

    public static String stringToString(String date, DateStyle oldDteStyle, String newParttern) {
        String dateString = null;
        if (oldDteStyle != null) {
            dateString = stringToString(date, oldDteStyle.getValue(), newParttern);
        }

        return dateString;
    }

    public static String stringToString(String date, String olddPattern, DateStyle newDateStyle) {
        String dateString = null;
        if (newDateStyle != null) {
            dateString = stringToString(date, olddPattern, newDateStyle.getValue());
        }

        return dateString;
    }

    public static String stringToString(String date, DateStyle olddDteStyle, DateStyle newDateStyle) {
        String dateString = null;
        if (olddDteStyle != null && newDateStyle != null) {
            dateString = stringToString(date, olddDteStyle.getValue(), newDateStyle.getValue());
        }

        return dateString;
    }

    public static String addYear(String date, int yearAmount) {
        return addInteger((String)date, 1, yearAmount);
    }

    public static Date addYear(Date date, int yearAmount) {
        return addInteger((Date)date, 1, yearAmount);
    }

    public static String addMonth(String date, int monthAmount) {
        return addInteger((String)date, 2, monthAmount);
    }

    public static Date addMonth(Date date, int monthAmount) {
        return addInteger((Date)date, 2, monthAmount);
    }

    public static String addDay(String date, int dayAmount) {
        return addInteger((String)date, 5, dayAmount);
    }

    public static Date addDay(Date date, int dayAmount) {
        return addInteger((Date)date, 5, dayAmount);
    }

    public static String addHour(String date, int hourAmount) {
        return addInteger((String)date, 11, hourAmount);
    }

    public static Date addHour(Date date, int hourAmount) {
        return addInteger((Date)date, 11, hourAmount);
    }

    public static String addMinute(String date, int minuteAmount) {
        return addInteger((String)date, 12, minuteAmount);
    }

    public static Date addMinute(Date date, int minuteAmount) {
        return addInteger((Date)date, 12, minuteAmount);
    }

    public static String addSecond(String date, int secondAmount) {
        return addInteger((String)date, 13, secondAmount);
    }

    public static Date addSecond(Date date, int secondAmount) {
        return addInteger((Date)date, 13, secondAmount);
    }

    public static int getYear(String date) {
        return getYear(stringToDate(date));
    }

    public static int getYear(Date date) {
        return getInteger(date, 1);
    }

    public static int getMonth(String date) {
        return getMonth(stringToDate(date));
    }

    public static int getMonth(Date date) {
        return getInteger(date, 2) + 1;
    }

    public static int getDay(String date) {
        return getDay(stringToDate(date));
    }

    public static int getDay(Date date) {
        return getInteger(date, 5);
    }

    public static int getHour(String date) {
        return getHour(stringToDate(date));
    }

    public static int getHour(Date date) {
        return getInteger(date, 11);
    }

    public static int getMinute(String date) {
        return getMinute(stringToDate(date));
    }

    public static int getMinute(Date date) {
        return getInteger(date, 12);
    }

    public static int getSecond(String date) {
        return getSecond(stringToDate(date));
    }

    public static int getSecond(Date date) {
        return getInteger(date, 13);
    }

    public static String getDate(String date) {
        return stringToString(date, DateStyle.YYYY_MM_DD);
    }

    public static String getDate(Date date) {
        return dateToString(date, DateStyle.YYYY_MM_DD);
    }

    public static String getTime(String date) {
        return stringToString(date, DateStyle.HH_MM_SS);
    }

    public static String getTime(Date date) {
        return dateToString(date, DateStyle.HH_MM_SS);
    }

    public static Week getWeek(String date) {
        Week week = null;
        DateStyle dateStyle = getDateStyle(date);
        if (dateStyle != null) {
            Date myDate = stringToDate(date, dateStyle);
            week = getWeek(myDate);
        }

        return week;
    }

    public static Week getWeek(Date date) {
        Week week = null;
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int weekNumber = calendar.get(7) - 1;
        switch(weekNumber) {
            case 0:
                week = Week.SUNDAY;
                break;
            case 1:
                week = Week.MONDAY;
                break;
            case 2:
                week = Week.TUESDAY;
                break;
            case 3:
                week = Week.WEDNESDAY;
                break;
            case 4:
                week = Week.THURSDAY;
                break;
            case 5:
                week = Week.FRIDAY;
                break;
            case 6:
                week = Week.SATURDAY;
        }

        return week;
    }

    public static int getIntervalDays(String beginDate, String endDate) {
        return getIntervalDays(stringToDate(beginDate), stringToDate(endDate));
    }

    public static int getIntervalDays(Date beginDate, Date endDate) {
        int num = -1;
        Date dateTmp = stringToDate(getDate(endDate), DateStyle.YYYY_MM_DD);
        Date otherDateTmp = stringToDate(getDate(beginDate), DateStyle.YYYY_MM_DD);
        if (dateTmp != null && otherDateTmp != null) {
            long time = dateTmp.getTime() - otherDateTmp.getTime();
            num = (int)(time / 86400000L);
        }

        return num;
    }

    public static Long getIntervalYears(Date beginDate, Date endDate) {
        return getIntervalYears(toCalendar(beginDate), toCalendar(endDate));
    }

    public static Long getIntervalMonths(Date beginDate, Date endDate) {
        return getIntervalMonths(toCalendar(beginDate), toCalendar(endDate));
    }

    public static Long getIntervalYears(Calendar calendar1, Calendar calendar2) {
        Long y = (long)(calendar2.get(1) - calendar1.get(1));
        return calendar2.get(2) <= calendar1.get(2) && (calendar2.get(2) != calendar1.get(2) || calendar2.get(5) < calendar1.get(5)) ? y.longValue() - 1L : y.longValue();
    }

    public static Long getIntervalMonths(Calendar calendar1, Calendar calendar2) {
        Long m = (long)((calendar2.get(1) - calendar1.get(1)) * 12 + (calendar2.get(2) - calendar1.get(2)));
        return calendar2.get(5) >= calendar1.get(5) ? m.longValue() : m.longValue() - 1L;
    }

    public static Calendar toCalendar(Date date) {
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        return c;
    }
}