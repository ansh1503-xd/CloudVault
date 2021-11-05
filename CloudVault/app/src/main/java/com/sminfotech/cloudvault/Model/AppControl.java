package com.sminfotech.cloudvault.Model;

public class AppControl {

    public AppControl() {
    }

    String admobBanner;
    String facebookAppID;
    String facebookBanner;
    Boolean testMode;
    String unityBanner;
    String unityID;
    String unityInterstitial;
    Boolean enableLoad;
    String unityRewarded;

    public Boolean getEnableLoad() {
        return enableLoad;
    }

    public void setEnableLoad(Boolean enableLoad) {
        this.enableLoad = enableLoad;
    }

    public String getAdmobBanner() {
        return admobBanner;
    }

    public void setAdmobBanner(String admobBanner) {
        this.admobBanner = admobBanner;
    }

    public String getFacebookAppID() {
        return facebookAppID;
    }

    public void setFacebookAppID(String facebookAppID) {
        this.facebookAppID = facebookAppID;
    }

    public String getFacebookBanner() {
        return facebookBanner;
    }

    public void setFacebookBanner(String facebookBanner) {
        this.facebookBanner = facebookBanner;
    }

    public Boolean getTestMode() {
        return testMode;
    }

    public void setTestMode(Boolean testMode) {
        this.testMode = testMode;
    }

    public String getUnityBanner() {
        return unityBanner;
    }

    public void setUnityBanner(String unityBanner) {
        this.unityBanner = unityBanner;
    }

    public String getUnityID() {
        return unityID;
    }

    public void setUnityID(String unityID) {
        this.unityID = unityID;
    }

    public String getUnityInterstitial() {
        return unityInterstitial;
    }

    public void setUnityInterstitial(String unityInterstitial) {
        this.unityInterstitial = unityInterstitial;
    }

    public String getUnityRewarded() {
        return unityRewarded;
    }

    public void setUnityRewarded(String unityRewarded) {
        this.unityRewarded = unityRewarded;
    }
}
