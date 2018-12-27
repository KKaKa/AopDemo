package com.laizexin.sdj.aopdemo.aspectj;

import android.content.Context;

/**
 * @Description:
 * @Author: laizexin
 * @Time: 2018/12/27
 */
public class AopUtil {

    private Context context;

    private static AopUtil instance;

    private AopUtil(Context context) {
        this.context = context;
    }

    public static void init(Context context) {
        instance = new AopUtil(context);
    }

    public static AopUtil getInstance() {
        return instance;
    }

    public Context getContext() {
        return context;
    }

}
