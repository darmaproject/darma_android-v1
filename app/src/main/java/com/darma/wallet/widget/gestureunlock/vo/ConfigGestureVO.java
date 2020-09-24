package com.darma.wallet.widget.gestureunlock.vo;

import com.darma.wallet.R;
import com.darma.wallet.widget.gestureunlock.util.ResourceUtil;

import java.io.Serializable;

public class ConfigGestureVO implements Serializable {

    public int minimumCodeLength;
    public int maximumAllowTrialTimes;
    public long errorRemainInterval;
    public long successRemainInterval;
    public String backgroundColor;
    public String normalThemeColor;
    public String normalTextColor;
    public String selectedThemeColor;
    public String errorThemeColor;
    public String creationBeginPrompt;
    public String codeLengthErrorPrompt;
    public String codeCheckPrompt;
    public String checkErrorPrompt;
    public String creationSucceedPrompt;
    public String verificationBeginPrompt;
    public String verificationErrorPrompt;


    public String errorCodeTooManyTry;
    public String verificationSucceedPrompt;
    public String cancelVerificationButtonTitle;
    public String cancelCreationButtonTitle;
    public String restartCreationButtonTitle;
    public String backgroundImage;
    public String iconImage;
    public boolean isShowTrack;

    public ConfigGestureVO() {
        restoreDefaultConfig();
    }

    public static ConfigGestureVO defaultConfig() {
        return new ConfigGestureVO().restoreDefaultConfig();
    }

