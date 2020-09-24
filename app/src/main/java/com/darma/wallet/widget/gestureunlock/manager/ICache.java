package com.darma.wallet.widget.gestureunlock.manager;

import android.content.Context;

public interface ICache {

    boolean isGestureCodeSet(Context context);

    String getGestureCodeSet(Context context);

    String getGuestGestureCodeSet(Context context);

    void clearGestureCode(Context context);

    void setGestureCode(Context context, String gestureCode);

    void setGuestGestureCode(Context context, String gestureCode);

}
