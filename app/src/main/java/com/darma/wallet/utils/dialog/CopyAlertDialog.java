package com.darma.wallet.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.darma.wallet.R;
import com.darma.wallet.utils.AppUtils;
import com.darma.wallet.utils.ToastUtils;

/**
 * Created by Darma Project on 2019/11/5.
 */
public class CopyAlertDialog extends Dialog {

    private String title,content,confirm;
    TextView tvTitle,tvContent,tvOk;
    private CopyAlertDialog( @NonNull Context context) {
        super(context,R.style.Dialog);
    }
    public static CopyAlertDialog createDialog( Context context){
        return new CopyAlertDialog(context);
    }
    public CopyAlertDialog setTitle(String title){
        this.title=title;
        return this;
    }
    public CopyAlertDialog setContent(String content){
        this.content=content;
        return this;
    }
    public CopyAlertDialog setConfirm(String confirm){
        this.confirm=confirm;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_copy_alert);


         tvTitle= findViewById(R.id.tv_title);
         tvContent= findViewById(R.id.tv_msg);
         tvOk= findViewById(R.id.tv_ok);
        ImageView ivCopy= findViewById(R.id.iv_copy);
        if(title!=null){
            tvTitle.setText(title);
        }
        if(content!=null){
            tvContent.setText(content);
        }
        if(confirm!=null){
            tvOk.setText(confirm);
        }
        ivCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUtils.copyTextToClipboard(getContext(),  tvContent.getText().toString());

                ToastUtils.showToast(getContext(),getContext(). getString(R.string.str_copy_success));
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }




}
