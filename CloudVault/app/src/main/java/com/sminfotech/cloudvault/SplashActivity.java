package com.sminfotech.cloudvault;

import static com.sminfotech.cloudvault.MainActivity.loginuid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.Model.User;

public class SplashActivity extends AppCompatActivity {

    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static boolean isLoggedIn = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        sp = getSharedPreferences("isLoggedIn", MODE_PRIVATE);
        editor = sp.edit();

        isLoggedIn = sp.getBoolean("isLoggedIn", false);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i;
                if (isLoggedIn) {
                    i = new Intent(SplashActivity.this, MainActivity.class);
                } else {
                    i = new Intent(SplashActivity.this, LoginActivity.class);
                }
                startActivity(i);
                finish();
            }
        }, 3000);

    }

}