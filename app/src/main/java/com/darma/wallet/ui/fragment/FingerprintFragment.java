package com.darma.wallet.ui.fragment;

import android.annotation.SuppressLint;
import android.hardware.fingerprint.FingerprintManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.hardware.fingerprint.FingerprintManagerCompat;
import android.support.v4.os.CancellationSignal;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.darma.wallet.R;


public class FingerprintFragment extends Fragment {

    TextView tvFingerprintError;



    private CancellationSignal mCancellationSignal;
    private FingerprintManagerCompat fingerprintManagerCompat;

    private boolean isSelfCancelled = false;

    OnResult mOnResult;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_fingerprint_layout, container, false);



        tvFingerprintError=view.findViewById(R.id.tv_fingerprint_error);
        fingerprintManagerCompat = FingerprintManagerCompat.from(getContext());


        return view;

    }

    public void setOnResult( OnResult mOnResult){
        this.mOnResult=mOnResult;
    }




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
//                                dismiss();
                                mOnResult.onError(errString.toString());
//                        mActivity.finish();

                                stopListening();
                            }
                        }
                    }

                    @Override
                    public void onAuthenticationHelp(int helpMsgId, CharSequence helpString) {
                        showError(helpString.toString());
                    }

                    @Override
                    public void onAuthenticationSucceeded(FingerprintManagerCompat.AuthenticationResult result) {
//                MainActivity.startActivity(mActivity, true)

//                        result.getCryptoObject().getSignature().getParameters()
//                        dismiss();
                        stopListening();
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
    public void onDestroyView() {

        stopListening();
        super.onDestroyView();
    }

    @Override
    public void onResume() {
        super.onResume();
        startListening();
    }

    @Override
    public void onPause() {
        super.onPause();

        stopListening();
    }

    private void stopListening() {
        if (null != mCancellationSignal) {
            mCancellationSignal.cancel();
            isSelfCancelled = true;
        }
    }

    public interface OnResult{

        void onSuccess();
        void onError(String error);
    }
}
