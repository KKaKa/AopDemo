package com.laizexin.sdj.aopdemo;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.laizexin.sdj.aopdemo.aspectj.CheckLogin;

public class MainActivity extends AppCompatActivity{
    private boolean isLogin = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @CheckLogin(param = "content")
    private void toNextActivity(Context context,boolean isLogin) {
        Log.i("CheckLoginAspectJ","toNextActivity");
    }

    private void initView() {
        Button mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toNextActivity(MainActivity.this,isLogin);
            }
        });

        CheckBox checkBox = findViewById(R.id.checkBox);
        checkBox.setChecked(true);
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isLogin = isChecked;
            }
        });
    }
}
