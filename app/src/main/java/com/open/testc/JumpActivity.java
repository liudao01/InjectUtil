package com.open.testc;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.open.testc.inject.Autowired;
import com.open.testc.util.InjectUtil;

public class JumpActivity extends AppCompatActivity {

    private static final String TAG = "JumpActivtiy";
    @Autowired("name")
    String mString;

    @Autowired("age")
    int age;

    private TextView tvResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        InjectUtil.InjectView(this);
        setContentView(R.layout.activity_jump);
//        Bundle bundleExtra = getIntent().getBundleExtra();
        Log.d(TAG, "自动装配获取 onCreate: name = " + mString);
        Log.d(TAG, "自动装配获取 onCreate: age = " + age);

        tvResult = (TextView) findViewById(R.id.tv_result);

        tvResult.setText("自动装配获取 name = " + mString + "   自动装配获取 age = " + age);
    }
}