package com.darma.wallet.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.*;
import com.darma.wallet.R;

/**
 * Created by Darma Project on 2019/10/10.
 */
public class SettingItemView extends FrameLayout {


    private int rightIconID=R.mipmap.ic_right;
    private String titleText="",infoText="";


    private TextView tvTile,tvInfo;
    private ImageView ivRight;

    public SettingItemView(Context context) {
        super(context);
        init();
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setAttrs(attrs);
        init();
    }

    public SettingItemView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setAttrs(attrs);
        init();
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setAttrs(attrs);
        init();
    }

    private void setAttrs(AttributeSet attrs) {
        if(attrs!=null){
            TypedArray array = getContext().obtainStyledAttributes(attrs, R.styleable.settingItem);

            rightIconID=array.getResourceId(R.styleable.settingItem_rightIcon,R.mipmap.ic_right);
            titleText=array.getString(R.styleable.settingItem_titleText);
            infoText=array.getString(R.styleable.settingItem_infoText);


        }

    }


    private View view;
    private void init() {

        view=inflate(getContext(),R.layout.item_setting,this);

        tvTile=view.findViewById(R.id.tv_title);
        tvInfo=view.findViewById(R.id.tv_info);
        ivRight=view.findViewById(R.id.iv_right);

        ivRight.setImageResource(rightIconID);
        tvTile.setText(titleText);
        tvInfo.setText(infoText);

    }

    public void  setInfoText(String info){
        tvInfo.setText(info);
    }

}
