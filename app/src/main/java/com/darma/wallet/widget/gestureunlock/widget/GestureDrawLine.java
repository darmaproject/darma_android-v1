package com.darma.wallet.widget.gestureunlock.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PorterDuff;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Pair;
import android.view.MotionEvent;
import android.view.View;

import com.darma.wallet.widget.gestureunlock.JsConst;
import com.darma.wallet.widget.gestureunlock.entity.GesturePoint;
import com.darma.wallet.widget.gestureunlock.util.GestureUtil;
import com.darma.wallet.widget.gestureunlock.util.OnDrawArrowListener;
import com.darma.wallet.widget.gestureunlock.util.VibrateHelp;
import com.darma.wallet.widget.gestureunlock.vo.ConfigGestureVO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GestureDrawLine extends View {
    private int mov_x;
    private int mov_y;
    private Paint paint;
    private Canvas canvas;
    private Bitmap bitmap;
    private List<GesturePoint> list;
    private List<Pair<GesturePoint, GesturePoint>> lineList;
    private Map<String, GesturePoint> autoCheckPointMap;
    private boolean isDrawEnable = true;

    private int[] screenDisplay;

    private GesturePoint currentPoint;
    private GestureCallBack callBack;

    private StringBuilder passWordSb;

    private boolean isVerify;

    private String[] passWord;

    private OnDrawArrowListener mOnDrawArrowListener;

    private int blockWidth;

    private int selectedColor;

    private int errorColor;

    private boolean isShowTrack = true;

    public GestureDrawLine(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public GestureDrawLine(Context context, List<GesturePoint> list, boolean isVerify,
                           String[] passWord, GestureCallBack callBack,
                           OnDrawArrowListener listener,
                           int blockWidth,
                           ConfigGestureVO data) {
        super(context);
        this.blockWidth = blockWidth;
        screenDisplay = GestureUtil.getScreenDisplay(context);
        paint = new Paint(Paint.DITHER_FLAG);
        bitmap = Bitmap.createBitmap(screenDisplay[0], screenDisplay[0], Bitmap.Config.ARGB_8888); //
        canvas = new Canvas();
        canvas.setBitmap(bitmap);
        paint.setStyle(Style.STROKE);
        paint.setStrokeWidth(5);
        paint.setColor(selectedColor);
        paint.setAntiAlias(true);

        this.list = list;
        this.lineList = new ArrayList<>();

        initAutoCheckPointMap();
        this.callBack = callBack;

        this.isVerify = isVerify;
        this.passWordSb = new StringBuilder();
        this.passWord = passWord;
        this.mOnDrawArrowListener = listener;

        this.selectedColor = data.getSelectedThemeColor();
        this.errorColor = data.getErrorThemeColor();
        this.isShowTrack = data.isShowTrack();
    }

    private void initAutoCheckPointMap() {
        autoCheckPointMap = new HashMap<String, GesturePoint>();
        autoCheckPointMap.put("1,3", getGesturePointByNum(2));
        autoCheckPointMap.put("1,7", getGesturePointByNum(4));
        autoCheckPointMap.put("1,9", getGesturePointByNum(5));
        autoCheckPointMap.put("2,8", getGesturePointByNum(5));
        autoCheckPointMap.put("3,7", getGesturePointByNum(5));
        autoCheckPointMap.put("3,9", getGesturePointByNum(6));
        autoCheckPointMap.put("4,6", getGesturePointByNum(5));
        autoCheckPointMap.put("7,9", getGesturePointByNum(8));
    }

    private GesturePoint getGesturePointByNum(int num) {
        for (GesturePoint point : list) {
            if (point.getNum() == num) {
                return point;
            }
        }
        return null;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(bitmap, 0, 0, null);

    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!isDrawEnable) {
            return true;
        }
        paint.setColor(selectedColor);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mov_x = (int) event.getX();
                mov_y = (int) event.getY();
                currentPoint = getPointAt(mov_x, mov_y);
                if (currentPoint != null) {
                    currentPoint.setPointState(JsConst.POINT_STATE_SELECTED);
                    passWordSb.append(currentPoint.getNum());
                    VibrateHelp.vSimple(getContext(), 30);
                }
                invalidate();

                break;
            case MotionEvent.ACTION_MOVE:
                clearScreenAndDrawList();

                GesturePoint pointAt = getPointAt((int) event.getX(), (int) event.getY());
                if (currentPoint == null && pointAt == null) {
                    return true;
                } else {
                    if (currentPoint == null) {
                        currentPoint = pointAt;
                        currentPoint.setPointState(JsConst.POINT_STATE_SELECTED);
                        passWordSb.append(currentPoint.getNum());
                        VibrateHelp.vSimple(getContext(), 30);
                    }
                }
                if (pointAt == null || currentPoint.equals(pointAt) ||
                        JsConst.POINT_STATE_SELECTED == pointAt.getPointState()) {
                    if (isShowTrack) {
                        canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(),
                                event.getX(), event.getY(), paint);//
                    }
                } else {
                    if (isShowTrack) {
                        canvas.drawLine(currentPoint.getCenterX(), currentPoint.getCenterY(),
                                pointAt.getCenterX(), pointAt.getCenterY(), paint);//
                    }
                    pointAt.setPointState(JsConst.POINT_STATE_SELECTED);

                    GesturePoint betweenPoint = getBetweenCheckPoint(currentPoint, pointAt);
                    if (betweenPoint != null &&
                            JsConst.POINT_STATE_SELECTED != betweenPoint.getPointState()) {
                        Pair<GesturePoint, GesturePoint> pair1 =
                                new Pair<GesturePoint, GesturePoint>(currentPoint, betweenPoint);
                        if (mOnDrawArrowListener != null) {
                            mOnDrawArrowListener.onDrawArrow(currentPoint, betweenPoint, blockWidth);
                        }
                        lineList.add(pair1);
                        passWordSb.append(betweenPoint.getNum());
                        VibrateHelp.vSimple(getContext(), 30);
                        Pair<GesturePoint, GesturePoint> pair2 =
                                new Pair<GesturePoint, GesturePoint>(betweenPoint, pointAt);
                        if (mOnDrawArrowListener != null) {
                            mOnDrawArrowListener.onDrawArrow(betweenPoint, pointAt, blockWidth);
                        }
                        lineList.add(pair2);
                        passWordSb.append(pointAt.getNum());
                        VibrateHelp.vSimple(getContext(), 30);
                        betweenPoint.setPointState(JsConst.POINT_STATE_SELECTED);
                        currentPoint = pointAt;
                    } else {
                        Pair<GesturePoint, GesturePoint> pair =
                                new Pair<>(currentPoint, pointAt);
                        if (mOnDrawArrowListener != null) {
                            mOnDrawArrowListener.onDrawArrow(currentPoint, pointAt, blockWidth);
                        }
                        lineList.add(pair);
                        passWordSb.append(pointAt.getNum());
                        VibrateHelp.vSimple(getContext(), 30);
                        currentPoint = pointAt;
                    }
                }
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                if (isVerify) {
                    if (passWord[0].equals(passWordSb.toString())) {
                        callBack.checkedSuccess();
                    } else {
                        if (passWord.length > 1 && passWord[1].equals(passWordSb.toString())) {
                            callBack.checkedGuestSuccess();
                        }
                        callBack.checkedFail();
                    }
                } else {
                    callBack.onGestureCodeInput(passWordSb.toString());
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void clearDrawLineState(long delayTime, boolean flag) {
        if (flag) {
            isDrawEnable = false;
            drawErrorPathTip();
        }
        new Handler().postDelayed(new clearStateRunnable(), delayTime);
    }

    final class clearStateRunnable implements Runnable {
        public void run() {
            passWordSb = new StringBuilder();
            lineList.clear();
            clearScreenAndDrawList();
            for (GesturePoint p : list) {
                p.setPointState(JsConst.POINT_STATE_NORMAL);
            }
            invalidate();
            isDrawEnable = true;
            if (mOnDrawArrowListener != null) {
                mOnDrawArrowListener.clearAllArrow();
            }
        }
    }

    private GesturePoint getPointAt(int x, int y) {

        for (GesturePoint point : list) {
            int leftX = point.getLeftX();
            int rightX = point.getRightX();
            if (!(x >= leftX && x < rightX)) {
                continue;
            }

            int topY = point.getTopY();
            int bottomY = point.getBottomY();
            if (!(y >= topY && y < bottomY)) {
                continue;
            }

            return point;
        }

        return null;
    }

    private GesturePoint getBetweenCheckPoint(GesturePoint pointStart, GesturePoint pointEnd) {
        int startNum = pointStart.getNum();
        int endNum = pointEnd.getNum();
        String key;
        if (startNum < endNum) {
            key = startNum + "," + endNum;
        } else {
            key = endNum + "," + startNum;
        }
        return autoCheckPointMap.get(key);
    }

    private void clearScreenAndDrawList() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (isShowTrack) {
            for (Pair<GesturePoint, GesturePoint> pair : lineList) {
                canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                        pair.second.getCenterX(), pair.second.getCenterY(), paint);//
            }
        }
    }

    private void drawErrorPathTip() {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        if (mOnDrawArrowListener != null) {
            mOnDrawArrowListener.onErrorState();
        }
        paint.setColor(errorColor);
        for (Pair<GesturePoint, GesturePoint> pair : lineList) {
            pair.first.setPointState(JsConst.POINT_STATE_WRONG);
            pair.second.setPointState(JsConst.POINT_STATE_WRONG);
            canvas.drawLine(pair.first.getCenterX(), pair.first.getCenterY(),
                    pair.second.getCenterX(), pair.second.getCenterY(), paint);//
        }
        invalidate();
    }


    public interface GestureCallBack {

        void onGestureCodeInput(String inputCode);

        void checkedSuccess();


        void checkedGuestSuccess();


        void checkedFail();
    }

}
