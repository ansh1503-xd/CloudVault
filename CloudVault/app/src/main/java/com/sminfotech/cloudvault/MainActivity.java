package com.sminfotech.cloudvault;

import static com.sminfotech.cloudvault.Profile.PanicSwitchActivity.destroyAppWhenShake;

import android.app.Dialog;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.sminfotech.cloudvault.Fragments.EarnFragment;
import com.sminfotech.cloudvault.Fragments.MoreFragment;
import com.sminfotech.cloudvault.Fragments.ProfileFragment;
import com.sminfotech.cloudvault.Model.AppControl;
import com.sminfotech.cloudvault.Model.User;
import com.sminfotech.cloudvault.Model.UserDocuments;
import com.sminfotech.cloudvault.Model.UserNotes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static String loginuid;
    BottomNavigationView bottomNavigationView;
    FrameLayout bottomTabContainer;
    Fragment fragment = null;
    FirebaseFirestore firestore;
    public static User user = new User();
    public static UserDocuments userDocuments;
    public static AppControl appControl = new AppControl();
    FirebaseUser firebaseUser;
    AdView adView;
    AdView mAdView;
    public static SensorManager manager;
    public static SharedPreferences sp;
    public static SharedPreferences.Editor editor;
    public static Boolean isPanicSwitchOn = false;
    ActivityResultLauncher<String> externalStoragePermission;
    UserNotes userNotes;
    public static List<UserNotes> userNotesList = new ArrayList<>();
    public static List<UserDocuments> userDocumentsList = new ArrayList<>();
    List<DocumentSnapshot> mArrayList = new ArrayList<>();
    List<DocumentSnapshot> mArrayListDocs = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomTabContainer = findViewById(R.id.bottomTabContainer);
        firestore = FirebaseFirestore.getInstance();

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        sp = getSharedPreferences("panicSwitch", MODE_PRIVATE);
        editor = sp.edit();

        isPanicSwitchOn = sp.getBoolean("isPanicSwitchOn", false);

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        loginuid = firebaseUser.getUid();
        bottomNavigationView.setItemIconTintList(null);
        setDataToModelFromFirebsae();
        setUpAppControl();
        setUserNotesFromFirebase();
        setUserDocumentsFromFirebase();

        Dialog dialog = new Dialog(MainActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setCancelable(false);
        TextView tvText = dialog.findViewById(R.id.tvText);
        tvText.setText("Loading...");

        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
                setUpBottomNavigation();
                dialog.dismiss();
                if (user.getPanicSwitch()){
                    destroyAppWhenShake(manager, MainActivity.this);
                }
            }
        });
        dialog.show();

        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId(appControl.getAdmobBanner());

        mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    private void setUserDocumentsFromFirebase() {
        firestore.collection("userDocs").whereEqualTo("uid", loginuid).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    userDocumentsList.clear();
                    mArrayListDocs = value.getDocuments();
                    for (int i = 0; i < mArrayListDocs.size(); i++) {
                        userDocuments = new UserDocuments();
                        userDocuments.setDocId((String) mArrayListDocs.get(i).get("docId"));
                        userDocuments.setDocLink((String) mArrayListDocs.get(i).get("docLink"));
                        userDocuments.setUid((String) mArrayListDocs.get(i).get("uid"));
                        userDocuments.setName((String) mArrayListDocs.get(i).get("name"));
                        userDocumentsList.add(userDocuments);
                    }
                }
            }
        });
    }

    private void setUserNotesFromFirebase() {
        firestore.collection("userNotes").whereEqualTo("uid", loginuid)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            userNotesList.clear();
                            mArrayList = value.getDocuments();
                            for (int i = 0; i < mArrayList.size(); i++) {
                                userNotes = new UserNotes();
                                userNotes.setUid((String) mArrayList.get(i).get("uid"));
                                userNotes.setNoteId((String) mArrayList.get(i).get("noteId"));
                                userNotes.setTitle((String) mArrayList.get(i).get("title"));
                                userNotes.setBody((String) mArrayList.get(i).get("body"));
                                userNotes.setCreatedAt((Double) mArrayList.get(i).get("createdAt"));
                                userNotesList.add(userNotes);
                                Collections.sort(userNotesList, new Comparator<UserNotes>() {
                                    @Override
                                    public int compare(UserNotes userNotes, UserNotes t1) {
                                        return t1.getCreatedAt().compareTo(userNotes.getCreatedAt());
                                    }
                                });
                            }

                        }
                    }
                });
    }

    private void setUpAppControl() {
        firestore.collection("appControl").document("appControl")
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value!=null){
                            appControl.setFacebookAppID((String) value.get("facebookAppID"));
                            appControl.setAdmobBanner((String) value.get("admobBanner"));
                            appControl.setFacebookBanner((String) value.get("facebookBanner"));
                            appControl.setUnityBanner((String) value.get("unityBanner"));
                            appControl.setUnityID((String) value.get("unityID"));
                            appControl.setUnityInterstitial((String) value.get("unityInterstitial"));
                            appControl.setTestMode((Boolean) value.get("testMode"));
                            appControl.setEnableLoad((Boolean) value.get("enableLoad"));
                            appControl.setUnityRewarded((String) value.get("unityRewarded"));
                        }
                    }
                });
    }

    private void setUpBottomNavigation() {
        bottomNavigationView.setSelectedItemId(R.id.tab_profile);
        fragment = new ProfileFragment();
        replaceFragment(fragment);
        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
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

    }

    private void setDataToModelFromFirebsae() {
        firestore.collection("user").document(loginuid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (value != null) {
                    user.setEmail((String) value.get("email"));
                    user.setName((String) value.get("fullName"));
                    user.setInAppPassword((String) value.get("inAppPassword"));
                    user.setUid((String) value.get("uid"));
                    user.setAudioList((List<String>) value.get("audioList"));
                    user.setNotesList((List<String>) value.get("notesList"));
                    user.setImageList((List<String>) value.get("imageList"));
                    user.setVideoList((List<String>) value.get("videoList"));
                    user.setDocumentsList((List<String>) value.get("documentsList"));
                    user.setPanicSwitch((Boolean) value.get("panicSwitch"));
                    user.setTotalDataQuota((long) value.get("totalDataQuota"));
                    user.setUsedDataQuota((long) value.get("usedDataQuota"));
                    user.setTotalCoins((long) value.get("totalCoins"));
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