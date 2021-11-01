package com.sminfotech.cloudvault.Profile;

import static com.sminfotech.cloudvault.MainActivity.editor;
import static com.sminfotech.cloudvault.MainActivity.isPanicSwitchOn;
import static com.sminfotech.cloudvault.MainActivity.loginuid;
import static com.sminfotech.cloudvault.MainActivity.manager;
import static com.sminfotech.cloudvault.MainActivity.user;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;

import com.google.firebase.firestore.FirebaseFirestore;
import com.sminfotech.cloudvault.R;

import java.util.Objects;

public class PanicSwitchActivity extends AppCompatActivity {

    ImageView backToProfile;
    SwitchCompat panicSwitch;
    FirebaseFirestore firestore;
//    SensorManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panic_switch);

        backToProfile = findViewById(R.id.backToProfile);
        panicSwitch = findViewById(R.id.panicSwitch);
        firestore = FirebaseFirestore.getInstance();

//        manager = (SensorManager) getSystemService(SENSOR_SERVICE);

        panicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (panicSwitch.isChecked()) {
                    firestore.collection("user").document(loginuid).update("panicSwitch", true);
                    editor.putBoolean("isPanicSwitchOn", true);
//                    isPanicSwitchOn = true;
                } else {
                    firestore.collection("user").document(loginuid).update("panicSwitch", false);
                    editor.putBoolean("isPanicSwitchOn", false);
//                    isPanicSwitchOn = false;false;
                }
                editor.commit();
            }
        });

        destroyAppWhenShake(manager, PanicSwitchActivity.this);

        if (user.getPanicSwitch()) {
            panicSwitch.setChecked(true);
        } else {
            panicSwitch.setChecked(false);
        }

        backToProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

    }

    public static void destroyAppWhenShake(SensorManager manager, Activity activity) {
        final float[] mAccel = new float[1];
        final float[] mAccelCurrent = new float[1];
        final float[] mAccelLast = new float[1];

        if (isPanicSwitchOn){
            Objects.requireNonNull(manager).registerListener(
                    new SensorEventListener() {
                        @Override
                        public void onSensorChanged(SensorEvent sensorEvent) {
                            mAccel[0] = 10f;
                            mAccelCurrent[0] = SensorManager.GRAVITY_EARTH;
                            mAccelLast[0] = SensorManager.GRAVITY_EARTH;
                            float x = sensorEvent.values[0];
                            float y = sensorEvent.values[1];
                            float z = sensorEvent.values[2];
                            mAccelLast[0] = mAccelCurrent[0];
                            mAccelCurrent[0] = (float) Math.sqrt((double) (x * x + y * y + z * z));
                            float delta = mAccelCurrent[0] - mAccelLast[0];
                            mAccel[0] = mAccel[0] * 0.9f + delta;
                            if (mAccel[0] > 12) {
                                Toast.makeText(activity, "Shake event detected", Toast.LENGTH_SHORT).show();
                                activity.onBackPressed();
                            }
                        }

                        @Override
                        public void onAccuracyChanged(Sensor sensor, int i) {

                        }
                    }, manager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                    SensorManager.SENSOR_DELAY_NORMAL);
        }

    }


}