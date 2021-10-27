package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.user;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.Model.User;
import com.sminfotech.cloudvault.R;

import java.util.Objects;

public class PasswordActivity extends AppCompatActivity {

    TextView one, two, three, four, five, six, seven, eight, nine, zero;
    PinView pvPassword;
    ImageView clearText;
    String inAppPassword;
    String passType;
    Boolean isPasswordTrue = false;
    Boolean isPasswordEnteredFirstTime = true;
    Boolean oldPassword = true;
    String newPassword;
    TextView tvHint;
    Boolean isNewPassEntered = true;
    FirebaseFirestore firestore;
    ImageView backToProfile;
    Animation shake;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        one = findViewById(R.id.one);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);
        five = findViewById(R.id.five);
        six = findViewById(R.id.six);
        seven = findViewById(R.id.seven);
        eight = findViewById(R.id.eight);
        nine = findViewById(R.id.nine);
        zero = findViewById(R.id.zero);
        tvHint = findViewById(R.id.tvHint);
        pvPassword = findViewById(R.id.pvPassword);
        clearText = findViewById(R.id.clearText);
        backToProfile = findViewById(R.id.backToProfile);
        inAppPassword = user.getInAppPassword();
        firestore = FirebaseFirestore.getInstance();
        shake = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.shake);

        passType = getIntent().getStringExtra("type");

        if (passType.equals("change")){
            tvHint.setText("Enter old PIN");
        }else{
            tvHint.setText("Set PIN");
        }

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("1");
            }
        });

        two.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("2");
            }
        });

        three.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("3");
            }
        });

        four.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("4");
            }
        });

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("5");
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("6");
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("7");
            }
        });

        eight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("8");
            }
        });

        nine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("9");
            }
        });

        zero.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.append("0");
            }
        });

        clearText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvPassword.setText("");
            }
        });

        pvPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (passType.equals("change")) {
                    if (pvPassword.getText().toString().length() == 4) {
                        if (oldPassword) {
                            if (inAppPassword.equals(pvPassword.getText().toString())) {
                                pvPassword.setText("");
                                isPasswordTrue = true;
                                oldPassword = false;
                                tvHint.setText("Set up new PIN");
                            } else {
                                isPasswordTrue = false;
                                pvPassword.setText("");
                                pvPassword.setAnimationEnable(true);
                                pvPassword.setAnimation(shake);
                                Snackbar.make(pvPassword, "Wrong PIN", Snackbar.LENGTH_LONG).show();
                            }
                        } else {
                            if (isPasswordTrue) {
                                if (isPasswordEnteredFirstTime) {
                                    newPassword = pvPassword.getText().toString();
                                    pvPassword.setText("");
                                    tvHint.setText("Re-enter new PIN");
                                    isPasswordEnteredFirstTime = false;
                                } else {
                                    if (pvPassword.getText().toString().equals(newPassword)) {
                                        firestore.collection("user").document(loginuid).update("inAppPassword", newPassword)
                                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        Snackbar.make(pvPassword, "PIN Changed succesfully...", Snackbar.LENGTH_LONG).show();
                                                        pvPassword.setText("");
                                                        onBackPressed();
                                                    }
                                                });
                                    } else {
                                        Snackbar.make(pvPassword, "PIN not matched", Snackbar.LENGTH_LONG).show();
                                        isPasswordEnteredFirstTime = true;
                                        pvPassword.setText("");
                                        tvHint.setText("Set up new PIN");

                                    }
                                }

                            }
                        }

                    }
                } else if (passType.equals("set")) {
                    if (pvPassword.getText().toString().length() == 4) {
                        if (isNewPassEntered) {
                            newPassword = pvPassword.getText().toString();
                            pvPassword.setText("");
                            tvHint.setText("Re-enter PIN");
                            isNewPassEntered = false;
                        } else {
                            if (pvPassword.getText().toString().equals(newPassword)) {
                                firestore.collection("user").document(loginuid).update("inAppPassword", newPassword)
                                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                Snackbar.make(pvPassword, "PIN Changed succesfully...", Snackbar.LENGTH_LONG).show();
                                                pvPassword.setText("");
                                                onBackPressed();
                                            }
                                        });
                            } else {
                                Snackbar.make(pvPassword, "PIN not matched", Snackbar.LENGTH_LONG).show();
                                isPasswordEnteredFirstTime = true;
                                pvPassword.setText("");
                                tvHint.setText("Set PIN");

                            }
                        }

                    }
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

    }
}