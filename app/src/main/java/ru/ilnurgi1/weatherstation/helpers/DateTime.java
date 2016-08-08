package ru.ilnurgi1.weatherstation.helpers;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;


public class DateTime {

    private static String date_format = "dd-MM-yyy HH:mm:ss";
    private static Calendar calendar = Calendar.getInstance();
    private static TimeZone tz = calendar.getTimeZone();
    private static TimeZone tz_utc = TimeZone.getTimeZone("UTC");
    private static DateFormat dateFormat = new SimpleDateFormat(DateTime.date_format);

    public static Date currentDateTime(){
        return calendar.getTime();
    }

    public static String currentDateTimeString(){
        dateFormat.setTimeZone(tz);
        return dateFormat.format(currentDateTime());
    }

    public static Date getDateFromString(String date){
        dateFormat.setTimeZone(tz);
        Date date_parsed = null;

        try {
            date_parsed = dateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date_parsed;
    }

    public static String getUTCStringFromDate(Date date){
        dateFormat.setTimeZone(tz_utc);
        return dateFormat.format(date);
    }

    public static String getStringFromDate(Date date){
        dateFormat.setTimeZone(tz);
        return dateFormat.format(date);
    }
}
