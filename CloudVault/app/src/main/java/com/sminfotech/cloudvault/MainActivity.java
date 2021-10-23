package com.sminfotech.cloudvault;

import static com.sminfotech.cloudvault.SplashActivity.sp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    public static String loginuid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginuid = sp.getString("uid","");

    }
}