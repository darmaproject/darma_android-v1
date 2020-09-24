package com.wallet.utils;

import android.content.Context;
import com.wallet.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by Darma Project on 2019/9/25.
 */
public class DateUtils {

    public static String dateToString(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return simpleDateFormat.format(date);
    }
    public static String dateToDay(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(date);
    }
    public static String longToString(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return simpleDateFormat.format(new Date(time));
    }

    public static String scendToString(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd  HH:mm:ss");
        return simpleDateFormat.format(new Date(time*1000));
    }

    public static String longToDay(long time){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return simpleDateFormat.format(new Date(time));
    }
    public static String dateToMonth(Date date){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
        return simpleDateFormat.format(date);
    }


}
