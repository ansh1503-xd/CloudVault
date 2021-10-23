package com.sminfotech.cloudvault;

import static com.sminfotech.cloudvault.SplashActivity.editor;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.identity.SignInCredential;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

public class LoginActivity extends AppCompatActivity {

    LinearLayout btnLogin;
    EditText etEmail, etPass;
    FirebaseAuth auth;
    LinearLayout signupIntent, tvForgotPass;
    ImageView googleSignIn;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    FirebaseUser user;
    int GOOGLE_SIGN_IN_CODE = 111;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        btnLogin = findViewById(R.id.btnLogin);
        etEmail = findViewById(R.id.etEmail);
        etPass = findViewById(R.id.etPass);
        signupIntent = findViewById(R.id.signupIntent);
        googleSignIn = findViewById(R.id.googleSignIn);
        tvForgotPass = findViewById(R.id.tvForgotPass);
        auth = FirebaseAuth.getInstance();


        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                mGoogleSignInClient.signOut();
            }
        });

        signupIntent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, SignupActivity.class);
                startActivity(i);
            }
        });

        googleSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestEmail()
                        .requestIdToken(getString(R.string.web_client_id))
                        .build();

                mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this, gso);

                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, GOOGLE_SIGN_IN_CODE);
                GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(LoginActivity.this);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP_MR1)
            @Override
            public void onClick(View view) {
                String email = etEmail.getText().toString();
                String pass = etPass.getText().toString();
                Dialog dialog = new Dialog(LoginActivity.this);
                if (!email.isEmpty() && !pass.isEmpty()) {
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.loading_dialog);
                    dialog.getWindow().setElevation(15f);
                    dialog.setCancelable(false);
                    dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    dialog.show();
                    TextView tvText = dialog.findViewById(R.id.tvText);
                    tvText.setText("Logging in...");

                    auth.signInWithEmailAndPassword(email, pass).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override
                        public void onSuccess(AuthResult authResult) {
                            dialog.dismiss();
                            user = auth.getCurrentUser();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("uid", user.getUid());
                            editor.commit();
                            Snackbar.make(view, "Login success", Snackbar.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            dialog.dismiss();
                            editor.putBoolean("isLoggedIn", false);
                            editor.commit();
                            Snackbar.make(view, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
                        }
                    });
                } else {
                    Snackbar.make(view, "Email/Password is empty", Snackbar.LENGTH_SHORT).show();
                }

            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GOOGLE_SIGN_IN_CODE) {
            try {
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                GoogleSignInAccount account = task.getResult(ApiException.class);
                String idToken = account.getIdToken();
                authWithGoogle(idToken);
            } catch (ApiException e) {
                Snackbar.make(googleSignIn, "Login failed", Snackbar.LENGTH_LONG).show();
            }
        }

    }

    private void authWithGoogle(String idToken) {
        Dialog dialog = new Dialog(LoginActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.loading_dialog);
        dialog.setCancelable(false);
        dialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        dialog.show();
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                dialog.dismiss();
                user = auth.getCurrentUser();
                editor.putBoolean("isLoggedIn", true);
                editor.putString("uid", user.getUid());
                editor.commit();
//                Snackbar.make(googleSignIn, "Login success", Snackbar.LENGTH_LONG).show();
                Toast.makeText(LoginActivity.this, "Login success", Toast.LENGTH_SHORT).show();
                Intent i = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                dialog.dismiss();
                Snackbar.make(googleSignIn, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
            }
        });
    }
}