package com.sminfotech.cloudvault;

import static androidx.core.content.ContextCompat.getSystemService;
import static com.sminfotech.cloudvault.MainActivity.appControl;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;

import com.sminfotech.cloudvault.Model.AppControl;

import java.util.Objects;

public class AppConstants {

    Context context;

    public static Boolean testMode = appControl.getTestMode();
    public static String unityGameID = appControl.getUnityID();
    public static String interstitial_android = appControl.getUnityInterstitial();
    public static Boolean enableLoad = appControl.getEnableLoad();
    public static String banner_android = appControl.getUnityBanner();
    public static String fb_banner_placement = appControl.getFacebookBanner();
    public static String admob_banner = appControl.getAdmobBanner();


}
