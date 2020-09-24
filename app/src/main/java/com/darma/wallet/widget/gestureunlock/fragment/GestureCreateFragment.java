package com.darma.wallet.widget.gestureunlock.fragment;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.darma.wallet.R;
import com.darma.wallet.widget.gestureunlock.JsConst;
import com.darma.wallet.widget.gestureunlock.util.GestureUtil;
import com.darma.wallet.widget.gestureunlock.util.ResourceUtil;
import com.darma.wallet.widget.gestureunlock.vo.ConfigGestureVO;
import com.darma.wallet.widget.gestureunlock.vo.ResultFailedVO;
import com.darma.wallet.widget.gestureunlock.vo.ResultVerifyVO;
import com.darma.wallet.widget.gestureunlock.widget.GestureContentView;
import com.darma.wallet.widget.gestureunlock.widget.GestureDrawLine;
import com.darma.wallet.widget.gestureunlock.widget.LockIndicator;

public class GestureCreateFragment extends GestureBaseFragment implements OnClickListener {

    private RelativeLayout mBg;
    private GestureCreateListener mGestureCreateListener;
    private TextView mTextCancel;
    private LockIndicator mLockIndicator;
    private TextView mTextTip;
    private FrameLayout mGestureContainer;
    private GestureContentView mGestureContentView;
    private TextView mTextReset;
    private boolean mIsFirstInput = true;
    private String mFirstPassword = null;
    private ConfigGestureVO mData;
    private LinearLayout mGestureTipLayout;

    public interface GestureCreateListener {
        void onCreateFinished(String gestureCode);

        void onCreateFailed(ResultVerifyVO result);

        void closeLayout();

        void onCancel();

        void onEventOccur(int eventCode);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = null;
        try {
            view = inflater.inflate(
                    ResourceUtil.getResLayoutID("plugin_uexgestureunlock_gesture_edit"),
                    container, false);
            mBg = view.findViewById(ResourceUtil.getResIdID("plugin_uexGestureUnlock_bg"));
            mGestureTipLayout = view.findViewById(
                    ResourceUtil.getResIdID("plugin_uexGestureUnlock_gesture_tip_layout"));
            mTextCancel = view.findViewById(ResourceUtil
                    .getResIdID("plugin_uexGestureUnlock_text_cancel"));
            mTextReset = view.findViewById(ResourceUtil
                    .getResIdID("plugin_uexGestureUnlock_text_reset"));
            mTextReset.setClickable(false);
            mGestureContainer = view.findViewById(ResourceUtil
                    .getResIdID("plugin_uexGestureUnlock_gesture_container"));
            RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            if (this.getActivity() != null) {
//                layoutParams.topMargin = GestureUtil.getScreenDisplay(this.getActivity())[0] / 8;
                layoutParams.leftMargin = GestureUtil.getScreenDisplay(this.getActivity())[0] / 8;
            } else {
//                layoutParams.topMargin = 120;
                layoutParams.leftMargin = 120;
            }

            layoutParams.addRule(RelativeLayout.BELOW, ResourceUtil.getResIdID("plugin_uexGestureUnlock_gesture_tip_layout"));
            mGestureContainer.setLayoutParams(layoutParams);
            setParentViewFrameLayout(mGestureContainer);
            setTipLayout();
            setUpData();
            mGestureContentView = new GestureContentView(this.getActivity(), false, null,
                    new GestureDrawLine.GestureCallBack() {
                        @Override
                        public void onGestureCodeInput(final String inputCode) {
                            if (!isInputPassValidate(inputCode)) {
                                setTextTipError(String.format(getString(R.string.plugin_uexGestureUnlock_codeLengthErrorPrompt),
                                        mData.getMinimumCodeLength()));
                                mGestureContentView.clearDrawLineState(mData.getErrorRemainInterval(), true);
                                mGestureCreateListener.onEventOccur(JsConst.EVENT_LENGTH_ERROR);
                                return;
                            }
                            if (mIsFirstInput) {
                                mFirstPassword = inputCode;
                                updateCodeList(inputCode);
                                mGestureContentView.clearDrawLineState(mData.getSuccessRemainInterval(), false);
                                setTextTipNormal(getString(R.string.plugin_uexGestureUnlock_codeCheckPrompt));
                                mTextReset.setClickable(true);
                                mTextReset.setText(getString(R.string.plugin_uexGestureUnlock_restartCreationButtonTitle));
                                mGestureCreateListener.onEventOccur(JsConst.EVENT_SECOND_INPUT);
                            } else {
                                if (inputCode.equals(mFirstPassword)) {
                                    mGestureContentView.clearDrawLineState(mData.getSuccessRemainInterval(), false);
                                    setTextTipNormal(getString(R.string.plugin_uexGestureUnlock_creationSucceedPrompt));
                                    new Handler().postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (mGestureCreateListener != null) {
                                                mGestureCreateListener.onCreateFinished(inputCode);
                                                mGestureCreateListener.onEventOccur(JsConst.EVENT_CREATE_SUCCESS);
                                            }
                                        }
                                    }, mData.getSuccessRemainInterval());
                                } else {
                                    mGestureCreateListener.onEventOccur(JsConst.EVENT_NOT_SAME);
                                    setTextTipError(getString(R.string.plugin_uexGestureUnlock_checkErrorPrompt));
                                    Animation shakeAnimation = AnimationUtils.loadAnimation(
                                            GestureCreateFragment.this.getActivity(),
                                            ResourceUtil.getResAnimID("plugin_uexgestureunlock_shake"));
                                    mTextTip.startAnimation(shakeAnimation);
                                    mGestureContentView.clearDrawLineState(mData.getErrorRemainInterval(), true);
                                    mLockIndicator.setErrorState();
                                }
                            }
                            mIsFirstInput = false;
                        }

