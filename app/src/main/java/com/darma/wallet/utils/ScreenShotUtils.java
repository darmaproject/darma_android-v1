package com.darma.wallet.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by Darma Project on 2019/10/8.
 */
public class ScreenShotUtils {


    /**
     * The activity passed in is the activity to be screenshot
     */
    public static Bitmap getViewBitmap(Activity activity) {
        View view = activity.getWindow().getDecorView();
        view.setDrawingCacheEnabled(true);
        view.buildDrawingCache();
        Bitmap b1 = view.getDrawingCache();

        Rect frame = new Rect();
        activity.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
        int statusBarHeight = frame.top;

        int width = activity.getResources().getDisplayMetrics().widthPixels;
        int height = activity.getResources().getDisplayMetrics().heightPixels;
        Bitmap b = Bitmap.createBitmap(b1, 0, statusBarHeight, width, height - statusBarHeight);
        view.destroyDrawingCache();
        return b;
    }

    public static Bitmap getViewBitmap(ViewGroup viewGroup) {

        if(viewGroup.getBackground()==null){
            viewGroup.setBackgroundColor(Color.WHITE);
        }

        //Total height of ViewGroup
        int h = 0;
        Bitmap bitmap;

        // Apply to ListView or RecyclerView
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            h += viewGroup.getChildAt(i).getHeight();
        }

        bitmap = Bitmap.createBitmap(viewGroup.getWidth(), h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        viewGroup.draw(canvas);
        return bitmap;
    }
}
