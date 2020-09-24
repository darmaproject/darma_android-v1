package com.darma.wallet.utils;

import android.content.Context;

import com.darma.wallet.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Darma Project on 2019/11/5.
 */
public class DateTimeUtils {


    public static String SIMPLE_DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ssZ";


    public static String SHOW_DATE_FORMAT = "yyyy/MM/dd HH:mm:ss";

    public static String COUNT_DOWN_DATE_FORMAT = "mm'ss''";

    private static SimpleDateFormat getSimpleDateFormat() {
        return new SimpleDateFormat(SIMPLE_DATE_FORMAT);
    }

    private static SimpleDateFormat getShowDateFormat() {
        return new SimpleDateFormat(SHOW_DATE_FORMAT);
    }

    /**
     * @param timeStr 2017-07-01T08:11:27Z
     * @return 2017/07/01 08:11
     */
    public static String stampToDateTime(String timeStr) {

        try {
            Date date = getSimpleDateFormat().parse(timeStr);
            return getShowDateFormat().format(date);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }


    /**
     * @param time mills
     * @return 29'12'' count down
     */
    public static String getCountDownTime(long time) {

        long second=time/1000;

        long mining=second/60;

        return mining+"'"+second%60;
    }
    public static String getMonthAndYear(Context context, Date date) {
        if (date == null) {
            return "";
        }
        return getMonth(context, date) + ",  " + getYear(date);

    }

    public static String getMonth(Context context, Date date) {
        if (date == null) {
            return "";
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int m = c.get(Calendar.MONTH);
        return context.getResources().getStringArray(R.array.str_array_months)[m];
    }

    public static int getYear(Date date) {
        if (date == null) {
            return 0;
        }
        Calendar c = Calendar.getInstance();
        c.setTime(date);
        int y = c.get(Calendar.YEAR);


        return y;
    }
}
