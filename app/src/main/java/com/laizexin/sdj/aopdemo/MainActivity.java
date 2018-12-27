package com.laizexin.sdj.aopdemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.laizexin.sdj.aopdemo.aspectj.AopUtil;
import com.laizexin.sdj.aopdemo.aspectj.CheckLogin;

public class MainActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @CheckLogin(isLogin = true)
    private void toNextActivity() {
        Log.i("CheckLoginAspectJ","toNextActivity");
    }

    private void initView() {
        Button mBtn = findViewById(R.id.btn);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AopUtil.init(MainActivity.this);
                toNextActivity();
            }
        });
    }
}
