package com.sminfotech.cloudvault;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignupActivity extends AppCompatActivity {

    FirebaseAuth auth;
    LinearLayout btnSignup;
    EditText etEmail, etPass, etConPass;
    LinearLayout loginIntent;
    ImageView backToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        btnSignup = findViewById(R.id.btnSignup);
        etEmail = findViewById(R.id.etEmailSignup);
        etPass = findViewById(R.id.etPassSignup);
        etConPass = findViewById(R.id.etConPassSignup);
        btnSignup = findViewById(R.id.btnSignup);
        loginIntent = findViewById(R.id.loginIntent);
        backToLogin = findViewById(R.id.backToLogin);
        auth = FirebaseAuth.getInstance();

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {

                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                String conPass = etConPass.getText().toString();
                Dialog dialog = new Dialog(SignupActivity.this);

                if (!email.isEmpty() && !pass.isEmpty() && !conPass.isEmpty()) {
                    if (pass.equals(conPass)) {

                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.loading_dialog);
                        dialog.getWindow().setElevation(15f);
                        dialog.setCancelable(false);
                        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                        dialog.show();
                        TextView tvText = dialog.findViewById(R.id.tvText);
                        tvText.setText("Creating\naccount...");
                        auth.createUserWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Snackbar.make(view, "Account created successfully", Snackbar.LENGTH_SHORT).show();
                                dialog.dismiss();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                                dialog.dismiss();
                            }
                        });
                    } else {
                        Snackbar.make(view, "Password mismatch", Snackbar.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(view, "Enter complete details...", Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }
}