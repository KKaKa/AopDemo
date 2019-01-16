package com.laizexin.sdj.aopdemo;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @Description:
 * @Author: laizexin
 * @Time: 2019/1/15
 */
public class FingerDialog extends Dialog {
    private Context context;
    private TextView tv;
    private ImageView iv;
    private Button btn;

    public FingerDialog(@NonNull Context context) {
        super(context);
        this.context = context;
    }

    public FingerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected FingerDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_finger);
        tv = findViewById(R.id.tv_finger);
        iv = findViewById(R.id.iv_finger);
        btn = findViewById(R.id.btn_cancel);
        setCanceledOnTouchOutside(false);
    }

    public void setOnCancelListener(View.OnClickListener listener){
        btn.setOnClickListener(listener);
    }

    public void setIvColor(int color){
        iv.setColorFilter(color);
    }

    public void setTvText(String msg){
        tv.setText(msg == null? "":msg);
    }

    public void startAnim(){
        AnimatedVectorDrawable drawable = (AnimatedVectorDrawable) context.getDrawable(R.drawable.finger_color_anim);
        iv.setImageDrawable(drawable);
        if (drawable != null) {
            drawable.start();
        }
    }
}
