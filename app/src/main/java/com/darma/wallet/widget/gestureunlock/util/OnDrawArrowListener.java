package com.darma.wallet.widget.gestureunlock.util;

import com.darma.wallet.widget.gestureunlock.entity.GesturePoint;

public interface OnDrawArrowListener {
    void onDrawArrow(GesturePoint first, GesturePoint second, int blockWidth);

    void onErrorState();

    void clearAllArrow();
}
