package com.sminfotech.cloudvault.Fragments;

import static com.sminfotech.cloudvault.MainActivity.appControl;
import static com.sminfotech.cloudvault.MainActivity.user;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.sminfotech.cloudvault.Profile.PanicSwitchActivity;
import com.sminfotech.cloudvault.Profile.PasswordActivity;
import com.sminfotech.cloudvault.Profile.VaultActivity;
import com.sminfotech.cloudvault.R;
import com.sminfotech.cloudvault.Unity.UnityAdsListner;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;


public class ProfileFragment extends Fragment {

    TextView tvProfileName, tvChangePin;
    LinearLayout llChangePin, llYourVault, llPanicSwitch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileName = v.findViewById(R.id.tvProfileName);
        llChangePin = v.findViewById(R.id.llChangePin);
        tvChangePin = v.findViewById(R.id.tvChangePin);
        llYourVault = v.findViewById(R.id.llYourVault);
        llPanicSwitch = v.findViewById(R.id.llPanicSwitch);

        UnityAdsListner myAdsListener = new UnityAdsListner();
        UnityAds.addListener(myAdsListener);
        UnityAds.initialize(getContext(), appControl.getUnityID(), appControl.getTestMode());

        llYourVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UnityAds.isInitialized()) {
                    UnityAds.show((Activity) getContext(), appControl.getUnityInterstitial(), new IUnityAdsShowListener() {
                        @Override
                        public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
                            Intent i = new Intent(getContext(), VaultActivity.class);
                            Log.d("error1111",unityAdsShowError.toString());
                            startActivity(i);
                        }

                        @Override
                        public void onUnityAdsShowStart(String s) {
                        }

                        @Override
                        public void onUnityAdsShowClick(String s) {

                        }

                        @Override
                        public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
                            Intent i = new Intent(getContext(), VaultActivity.class);
                            startActivity(i);
                        }
                    });
                }
            }
        });

        llPanicSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), PanicSwitchActivity.class);
                startActivity(i);
            }
        });

        Intent intent = new Intent(getContext(), PasswordActivity.class);
        if (user.getInAppPassword() != null && !user.getInAppPassword().equals("")) {
            tvChangePin.setText("Change PIN");
            intent.putExtra("type", "change");
        } else {
            tvChangePin.setText("Set PIN");
            intent.putExtra("type", "set");
        }
        llChangePin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });

        tvProfileName.setText(user.getName());
        return v;
    }
}