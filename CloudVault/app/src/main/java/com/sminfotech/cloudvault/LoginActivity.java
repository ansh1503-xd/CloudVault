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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.sminfotech.cloudvault.Model.User;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    LinearLayout btnLogin;
    EditText etEmail, etPass;
    FirebaseAuth auth;
    LinearLayout signupIntent, tvForgotPass;
    ImageView googleSignIn;
    GoogleSignInClient mGoogleSignInClient;
    GoogleSignInOptions gso;
    FirebaseUser firebaseUser;
    FirebaseFirestore firestore;
    User user;
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
        firestore = FirebaseFirestore.getInstance();



        tvForgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(i);
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
                            firebaseUser = auth.getCurrentUser();
                            editor.putBoolean("isLoggedIn", true);
                            editor.putString("uid", firebaseUser.getUid());
                            editor.commit();
                            Snackbar.make(view, "Login success", Snackbar.LENGTH_SHORT).show();
                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();
                            dialog.dismiss();
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
                Snackbar.make(googleSignIn, e.getLocalizedMessage(), Snackbar.LENGTH_LONG).show();
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
        TextView tvText = dialog.findViewById(R.id.tvText);
        tvText.setText("Signing in\nwith Google...");
        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
        auth.signInWithCredential(credential).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                dialog.dismiss();
                firebaseUser = auth.getCurrentUser();

                firestore.collection("user").document(firebaseUser.getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value != null) {
                            user = new User();
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

                Map<String, Object> userData = new HashMap<>();
                if (user!=null){
                    userData.put("uid", firebaseUser.getUid());
                    userData.put("inAppPassword", "");
                    userData.put("notesList", Arrays.asList());
                    userData.put("documentsList", Arrays.asList());
                    userData.put("audioList", Arrays.asList());
                    userData.put("email", firebaseUser.getEmail());
                    userData.put("fullName", firebaseUser.getDisplayName());
                    userData.put("panicSwitch", false);
                    userData.put("usedDataQuota", 0);
                    userData.put("totalDataQuota", 100);
                    userData.put("totalCoins", 100);
                }else{
                    userData.put("uid", firebaseUser.getUid());
                    userData.put("inAppPassword", "");
                    userData.put("notesList", Arrays.asList());
                    userData.put("documentsList", Arrays.asList());
                    userData.put("audioList", Arrays.asList());
                    userData.put("email", firebaseUser.getEmail());
                    userData.put("fullName", firebaseUser.getDisplayName());
                    userData.put("panicSwitch", false);
                    userData.put("usedDataQuota", 0);
                    userData.put("totalDataQuota", 100);
                    userData.put("totalCoins", 100);
                }
                if(user.getImageList().size()>0){
                    userData.put("imageList", Arrays.asList(user.getImageList()));
                }else{
                    userData.put("imageList", Arrays.asList());
                }

                if (user.getVideoList().size()>0){
                    userData.put("videoList", Arrays.asList(user.getVideoList()));
                }else {
                    userData.put("videoList", Arrays.asList());
                }

                DocumentReference ref = firestore.collection("user").document(firebaseUser.getUid());
                ref.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Snackbar.make(googleSignIn, "Logged in successfully", Snackbar.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                });
//                ref.addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
//                        if (value.exists()){
//                            if (!value.get("uid").equals(firebaseUser.getUid())) {
//
//                            }
//                        }else{
//                            ref.set(userData).addOnSuccessListener(new OnSuccessListener<Void>() {
//                                @Override
//                                public void onSuccess(Void unused) {
//                                    Snackbar.make(googleSignIn, "Logged in successfully", Snackbar.LENGTH_SHORT).show();
//                                    dialog.dismiss();
//                                }
//                            });
//                        }
//                    }
//                });

                editor.putBoolean("isLoggedIn", true);
                editor.putString("uid", firebaseUser.getUid());
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
        dialog.show();
    }
}