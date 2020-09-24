package com.darma.wallet.widget.gestureunlock.util;

import android.content.Context;
import android.os.Vibrator;

public class VibrateHelp {
    private static Vibrator vibrator;

    @SuppressWarnings("static-access")
    public static void vSimple(Context context, int millisecond) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(millisecond);
        }
    }

    @SuppressWarnings("static-access")
    public static void vComplicated(Context context, long[] pattern, int repeate) {
        vibrator = (Vibrator) context.getSystemService(context.VIBRATOR_SERVICE);
        if (vibrator != null) {
            vibrator.vibrate(pattern, repeate);
        }
    }

    public static void stop() {
        if (vibrator != null) {
            vibrator.cancel();
        }
    }

}