    private ConfigGestureVO restoreDefaultConfig() {
        this.minimumCodeLength = 4;
        this.maximumAllowTrialTimes = 5;
        this.errorRemainInterval = 1000;
        this.successRemainInterval = 200;
        this.backgroundColor = "#fff";
        this.normalThemeColor = "#F0F1F4";
        this.normalTextColor = "#202640";
        this.selectedThemeColor = "#3DB4B7";
        this.errorThemeColor = "#EC4561";
//        this.creationBeginPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_creationBeginPrompt");
//        this.codeLengthErrorPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_codeLengthErrorPrompt");
//        this.codeCheckPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_codeCheckPrompt");
//        this.checkErrorPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_checkErrorPrompt");
//        this.creationSucceedPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_creationSucceedPrompt");
//        this.verificationBeginPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_verificationBeginPrompt");
//        this.verificationErrorPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_verificationErrorPrompt");
//        this.verificationSucceedPrompt = ResourceUtil.getString("plugin_uexGestureUnlock_verificationSucceedPrompt");
//        this.cancelVerificationButtonTitle = ResourceUtil.getString("plugin_uexGestureUnlock_cancelVerificationButtonTitle");
//        this.cancelCreationButtonTitle = ResourceUtil.getString("plugin_uexGestureUnlock_cancelCreationButtonTitle");
//        this.restartCreationButtonTitle = ResourceUtil.getString("plugin_uexGestureUnlock_restartCreationButtonTitle");
        this.creationBeginPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_creationBeginPrompt);
        this.codeLengthErrorPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_codeLengthErrorPrompt);
        this.codeCheckPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_codeCheckPrompt);
        this.checkErrorPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_checkErrorPrompt);
        this.creationSucceedPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_creationSucceedPrompt);
        this.verificationBeginPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_verificationBeginPrompt);
        this.verificationErrorPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_verificationErrorPrompt);
        this.verificationSucceedPrompt = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_verificationSucceedPrompt);
        this.cancelVerificationButtonTitle = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_cancelVerificationButtonTitle);
        this.cancelCreationButtonTitle = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_cancelCreationButtonTitle);
        this.restartCreationButtonTitle = ResourceUtil.getString(R.string.plugin_uexGestureUnlock_restartCreationButtonTitle);

        this.backgroundImage = null;
        this.iconImage = null;
        this.isShowTrack = true;
        return this;
    }

    public boolean isShowTrack() {
        return isShowTrack;
    }

    public void setShowTrack(boolean showTrack) {
        isShowTrack = showTrack;
    }

    public int getMinimumCodeLength() {
        return minimumCodeLength;
    }

    public void setMinimumCodeLength(int minimumCodeLength) {
        this.minimumCodeLength = minimumCodeLength;
    }

    public int getMaximumAllowTrialTimes() {
        return maximumAllowTrialTimes;
    }

    public void setMaximumAllowTrialTimes(int maximumAllowTrialTimes) {
        this.maximumAllowTrialTimes = maximumAllowTrialTimes;
    }

    public long getErrorRemainInterval() {
        return errorRemainInterval;
    }

    public void setErrorRemainInterval(long errorRemainInterval) {
        this.errorRemainInterval = errorRemainInterval;
    }

    public long getSuccessRemainInterval() {
        return successRemainInterval;
    }

    public void setSuccessRemainInterval(long successRemainInterval) {
        this.successRemainInterval = successRemainInterval;
    }

    public int getBackgroundColor() {
        return ResourceUtil.parseColor(backgroundColor);
    }

    public void setBackgroundColor(String backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    public int getNormalTextColor() {
        return ResourceUtil.parseColor(normalTextColor);
    }

    public void setNormalTextColor(String normalTextColor) {
        this.normalTextColor = normalTextColor;
    }

    public int getNormalThemeColor() {
        return ResourceUtil.parseColor(normalThemeColor);
    }

    public void setNormalThemeColor(String normalThemeColor) {
        this.normalThemeColor = normalThemeColor;
    }


    public String getErrorCodeTooManyTry() {
        return errorCodeTooManyTry;
    }

    public void setErrorCodeTooManyTry(String errorCodeTooManyTry) {
        this.errorCodeTooManyTry = errorCodeTooManyTry;
    }

    public int getSelectedThemeColor() {
        return ResourceUtil.parseColor(selectedThemeColor);
    }

    public void setSelectedThemeColor(String selectedThemeColor) {
        this.selectedThemeColor = selectedThemeColor;
    }

    public int getErrorThemeColor() {
        return ResourceUtil.parseColor(errorThemeColor);
    }

    public void setErrorThemeColor(String errorThemeColor) {
        this.errorThemeColor = errorThemeColor;
    }

    public String getCreationBeginPrompt() {
//        return creationBeginPrompt;
        return ResourceUtil.getString(R.string.plugin_uexGestureUnlock_creationBeginPrompt);
}

    public void setCreationBeginPrompt(String creationBeginPrompt) {
        this.creationBeginPrompt = creationBeginPrompt;
    }

    public String getCodeLengthErrorPrompt() {
        return codeLengthErrorPrompt;
    }

    public void setCodeLengthErrorPrompt(String codeLengthErrorPrompt) {
        this.codeLengthErrorPrompt = codeLengthErrorPrompt;
    }

    public String getCodeCheckPrompt() {
        return codeCheckPrompt;
    }

    public void setCodeCheckPrompt(String codeCheckPrompt) {
        this.codeCheckPrompt = codeCheckPrompt;
    }

    public String getCheckErrorPrompt() {
        return checkErrorPrompt;
    }

    public void setCheckErrorPrompt(String checkErrorPrompt) {
        this.checkErrorPrompt = checkErrorPrompt;
    }

    public String getCreationSucceedPrompt() {
        return creationSucceedPrompt;
    }

    public void setCreationSucceedPrompt(String creationSucceedPrompt) {
        this.creationSucceedPrompt = creationSucceedPrompt;
    }

    public String getVerificationBeginPrompt() {
        return verificationBeginPrompt;
    }

    public void setVerificationBeginPrompt(String verificationBeginPrompt) {
        this.verificationBeginPrompt = verificationBeginPrompt;
    }

    public String getVerificationErrorPrompt() {
        return verificationErrorPrompt;
    }

    public void setVerificationErrorPrompt(String verificationErrorPrompt) {
        this.verificationErrorPrompt = verificationErrorPrompt;
    }

    public String getVerificationSucceedPrompt() {
        return verificationSucceedPrompt;
    }

    public void setVerificationSucceedPrompt(String verificationSucceedPrompt) {
        this.verificationSucceedPrompt = verificationSucceedPrompt;
    }

    public String getCancelVerificationButtonTitle() {
        return cancelVerificationButtonTitle;
    }

    public void setCancelVerificationButtonTitle(String cancelVerificationButtonTitle) {
        this.cancelVerificationButtonTitle = cancelVerificationButtonTitle;
    }

    public String getCancelCreationButtonTitle() {
        return cancelCreationButtonTitle;
    }

    public void setCancelCreationButtonTitle(String cancelCreationButtonTitle) {
        this.cancelCreationButtonTitle = cancelCreationButtonTitle;
    }

    public String getRestartCreationButtonTitle() {
        return restartCreationButtonTitle;
    }

    public void setRestartCreationButtonTitle(String restartCreationButtonTitle) {
        this.restartCreationButtonTitle = restartCreationButtonTitle;
    }

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(String backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public String getIconImage() {
        return iconImage;
    }

    public void setIconImage(String iconImage) {
        this.iconImage = iconImage;
    }
}