                        @Override
                        public void checkedSuccess() {

                        }

                        @Override
                        public void checkedGuestSuccess() {

                        }

                        @Override
                        public void checkedFail() {

                        }
                    }, mDrawArrowListener, mData);
            mGestureContentView.setParentView(mGestureContainer);
            updateCodeList("");
            setUpListeners();
            if (mGestureCreateListener != null) {
                mGestureCreateListener.onEventOccur(JsConst.EVENT_PLUGIN_INIT);
                mGestureCreateListener.onEventOccur(JsConst.EVENT_START_CREATE);
            }
        } catch (Exception e) {
            if (mGestureCreateListener != null) {
                ResultFailedVO result = new ResultFailedVO();
                result.setIsFinished(false);
                result.setErrorCode(JsConst.ERROR_CODE_UNKNOWN);
                result.setErrorString(ResourceUtil
                        .getString(R.string.plugin_uexGestureUnlock_errorCodeUnknown));
                mGestureCreateListener.onCreateFailed(result);
            }
        }
        return view;
    }

    private void setTipLayout() {
        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLockIndicator = new LockIndicator(this.getActivity(), mData);
        mLockIndicator.setLayoutParams(lp1);
        mGestureTipLayout.addView(mLockIndicator);

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mTextTip = new TextView(this.getActivity());
        lp2.gravity = Gravity.CENTER_HORIZONTAL;
        lp2.setMargins(0, 20, 0, 0);
//        setTextTipNormal(mData.getCreationBeginPrompt());
        setTextTipNormal(getString(R.string.plugin_uexGestureUnlock_creationBeginPrompt));
        mTextTip.setLayoutParams(lp2);
        mGestureTipLayout.addView(mTextTip);
    }

    private void setUpData() {
        if (!TextUtils.isEmpty(mData.getBackgroundImage())) {
            mBg.setBackgroundDrawable(new BitmapDrawable(
                    ResourceUtil.getLocalImg(this.getActivity(),
                            mData.getBackgroundImage())));
        } else {
            mBg.setBackgroundColor(mData.getBackgroundColor());
        }
        mTextCancel.setTextColor(mData.getNormalThemeColor());
        mTextCancel.setText(getString(R.string.plugin_uexGestureUnlock_cancelCreationButtonTitle));
        mTextReset.setTextColor(mData.getNormalThemeColor());
        mTextReset.setText(getString(R.string.plugin_uexGestureUnlock_restartCreationButtonTitle));
    }

    private void setTextTipError(String tips) {
        mTextTip.setTextColor(mData.getErrorThemeColor());
        mTextTip.setText(tips);
    }

    private void setTextTipNormal(String tips) {
        mTextTip.setTextColor(mData.getNormalTextColor());
        mTextTip.setText(tips);
    }

    private void setUpListeners() {
        mTextCancel.setOnClickListener(this);
        mTextReset.setOnClickListener(this);
    }

    public void setGestureCreateListener(GestureCreateListener listener) {
        this.mGestureCreateListener = listener;
    }

    private void updateCodeList(String inputCode) {
        mLockIndicator.setPath(inputCode);
    }

    private boolean isInputPassValidate(String inputPassword) {
        return (!TextUtils.isEmpty(inputPassword) && inputPassword.length() >= mData.getMinimumCodeLength());
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == ResourceUtil.getResIdID("plugin_uexGestureUnlock_text_cancel")) {
            if (mGestureCreateListener != null) {
                mGestureCreateListener.onCancel();
            }
        } else if (v.getId() == ResourceUtil.getResIdID("plugin_uexGestureUnlock_text_reset")) {
            mIsFirstInput = true;
            updateCodeList("");
            setTextTipNormal(getString(R.string.plugin_uexGestureUnlock_creationBeginPrompt));
        }
    }

    public void setData(ConfigGestureVO data) {
        if (data == null) {
            mData = new ConfigGestureVO();
        } else {
            this.mData = data;
        }
        super.setData(mData);
    }

}
