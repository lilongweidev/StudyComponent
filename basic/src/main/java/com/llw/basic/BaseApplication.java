package com.llw.basic;

import android.app.Application;

public class BaseApplication extends Application {

    public static boolean isApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        isApplication = BuildConfig.isApplication;
    }
}
