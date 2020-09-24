package com.darma.wallet.utils.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.*;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.darma.wallet.R;

/**
 * Created by Darma Project on 2019/10/9.
 */
public class DeleteWalletDialog extends Dialog {

    OnConfirm mOnConfirm;
    private DeleteWalletDialog(@NonNull Context context) {
        super(context, R.style.Dialog_bottom);

    }

    public static DeleteWalletDialog createDialog(Context context){
        return new DeleteWalletDialog(context);
    }


    public DeleteWalletDialog setOnConfirmListener(OnConfirm mOnConfirm){
        this.mOnConfirm=mOnConfirm;
        return this;

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.dialog_delete_wallet);

        setCanceledOnTouchOutside(true);
        setCancelable(true);
        Window window = getWindow();
        window.setGravity(Gravity.BOTTOM);

        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);//



        findViewById(R.id.tv_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if(mOnConfirm!=null){
                    mOnConfirm.onConfirm();
                }
            }
        });

    }
    public interface OnConfirm{
        void onConfirm();
    }
}
