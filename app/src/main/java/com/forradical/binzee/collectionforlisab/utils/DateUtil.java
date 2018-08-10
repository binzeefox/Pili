package com.forradical.binzee.collectionforlisab.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class DateUtil {

    /**
     * 日期转毫秒(yyyy-MM-dd)
     */
    public static long dateToMill(String yyyyMMdd){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        try {
            Date date = sdf.parse(yyyyMMdd);
            return date.getTime();
        } catch (ParseException e) {
            return 0;
        }
    }

    /**
     * 毫秒转日期
     */
    public static String millToDate(long mill){
        Date date = new Date(mill);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        return sdf.format(date);
    }
}
