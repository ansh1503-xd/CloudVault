package com.sminfotech.cloudvault;

import static com.sminfotech.cloudvault.SplashActivity.sp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.Fragments.EarnFragment;
import com.sminfotech.cloudvault.Fragments.HomeFragment;
import com.sminfotech.cloudvault.Fragments.MoreFragment;
import com.sminfotech.cloudvault.Fragments.ProfileFragment;
import com.sminfotech.cloudvault.Model.User;

public class MainActivity extends AppCompatActivity {

    public static String loginuid;
    BottomNavigationView bottomNavigationView;
    FrameLayout bottomTabContainer;
    Fragment fragment = null;
    FirebaseFirestore firestore;
    public static User user = new User();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginuid = sp.getString("uid", "");
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomTabContainer = findViewById(R.id.bottomTabContainer);
        bottomNavigationView.setSelectedItemId(R.id.tab_home);
        firestore = FirebaseFirestore.getInstance();
        fragment = new HomeFragment();
        replaceFragment(fragment);

        setDataToModelFromFirebsae();

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

    private void setDataToModelFromFirebsae() {
        firestore.collection("user").document(loginuid).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot snapshot = task.getResult();
                    user.setEmail((String) snapshot.get("email"));
                    user.setName((String) snapshot.get("fullName"));
                    user.setInAppPassword((String) snapshot.get("inAppPassword"));
                    user.setUid((String) snapshot.get("uid"));
                }
            }
        });
    }

    private void replaceFragment(Fragment fragment) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.bottomTabContainer, fragment, null);
        transaction.commit();
    }
}