package com.darma.wallet.utils;

import android.app.Dialog;
import android.content.Context;
import com.darma.wallet.utils.dialog.CommonAlertDialog;
import com.darma.wallet.utils.dialog.LoadingDialog;

/**
 * Created by Darma Project on 2019/9/27.
 */
public class DialogUtils {


    public static void showAlertDialog(Context context, String title, String content) {

        CommonAlertDialog.createDialog(context)
                .setContent(content)
                .setTitle(title)
                .show();

    }





}
