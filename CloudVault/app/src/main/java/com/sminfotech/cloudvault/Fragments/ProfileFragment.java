package com.sminfotech.cloudvault.Fragments;

import static com.sminfotech.cloudvault.MainActivity.user;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sminfotech.cloudvault.Model.User;
import com.sminfotech.cloudvault.R;


public class ProfileFragment extends Fragment {

    TextView tvProfileName;
//    User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profile, container, false);

//        user = new User();

        tvProfileName = v.findViewById(R.id.tvProfileName);

        tvProfileName.setText(user.getName());

        return v;
    }
}