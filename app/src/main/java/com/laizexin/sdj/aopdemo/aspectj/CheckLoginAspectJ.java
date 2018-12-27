package com.laizexin.sdj.aopdemo.aspectj;

import android.content.Context;
import android.util.Log;

import com.laizexin.sdj.aopdemo.LoginActivity;
import com.laizexin.sdj.aopdemo.Main2Activity;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Description:
 * @Author: laizexin
 * @Time: 2018/12/27
 */
@Aspect
public class CheckLoginAspectJ {
    private static final String TAG = "CheckLoginAspectJ";

    @Pointcut("execution(@com.laizexin.sdj.aopdemo.aspectj.CheckLogin * *(..))")
    public void pointCut(){

    }

    @Before("pointCut()")
    public void before(JoinPoint point){
        Log.i(TAG,"CheckLoginAspectJ.before");
    }

    @Around("pointCut()")
    public Object checkLogin(ProceedingJoinPoint joinPoint) throws Throwable{
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        CheckLogin checkLogin = method.getAnnotation(CheckLogin.class);
        boolean isLogin = checkLogin.isLogin();
        Context context = AopUtil.getInstance().getContext();

        if(isLogin){
            Main2Activity.toMain2Activity(context);
        }else{
            LoginActivity.toLoginActivity(context);
        }

        return joinPoint.proceed();
    }

    @After("pointCut()")
    public void after(JoinPoint point){
        Log.i(TAG,"CheckLoginAspectJ.after");
    }

    @AfterThrowing(value = "pointcut()", throwing = "ex")
    public void afterThrowing(Throwable ex) {
        Log.i(TAG,"CheckLoginAspectJ.afterThrowing.ex = " + ex.getMessage());
    }
}
