package com.llw.login;

import android.os.Bundle;

import com.llw.basic.BaseActivity;

public class LoginActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        showMsg("LoginActivity");
    }
}