package com.darma.wallet.utils;

import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;

import com.darma.wallet.ui.activity.GestureActivity;
import com.darma.wallet.widget.gestureunlock.manager.ICache;
import com.darma.wallet.widget.gestureunlock.manager.SpCache;
import com.darma.wallet.widget.gestureunlock.util.ResourceUtil;

public class GestureUnlock {
    private static GestureUnlock sGestureUnlock;
    private ICache cache;

    public static GestureUnlock getInstance() {
        if (sGestureUnlock == null) {
            sGestureUnlock = new GestureUnlock();
        }
        return sGestureUnlock;
    }

    public void init(Context applicationContext) {
        ResourceUtil.init(applicationContext);
        cache = new SpCache();
    }

    public void init(Context applicationContext, ICache cache) {
        ResourceUtil.init(applicationContext);
        this.cache = cache;
    }

    public void createGestureUnlock(Activity activityContext) {
        GestureActivity.openActivity(activityContext, GestureActivity.TYPE_GESTURE_CREATE);
    }

    public void verifyGestureUnlock(Activity activityContext) {
        GestureActivity.openActivity(activityContext, GestureActivity.TYPE_GESTURE_VERIFY);
    }

    public void modifyGestureUnlock(Activity activityContext) {
        GestureActivity.openActivity(activityContext, GestureActivity.TYPE_GESTURE_MODIFY);
    }


    public boolean isGestureCodeSet(Context context) {
        return !TextUtils.isEmpty(getGestureCodeSet(context));
    }


    public String getGestureCodeSet(Context context) {
        return cache.getGestureCodeSet(context);
    }


    public void clearGestureCode(Context context) {
        cache.clearGestureCode(context);
    }


    public void setGestureCode(Context context, String gestureCode) {
        cache.setGestureCode(context, gestureCode);
    }
}
