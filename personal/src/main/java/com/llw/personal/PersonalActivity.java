package com.llw.personal;

import android.os.Bundle;

import com.llw.annotation.BindPath;
import com.llw.basic.BaseActivity;

@BindPath("personal/PersonalActivity")
public class PersonalActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        String data = getIntent().getExtras().getString("data");
        if (data != null) {
            showMsg(data);
        }
    }
}