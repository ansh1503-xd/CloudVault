package com.sminfotech.cloudvault.Fragments;

import static com.sminfotech.cloudvault.MainActivity.user;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sminfotech.cloudvault.Profile.PasswordActivity;
import com.sminfotech.cloudvault.Profile.VaultActivity;
import com.sminfotech.cloudvault.R;


public class ProfileFragment extends Fragment {

    TextView tvProfileName, tvChangePin;
    LinearLayout llChangePin, llYourVault;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileName = v.findViewById(R.id.tvProfileName);
        llChangePin = v.findViewById(R.id.llChangePin);
        tvChangePin = v.findViewById(R.id.tvChangePin);
        llYourVault = v.findViewById(R.id.llYourVault);



        llYourVault.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), VaultActivity.class);
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