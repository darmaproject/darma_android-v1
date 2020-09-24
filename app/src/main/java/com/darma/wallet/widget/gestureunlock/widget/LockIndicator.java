package com.darma.wallet.widget.gestureunlock.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.darma.wallet.widget.gestureunlock.vo.ConfigGestureVO;

public class LockIndicator extends View {
    private int numRow = 3;
    private int numColumn = 3;
    private int patternWidth = 40;
    private int patternHeight = 40;
    private int f = 5;
    private int g = 5;
    private static final int strokeWidth = 3;
    private Paint paint = null;
    private int normalColor = Color.BLACK;
    private int pressedColor = Color.GREEN;
    private int errorColor = Color.RED;
    private String lockPassStr;
    private int width;
    private int height;
    private int currentPressedColor;

    public LockIndicator(Context paramContext) {
        super(paramContext);
    }

    public LockIndicator(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet, 0);
    }

    public LockIndicator(Context context, ConfigGestureVO data) {
        super(context);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(strokeWidth);
        this.f = (patternWidth / 4);
        this.g = (patternHeight / 4);
        width = numColumn * patternHeight + this.g
                * (-1 + numColumn);
        height = numRow * patternWidth + this.f
                * (-1 + numRow);
        this.normalColor = data.getNormalThemeColor();
        this.pressedColor = data.getSelectedThemeColor();
        this.errorColor = data.getErrorThemeColor();
        this.currentPressedColor = pressedColor;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int baseX = width / 3;
        int baseY = height / 3;
        int baseRadius = (baseX > baseY ? baseY : baseX);
        int radius = baseRadius / 3;
        for (int i = 0; i < numRow; i++) {
            for (int j = 0; j < numColumn; j++) {
                int x = baseX / 2 + baseX * j;
                int y = baseY / 2 + baseY * i;
                canvas.save();
                String curNum = String.valueOf(numColumn * i + (j + 1));
                if (!TextUtils.isEmpty(lockPassStr)) {
                    if (!lockPassStr.contains(curNum)) {
                        paint.setStyle(Paint.Style.STROKE);
                        paint.setColor(normalColor);
                        canvas.drawCircle(x, y, radius, paint);

                    } else {
                        paint.setColor(currentPressedColor);
                        paint.setStyle(Paint.Style.FILL);
                        canvas.drawCircle(x, y, radius, paint);
                    }
                } else {
                    paint.setColor(normalColor);
                    paint.setStyle(Paint.Style.STROKE);
                    canvas.drawCircle(x, y, radius, paint);
                }
                canvas.restore();
            }
        }

    }

    @Override
    protected void onMeasure(int paramInt1, int paramInt2) {
        setMeasuredDimension(numColumn * patternHeight + this.g
                * (-1 + numColumn), numRow * patternWidth + this.f
                * (-1 + numRow));
    }

    public void setPath(String paramString) {
        if (TextUtils.isEmpty(lockPassStr)) {
            this.currentPressedColor = pressedColor;
        }
        lockPassStr = paramString;
        invalidate();
    }

    public void setErrorState() {
        this.currentPressedColor = errorColor;
        invalidate();
    }
}
