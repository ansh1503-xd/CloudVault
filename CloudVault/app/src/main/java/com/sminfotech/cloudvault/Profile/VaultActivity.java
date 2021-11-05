package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.appControl;
import static com.sminfotech.cloudvault.MainActivity.manager;
import static com.sminfotech.cloudvault.Profile.PanicSwitchActivity.destroyAppWhenShake;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AdListener;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.sminfotech.cloudvault.R;
import com.sminfotech.cloudvault.Unity.UnityBannerListener;
import com.unity3d.ads.UnityAds;
import com.unity3d.services.banners.BannerView;
import com.unity3d.services.banners.UnityBannerSize;

public class VaultActivity extends AppCompatActivity {

    ImageView backToProfile;
    BannerView bottomBanner;
    LinearLayout bottomBannerView;
    AdView adView;
    LinearLayout llImage, llVideo, llNotes, llAudio, llDocuments;
    Boolean isFacebookAdsEnabled = true;
    AdListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vault);

        backToProfile = findViewById(R.id.backToProfile);
        bottomBannerView = findViewById(R.id.bottomBannerView);
        llImage = findViewById(R.id.llImage);
        llVideo = findViewById(R.id.llVideo);
        llNotes = findViewById(R.id.llNotes);
        llAudio = findViewById(R.id.llAudio);
        llDocuments = findViewById(R.id.llDocuments);


        bottomBanner = new BannerView(VaultActivity.this, appControl.getUnityBanner(), new UnityBannerSize(320, 50));
        UnityBannerListener bannerListener = new UnityBannerListener();
        UnityAds.initialize(this, appControl.getUnityID(), appControl.getTestMode(), appControl.getEnableLoad());
        bottomBanner.setListener(bannerListener);

        destroyAppWhenShake(manager, VaultActivity.this);

        adView = new AdView(this, appControl.getFacebookBanner(), AdSize.BANNER_HEIGHT_50);
        bottomBannerView.addView(adView);
        listener = new AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                isFacebookAdsEnabled = false;
                bottomBannerView.addView(bottomBanner);
                bottomBanner.load();
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        };

        adView.loadAd(adView.buildLoadAdConfig().withAdListener(listener).build());

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        llImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VaultActivity.this, UploadImageActivity.class);
                startActivity(i);
            }
        });

        llVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VaultActivity.this, UploadVideoActivity.class);
                startActivity(i);
            }
        });

        llNotes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VaultActivity.this, NotesActivity.class);
                startActivity(i);
            }
        });

        llAudio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VaultActivity.this, UploadAudioActivity.class);
                startActivity(i);
            }
        });

        llDocuments.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(VaultActivity.this, UploadDocumentsActivity.class);
                startActivity(i);
            }
        });


    }

}