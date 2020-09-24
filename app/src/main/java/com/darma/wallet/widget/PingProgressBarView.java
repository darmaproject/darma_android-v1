package com.darma.wallet.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.darma.wallet.R;

/**
 * Created by Darma Project on 2019/10/14.
 */
public class PingProgressBarView extends FrameLayout {
    public PingProgressBarView( @NonNull Context context) {
        super(context);
        init();
    }

    public PingProgressBarView( @NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PingProgressBarView( @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PingProgressBarView( @NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }
    View view;
    TextView tvSlow,tvCommon,tvFast;
    ProgressBar progressBar;
    private void init(){
        view=inflate(getContext(), R.layout.view_ping_progress_bar_layout,this);
        tvSlow=view.findViewById(R.id.tv_slow);
        tvCommon=view.findViewById(R.id.tv_common);
        tvFast=view.findViewById(R.id.tv_fast);
        progressBar=view.findViewById(R.id.myProgressbar);


    }


    public void setProgress(int progress){
        progressBar.setProgress(progress);
        tvSlow.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color_gray));
        tvCommon.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color_gray));
        tvFast.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color_gray));
        if(progress>=100){
            tvFast.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color_black));
        }else if(progress>=50){

            tvCommon.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color_black));
        }else{

            tvSlow.setTextColor(ContextCompat.getColor(getContext(),R.color.text_color_black));
        }
    }
}
