package com.llw.basic;

import android.app.Application;

import com.llw.basic.router.ARouter;

public class BaseApplication extends Application {

    public static boolean isApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        isApplication = BuildConfig.isApplication;
        ARouter.getInstance().init(this);
    }
}
