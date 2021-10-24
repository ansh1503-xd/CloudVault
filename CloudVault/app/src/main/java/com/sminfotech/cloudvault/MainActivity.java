package com.sminfotech.cloudvault;

import static com.sminfotech.cloudvault.SplashActivity.sp;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.hardware.biometrics.BiometricManager;
import android.hardware.biometrics.BiometricPrompt;
import android.hardware.biometrics.BiometricPrompt.AuthenticationCallback;
import android.os.Build;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.concurrent.Executor;

public class MainActivity extends AppCompatActivity {

    public static String loginuid;
    BottomNavigationView bottomNavigationView;
    FrameLayout bottomTabContainer;
    Fragment fragment = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginuid = sp.getString("uid", "");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomTabContainer = findViewById(R.id.bottomTabContainer);
        bottomNavigationView.setSelectedItemId(R.id.tab_home);
        fragment = new HomeFragment();
        replaceFragment(fragment);


        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.tab_home:
                        fragment = new HomeFragment();
                        replaceFragment(fragment);
                        break;
                    case R.id.tab_profile:
                        fragment = new ProfileFragment();
                        replaceFragment(fragment);
                        break;
                    case R.id.tab_wallet:
                        fragment = new EarnFragment();
                        replaceFragment(fragment);
                        break;
                    case R.id.tab_more:
                        fragment = new MoreFragment();
                        replaceFragment(fragment);
                        break;
                }
                return true;
            }
        });

        bottomNavigationView.setItemIconTintList(null);
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.bottomTabContainer, fragment, null);
        transaction.commit();
    }
}