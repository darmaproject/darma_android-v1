package com.darma.wallet.utils.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import com.darma.wallet.R;

/**
 * Created by Darma Project on 2019/9/27.
 */
public class CommonAlertDialog extends Dialog {


    private String title,content,confirm;

    private CommonAlertDialog( @NonNull Context context) {
        super(context,R.style.Dialog);
    }
    public static CommonAlertDialog createDialog( Context context){
        return new CommonAlertDialog(context);
    }
    public CommonAlertDialog setTitle(String title){
        this.title=title;
        return this;
    }
    public CommonAlertDialog setContent(String content){
        this.content=content;
        return this;
    }
    public CommonAlertDialog setConfirm(String confirm){
        this.confirm=confirm;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_common_alert);


        TextView tvTitle= findViewById(R.id.tv_title);
        TextView tvContent= findViewById(R.id.tv_msg);
        TextView tvOk= findViewById(R.id.tv_ok);
        if(title!=null){
            tvTitle.setText(title);
        }
        if(content!=null){
            tvContent.setText(content);
        }
        if(confirm!=null){
            tvOk.setText(confirm);
        }

        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }



}
