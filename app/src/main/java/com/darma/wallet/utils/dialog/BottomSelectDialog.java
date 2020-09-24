package com.darma.wallet.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.darma.wallet.R;
import com.darma.wallet.utils.AppUtils;

/**
 * Created by Darma Project on 2019/10/9.
 */
public class BottomSelectDialog extends Dialog {

    LinearLayout linearLayout;
    private BottomSelectDialog( @NonNull Context context) {
        super(context, R.style.Dialog_bottom);
        linearLayout=new LinearLayout(context);
        linearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);



    }

    public static BottomSelectDialog createDialog(Context context){
        return new BottomSelectDialog(context);
    }

    public BottomSelectDialog addItem(String text, final OnItemClick onClickListener){
        View  view=  getLayoutInflater().inflate(R.layout.item_text,null);

        TextView textView=view.findViewById(R.id.text);
        textView.setText(text);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dismiss();
                onClickListener.onItemClick();
            }
        });
        linearLayout.addView(view);
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_bottom_select);

        setCanceledOnTouchOutside(true); //
        setCancelable(true);             //
        Window window = getWindow();      //
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//



        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        LinearLayout ll_container=findViewById(R.id.ll_container);
        ll_container.removeAllViews();
        ll_container.addView(linearLayout);
    }
    public interface OnItemClick{
        void onItemClick();
    }
}
