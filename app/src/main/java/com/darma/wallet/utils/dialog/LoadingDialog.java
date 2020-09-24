package com.darma.wallet.utils.dialog;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;
import com.darma.wallet.R;


public class LoadingDialog extends ProgressDialog {


    public LoadingDialog(Context context) {
        super(context,R.style.Dialog);
    }

    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(getContext());
    }

    private void init(Context context) {
        setCancelable(true);
        setCanceledOnTouchOutside(true);
        setContentView(R.layout.dialog_loading_layout);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);
    }

    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

}
