package com.wallet.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.math.BigDecimal;

/**
 * Created by Darma Project on 2019/9/24.
 */
public class StringUtils {

    public static final  String SP_STRING_NAME="sp_string";

    public static boolean isEmpty(String str) {
        if (str == null || str.length()==0||"null".equals(str)||"NULL".equals(str)) {
            return true;
        }
        return false;
    }



    public static void saveStringToSp(Context context,String key, String value){
        if(context==null){
            return ;
        }
        SharedPreferences sp=context.getSharedPreferences(SP_STRING_NAME,Context.MODE_PRIVATE);
        sp.edit().putString(key,value).apply();

    }
    public static String getStringFromSp(Context context,String key, String value){
        if(context==null){
            return value;
        }
        SharedPreferences sp=context.getSharedPreferences(SP_STRING_NAME,Context.MODE_PRIVATE);

        return sp.getString(key,value);
    }

}
