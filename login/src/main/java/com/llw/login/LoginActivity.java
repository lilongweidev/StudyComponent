package com.llw.login;

import android.os.Bundle;

import com.llw.annotation.BindPath;
import com.llw.basic.BaseActivity;
import com.llw.basic.router.ARouter;

@BindPath("login/LoginActivity")
public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showMsg("LoginActivity");
        Bundle bundle = new Bundle();
        bundle.putString("data","Very Good!");
        ARouter.getInstance().jumpActivity("personal/PersonalActivity", bundle);
    }
}