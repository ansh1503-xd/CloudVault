package com.sminfotech.cloudvault.Fragments;

import static com.sminfotech.cloudvault.MainActivity.appControl;
import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.R;
import com.unity3d.ads.IUnityAdsListener;
import com.unity3d.ads.UnityAds;

public class EarnFragment extends Fragment {

    TextView tvCoins;
    AppCompatButton btnWatchAd, btnWithdraw;
    FirebaseFirestore firestore;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_earn, container, false);

        tvCoins = v.findViewById(R.id.tvCoins);
        btnWatchAd = v.findViewById(R.id.btnWatchAd);
        btnWithdraw = v.findViewById(R.id.btnWithdraw);
        firestore = FirebaseFirestore.getInstance();

        tvCoins.setText(String.valueOf(user.getTotalCoins()));

        btnWatchAd.setEnabled(appControl.getEnableLoad());

        RewardedAdsListner myAdsListener = new RewardedAdsListner();
        UnityAds.addListener(myAdsListener);
        UnityAds.initialize(requireActivity(), appControl.getUnityRewarded(), appControl.getTestMode());

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnWatchAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UnityAds.isInitialized()) {
                    UnityAds.show(requireActivity(), appControl.getUnityRewarded());
                }
            }
        });

        return v;
    }

    private class RewardedAdsListner implements IUnityAdsListener {

        @Override
        public void onUnityAdsReady(String s) {

        }

        @Override
        public void onUnityAdsStart(String s) {

        }

        @Override
        public void onUnityAdsFinish(String s, UnityAds.FinishState finishState) {
            if (finishState.equals(UnityAds.FinishState.SKIPPED)) {
                Snackbar.make(btnWatchAd, "You have not completed the ad", Snackbar.LENGTH_LONG).show();
            } else if (finishState.equals(UnityAds.FinishState.COMPLETED)) {
                firestore.collection("user").document(loginuid).update("totalCoins", user.getTotalCoins() + 10);
            } else if (finishState.equals(UnityAds.FinishState.ERROR)) {
                Snackbar.make(btnWatchAd, UnityAds.FinishState.ERROR.toString(), Snackbar.LENGTH_LONG).show();
            }
        }

        @Override
        public void onUnityAdsError(UnityAds.UnityAdsError unityAdsError, String s) {
            Snackbar.make(btnWatchAd, unityAdsError.toString(), Snackbar.LENGTH_LONG).show();
        }
    }
}