package com.darma.wallet.widget.gestureunlock.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.darma.wallet.widget.gestureunlock.entity.GesturePoint;
import com.darma.wallet.widget.gestureunlock.util.GestureUtil;
import com.darma.wallet.widget.gestureunlock.util.OnDrawArrowListener;
import com.darma.wallet.widget.gestureunlock.vo.ConfigGestureVO;

import java.util.ArrayList;
import java.util.List;

public class GestureContentView extends FrameLayout {

    private int baseNum = 3;

    private int[] screenDisplay;
    private int blockWidth;
    private List<GesturePoint> list;
    private Context context;
    private GestureDrawLine gestureDrawline;

    private int circleWidth;

    public GestureContentView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureContentView(Context context, boolean isVerify, String[] passWord,
                              GestureDrawLine.GestureCallBack callBack,
                              OnDrawArrowListener listener,
                              ConfigGestureVO data) {
        super(context);
        screenDisplay = GestureUtil.getScreenDisplay(context);
        blockWidth = screenDisplay[0] / 4;
        this.list = new ArrayList<>();
        this.context = context;
        circleWidth = blockWidth - 2 * blockWidth / baseNum;
        addChild(data);
        gestureDrawline = new GestureDrawLine(context, list, isVerify, passWord,
                callBack, listener, circleWidth, data);


    }

    private void addChild(ConfigGestureVO data) {
        for (int i = 0; i < 9; i++) {
            CircleImageView image = new CircleImageView(context, circleWidth,
                    data.getNormalThemeColor(),
                    data.getSelectedThemeColor(),
                    data.getErrorThemeColor(), data.isShowTrack());
            this.addView(image);
            invalidate();
            int row = i / 3;
            int col = i % 3;
            int leftX = col * blockWidth + blockWidth / baseNum;
            int topY = row * blockWidth + blockWidth / baseNum;
            int rightX = col * blockWidth + blockWidth - blockWidth / baseNum;
            int bottomY = row * blockWidth + blockWidth - blockWidth / baseNum;
            GesturePoint p = new GesturePoint(leftX, rightX, topY, bottomY, image, i + 1);
            this.list.add(p);
        }
    }

    public void setParentView(ViewGroup parent) {
        int width = screenDisplay[0] * 3 / 4;
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(width, width);
        this.setLayoutParams(layoutParams);
        gestureDrawline.setLayoutParams(layoutParams);
        parent.addView(gestureDrawline);
        parent.addView(this);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        for (int i = 0; i < getChildCount(); i++) {
            int row = i / 3;
            int col = i % 3;
            View v = getChildAt(i);
            v.layout(col * blockWidth + blockWidth / baseNum,
                    row * blockWidth + blockWidth / baseNum,
                    col * blockWidth + blockWidth - blockWidth / baseNum,
                    row * blockWidth + blockWidth - blockWidth / baseNum);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        for (int i = 0; i < getChildCount(); i++) {
            View v = getChildAt(i);
            v.measure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    public void clearDrawLineState(long delayTime, boolean flag) {
        gestureDrawline.clearDrawLineState(delayTime, flag);
    }
}
