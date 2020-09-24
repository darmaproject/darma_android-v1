package com.darma.wallet.utils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.darma.wallet.ui.activity.UnlockActivity;
import com.wallet.WalletManager;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;


public class SettingManager {

    public static final String TAG = "SettingManager";
    public static final String SP_SETTING = "sp_setting";


    public static final String IS_FINGERPRINT_OPEN = "is_fingerprint_open";

    public static final String IS_GESTURE_OPEN = "is_gesture_open";

    public static final String APP_LAST_TIME= "app_last_time";



    public static boolean isFingerprintOpen(Context context){

        return (boolean) get(context,IS_FINGERPRINT_OPEN,false);
    }

    public static void closeFingerprint(Context context){
        put(context,IS_FINGERPRINT_OPEN,false);
    }

    public static void openFingerprint(Context context){
      put(context,IS_FINGERPRINT_OPEN,true);
    }

    public static boolean isGestureOpen(Context context){

        return (boolean) get(context,IS_GESTURE_OPEN,false);
    }

    public static void closeGesture(Context context){
        put(context,IS_GESTURE_OPEN,false);
    }

    public static void openGesture(Context context){
        put(context,IS_GESTURE_OPEN,true);
    }


    public static boolean haveSetLock(Context context) {
        return SettingManager.isFingerprintOpen(context) || SettingManager.isGestureOpen(context);
    }



    public static void put(Context context, String key, Object object) {
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else {
            editor.putString(key, object.toString());
        }
        editor.apply();
    }
    public static Object get(Context context, String key, Object defaultObject) {
        SharedPreferences sp = context.getSharedPreferences(SP_SETTING,
                Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }


    public static void recordLastTime(Context context) {
       put(context,APP_LAST_TIME, System.currentTimeMillis());
    }

    public static long getLastTimeSecond(Context context) {
        return System.currentTimeMillis()-(long)get(context, APP_LAST_TIME,System.currentTimeMillis());
    }


    public static void checkUnlock(Context context){
        if(getLastTimeSecond(context)>30*1000){

            if(WalletManager.getInstance().isOpenWallet() &&SettingManager.haveSetLock(context)){

                Intent intent=new Intent(context, UnlockActivity.class);
                intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP);

                context.startActivity(intent);

            }
        }
    }
}
