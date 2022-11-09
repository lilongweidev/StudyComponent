package com.llw.personal;

import android.os.Bundle;

import com.llw.basic.BaseActivity;

public class PersonalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        showMsg("PersonalActivity");
    }
}