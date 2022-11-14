package com.llw.component;

import android.os.Bundle;

import com.llw.annotation.BindPath;
import com.llw.basic.BaseActivity;
import com.llw.basic.router.ARouter;

@BindPath("main/MainActivity")
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ARouter.getInstance().jumpActivity("login/LoginActivity");
    }
}