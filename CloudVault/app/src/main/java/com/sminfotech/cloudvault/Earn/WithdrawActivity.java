package com.sminfotech.cloudvault.Earn;

import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.user;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.R;

import java.util.HashMap;
import java.util.Map;

public class WithdrawActivity extends AppCompatActivity {

    ImageView back;
    LinearLayout btnWithdraw;
    EditText etEmailWithdraw, etNameWithdraw, etMobileWithdraw, etWithdrawalCoin;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdraw);

        back = findViewById(R.id.back);
        btnWithdraw = findViewById(R.id.btnWithdraw);
        etEmailWithdraw = findViewById(R.id.etEmailWithdraw);
        etNameWithdraw = findViewById(R.id.etNameWithdraw);
        etMobileWithdraw = findViewById(R.id.etMobileWithdraw);
        etWithdrawalCoin = findViewById(R.id.etWithdrawalCoin);
        firestore = FirebaseFirestore.getInstance();

        etEmailWithdraw.setText(user.getEmail());
        etNameWithdraw.setText(user.getName());

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        btnWithdraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int wCoin = Integer.parseInt(etWithdrawalCoin.getText().toString());
                if (wCoin>user.getTotalCoins()){
                    Dialog dialog = new Dialog(WithdrawActivity.this);
                    dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    dialog.setContentView(R.layout.alert_popup);
                    dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                    dialog.setCancelable(false);
                    TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
                    TextView popupHeading = dialog.findViewById(R.id.popupHeading);
                    TextView popupBody = dialog.findViewById(R.id.popupBody);
                    popupHeading.setText("Alert!");
                    popupBody.setText("Your coin balance is only "+user.getTotalCoins()+"\nWithdrawal amount should be smaller than"+user.getTotalCoins());
                    cancelDialog.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }else{
                    DocumentReference ref = firestore.collection("withdrawals").document();

                    Map<String, Object> withdrawMap = new HashMap<>();
                    withdrawMap.put("isApproved", false);
                    withdrawMap.put("isPayed", false);
                    withdrawMap.put("payedAt", 0);
                    withdrawMap.put("phoneNo", etMobileWithdraw.getText().toString());
                    withdrawMap.put("requestedAt", Double.valueOf(System.currentTimeMillis() / 1000.000));
                    withdrawMap.put("uid", loginuid);
                    withdrawMap.put("userName", etNameWithdraw.getText().toString());
                    withdrawMap.put("withdrawId", ref.getId());
                    withdrawMap.put("withdrawalAmount", Integer.parseInt(etWithdrawalCoin.getText().toString()));

                    ref.set(withdrawMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {

                            Dialog dialog = new Dialog(WithdrawActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setContentView(R.layout.alert_popup);
                            dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                            dialog.setCancelable(false);
                            TextView cancelDialog = dialog.findViewById(R.id.cancelDialog);
                            TextView popupHeading = dialog.findViewById(R.id.popupHeading);
                            TextView popupBody = dialog.findViewById(R.id.popupBody);
                            popupHeading.setText("Success");
                            popupBody.setText("Withdrawal successfull\n\nYou will get your amount with in 24 hours");
                            cancelDialog.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    firestore.collection("user").document(loginuid)
                                            .update("totalCoins", user.getTotalCoins()-wCoin)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {
                                                    dialog.dismiss();
                                                    onBackPressed();
                                                }
                                            });
                                }
                            });
                            dialog.show();
                        }
                    });

                }
            }
        });

    }
}