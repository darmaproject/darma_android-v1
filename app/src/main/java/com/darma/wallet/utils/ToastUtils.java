package com.darma.wallet.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Darma Project on 2019/9/29.
 */
public class ToastUtils {
    public static void showToast(Context context,String str){

        Toast.makeText(context,str,Toast.LENGTH_SHORT).show();
    }
}
