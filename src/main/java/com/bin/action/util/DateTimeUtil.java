package com.bin.action.util;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.text.DateFormat;
import java.util.Date;

/**
 * Created by zhangbin on 18/1/6.
 */
public class DateTimeUtil {

    private static final String formater = "yyyy-MM-dd HH:mm:ss";

    public static Date str2Date(String time){
        return str2Date(time,formater);
    }

    public static String date2Str(Date date){
        return date2Str(date,formater);
    }

    public static Date str2Date(String time,String formater){
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern(formater);
        DateTime dateTime = dateTimeFormatter.parseDateTime(time);
        return dateTime.toDate();
    }

    public static String date2Str(Date date,String formater){
        DateTime dateTime = new DateTime(date);
        String time = dateTime.toString(formater);
        return time;
    }
}
