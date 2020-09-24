package com.darma.wallet.utils.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.View;
import android.widget.TextView;
import com.darma.wallet.R;
import com.orhanobut.logger.Logger;

/**
 * Created by LuoQiuJie on 2019/8/6.
 */
public class FingerprintDialog extends Dialog {


    TextView tvFingerprintError;
    TextView tvCancel;




    private CancellationSignal mCancellationSignal;
    private FingerprintManagerCompat fingerprintManagerCompat;



    private boolean isSelfCancelled = false;

    OnResult mOnResult;
    private FingerprintDialog(@NonNull Context context) {
        super(context, R.style.Dialog);
    }

    public static FingerprintDialog createDialog(Context context){
        return new FingerprintDialog(context);
    }


    public  FingerprintDialog setOnResultListener(OnResult mOnResult){
        this.mOnResult=mOnResult;
        return this;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_finger_print);

        tvFingerprintError=findViewById(R.id.tv_fingerprint_error);
        tvCancel=findViewById(R.id.tv_cancel);

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                stopListening();
            }
        });

        fingerprintManagerCompat = FingerprintManagerCompat.from(getContext());

        startListening();
    }




    /**
     * startListening
     */
    @SuppressLint("MissingPermission")
    private void startListening() {
        isSelfCancelled = false;
        mCancellationSignal = new CancellationSignal();
        fingerprintManagerCompat.authenticate(null, 0, mCancellationSignal, new FingerprintManagerCompat.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errMsgId, CharSequence errString) {
                if (!isSelfCancelled) {
                    showError(errString.toString());
                    if (errMsgId == FingerprintManager.FINGERPRINT_ERROR_LOCKOUT) {
//                        Toast.makeText(getContext(), errString, Toast.LENGTH_SHORT).show();
//
                        dismiss();
                        mOnResult.onError(errString.toString());
//                        mActivity.finish();
                    }
                }
            }

            @Override
            public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                showError(helpString.toString());
            }

            @Override
            public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {

//                Toast.makeText(getContext(), "", Toast.LENGTH_SHORT).show();
//                FingerprintManagerCompat.CryptoObject cryptoObject=result.getCryptoObject();
//                Logger.d("getCipher = "+cryptoObject.getCipher() +
//                        "\ngetMac = "+cryptoObject.getMac()+
//                        "\ngetSignature = "+cryptoObject.getSignature());
                dismiss();
                mOnResult.onSuccess();

            }

            @Override
            public void onAuthenticationFailed() {
                showError(getContext().getResources().getString(R.string.str_fingerprint_fail_re_try));
            }
        }
        , null);

    }

    private void showError(String errString) {

        tvFingerprintError.setText(errString);
    }

    @Override
    public void dismiss() {
        super.dismiss();
        mOnResult.onDismiss();
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopListening();

    }

    /**
     * stopListening
     */
    private void stopListening() {
        if (null != mCancellationSignal) {
            mCancellationSignal.cancel();
            isSelfCancelled = true;
        }
    }

    public interface OnResult{

        void onSuccess();
        void onError(String error);
        void onDismiss();
    }
}
