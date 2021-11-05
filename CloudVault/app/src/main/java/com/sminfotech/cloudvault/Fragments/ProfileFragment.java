package com.sminfotech.cloudvault.Fragments;

import static com.sminfotech.cloudvault.MainActivity.appControl;
import static com.sminfotech.cloudvault.MainActivity.user;
import static com.sminfotech.cloudvault.SplashActivity.editor;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.sminfotech.cloudvault.LoginActivity;
import com.sminfotech.cloudvault.Profile.ImageOrVideoActivity;
import com.sminfotech.cloudvault.Profile.PanicSwitchActivity;
import com.sminfotech.cloudvault.Profile.PasswordActivity;
import com.sminfotech.cloudvault.Profile.VaultActivity;
import com.sminfotech.cloudvault.R;
import com.sminfotech.cloudvault.Unity.UnityAdsListner;
import com.unity3d.ads.IUnityAdsShowListener;
import com.unity3d.ads.UnityAds;


public class ProfileFragment extends Fragment {

    TextView tvProfileName, tvChangePin;
    LinearLayout llChangePin, llYourVault, llPanicSwitch, llSignOut;
    FirebaseAuth auth;
    GoogleSignInOptions gso;
    GoogleSignInClient mGoogleSignInClient;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileName = v.findViewById(R.id.tvProfileName);
        llChangePin = v.findViewById(R.id.llChangePin);
        tvChangePin = v.findViewById(R.id.tvChangePin);
        llYourVault = v.findViewById(R.id.llYourVault);
        llPanicSwitch = v.findViewById(R.id.llPanicSwitch);
        llSignOut = v.findViewById(R.id.llSignOut);

        auth = FirebaseAuth.getInstance();
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestIdToken(getString(R.string.web_client_id))
                .build();
        mGoogleSignInClient = GoogleSignIn.getClient(getContext(), gso);

        UnityAdsListner myAdsListener = new UnityAdsListner();
        UnityAds.addListener(myAdsListener);
        UnityAds.initialize(getContext(), appControl.getUnityID(), appControl.getTestMode());

        llYourVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), VaultActivity.class);
                startActivity(i);
//                if (UnityAds.isInitialized()) {
//                    UnityAds.show((Activity) getContext(), appControl.getUnityInterstitial(), new IUnityAdsShowListener() {
//                        @Override
//                        public void onUnityAdsShowFailure(String s, UnityAds.UnityAdsShowError unityAdsShowError, String s1) {
//                            Intent i = new Intent(getContext(), VaultActivity.class);
//                            Log.d("error1111",unityAdsShowError.toString());
//                            startActivity(i);
//                        }
//
//                        @Override
//                        public void onUnityAdsShowStart(String s) {
//                        }
//
//                        @Override
//                        public void onUnityAdsShowClick(String s) {
//
//                        }
//
//                        @Override
//                        public void onUnityAdsShowComplete(String s, UnityAds.UnityAdsShowCompletionState unityAdsShowCompletionState) {
//                            Intent i = new Intent(getContext(), VaultActivity.class);
//                            startActivity(i);
//                        }
//                    });
//                }
            }
        });

        llSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.confirmation_popup);
                dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                dialog.setCancelable(false);
                TextView confirmDelete = dialog.findViewById(R.id.confirmDelete);
                TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
                TextView popupHeading = dialog.findViewById(R.id.popupHeading);
                TextView popupBody = dialog.findViewById(R.id.popupBody);
                popupHeading.setText("Log out");
                popupBody.setText("Are you sure?\nYou want to logout?");
                confirmDelete.setText("Logout");
                dialog.show();
                confirmDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        auth.signOut();
                        mGoogleSignInClient.signOut();
                        Intent i = new Intent(getContext(), LoginActivity.class);
                        requireActivity().startActivity(i);
                        requireActivity().finish();
                        editor.putBoolean("isLoggedIn", false);
                        editor.putString("uid", "");
                        editor.commit();
                    }
                });
                cancelDialog.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

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