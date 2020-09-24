package com.darma.wallet.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.darma.wallet.R;
import com.wallet.WalletManager;
import com.wallet.utils.StringUtils;

/**
 * Created by Darma Project on 2019/9/27.
 */
public class EnterPwdDialog extends Dialog {


    private InputPassWordListener inputPassWordListener;
    private CheckPassWordListener checkPassWordListener;


    private EnterPwdDialog(@NonNull Context context) {
        super(context, R.style.Dialog_input);
    }

    public static EnterPwdDialog createDialog(Context context) {
        return new EnterPwdDialog(context);
    }

    public EnterPwdDialog setInputPassWordListener(InputPassWordListener inputPassWordListener) {
        this.inputPassWordListener = inputPassWordListener;
        return this;
    }

    public EnterPwdDialog setCheckPassWordListener(CheckPassWordListener checkPassWordListener) {
        this.checkPassWordListener = checkPassWordListener;
        return this;
    }


    private TextInputLayout etPwd;
    TextView tvOk;
    TextView tvCancel;
    ProgressBar progressBar;
    View rlContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_enter_pwd);

        setCancelable(true);
        setCanceledOnTouchOutside(true);

        tvOk = findViewById(R.id.tv_ok);
        tvCancel = findViewById(R.id.tv_cancel);
        etPwd = findViewById(R.id.et_pwd);
        progressBar = findViewById(R.id.pb_load);
        rlContainer = findViewById(R.id.rl_container);

        etPwd.getEditText().addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                etPwd.setError("");

            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
            }
        });
        tvOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (StringUtils.isEmpty(etPwd.getEditText().getText().toString())) {
                    etPwd.setError(getContext().getResources().getString(R.string.str_please_input_pwd));

                    return;
                }
                if (checkPassWordListener != null) {


                    checkPwd();

                } else if (inputPassWordListener != null) {

                    showLoad();
                    inputPassWordListener.success(EnterPwdDialog.this, etPwd);
                    overLoad();
//                    showError("");
                }
            }
        });
        setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                if (checkPassWordListener != null && !isSuccess) {
                    isSuccess=true;
                    checkPassWordListener.cancel();
                }
            }
        });
    }

    boolean isSuccess = false;

    private void checkPwd() {

        showLoad();
        tvOk.postDelayed(new Runnable() {
            @Override
            public void run() {
                String pwd = etPwd.getEditText().getText().toString();
                if (WalletManager.getInstance().checkOpenWalletPWd(pwd)) {
                    if (!isSuccess) {
                        checkPassWordListener.success(pwd);
                    }
                    isSuccess = true;
                    dismiss();


                } else {
                    showError(getContext().getString(R.string.str_pwd_error));

                }
            }
        }, 1000);

    }

    private void showLoad() {
        etPwd.setEnabled(false);
        tvCancel.setEnabled(false);
        tvOk.setEnabled(false);

        rlContainer.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void showError(String error) {
        overLoad();
        etPwd.setError(error);

    }

    private void overLoad(){
        etPwd.setEnabled(true);
        tvCancel.setEnabled(true);
        tvOk.setEnabled(true);

        rlContainer.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);

    }

    public interface InputPassWordListener {
        void success(EnterPwdDialog dialog, TextInputLayout etPwd);


    }

    public interface CheckPassWordListener {
        void success(String password);

        void cancel();

    }
}
