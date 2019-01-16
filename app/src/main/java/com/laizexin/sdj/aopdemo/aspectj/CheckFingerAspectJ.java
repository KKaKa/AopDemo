package com.laizexin.sdj.aopdemo.aspectj;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.laizexin.sdj.aopdemo.BuildConfig;
import com.laizexin.sdj.aopdemo.FingerDialog;
import com.laizexin.sdj.aopdemo.Main2Activity;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;

import co.infinum.goldfinger.Error;
import co.infinum.goldfinger.Goldfinger;

/**
 * @Description:
 * @Author: laizexin
 * @Time: 2019/1/14
 */
@Aspect
public class CheckFingerAspectJ {
    private static final String TAG = "CheckFingerAspectJ";
    private Context context = null;

    @Pointcut("execution(@com.laizexin.sdj.aopdemo.aspectj.CheckFinger * *(..))")
    public void pointCut(){

    }

    @Around("pointCut()")
    public Object checkFinger(ProceedingJoinPoint joinPoint) throws Throwable{
        //指纹库初始化，需要context对象,先获取context
        if(joinPoint.getArgs() == null || joinPoint.getArgs().length == 0){
            return joinPoint.proceed();
        }
        for(Object obj : joinPoint.getArgs()){
            if(obj instanceof Context){
                context = (Context) obj;
            }
        }
        if(context == null){
            return joinPoint.proceed();
        }
        final Goldfinger goldfinger = new Goldfinger.Builder(context).setLogEnabled(BuildConfig.DEBUG).build();
        //判断设备指纹支持
        if(!goldfinger.hasEnrolledFingerprint()){
            showToast(context,"设备不支持指纹");
            return joinPoint.proceed();
        }
        //判断用户在设备中是否有指纹
        if(!goldfinger.hasEnrolledFingerprint()){
            showToast(context,"设备中暂无用户指纹信息,请先在手机设置中录入。");
            return joinPoint.proceed();
        }
        //显示提示框
        final FingerDialog dialog = new FingerDialog(context);
        dialog.show();
        dialog.setOnCancelListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                goldfinger.cancel();
            }
        });
        //验证指纹
        goldfinger.authenticate(new Goldfinger.Callback() {
            @Override
            public void onSuccess(String value) {
                Log.i(TAG,"value = "+value);
                dialog.startAnim();
                dialog.setTvText("验证成功");
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        dialog.dismiss();
                        Main2Activity.toMain2Activity(context);
                    }
                },1500);
                goldfinger.cancel();
            }

            @Override
            public void onError(final Error error) {
                Log.e(TAG,"error = "+error.toString());
                dialog.setTvText("验证失败");
                Toast.makeText(context,"错误："+error.toString(),Toast.LENGTH_LONG).show();
            }
        });
        return joinPoint.proceed();
    }

    private void showToast(Context context,String msg){
        if(context == null)
            return;
        Toast.makeText(context,msg,Toast.LENGTH_LONG).show();
    }
}
