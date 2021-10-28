package com.sminfotech.cloudvault.Unity;

import android.widget.Toast;

import com.unity3d.services.banners.BannerErrorInfo;
import com.unity3d.services.banners.BannerView;

public class UnityBannerListener implements BannerView.IListener {
    @Override
    public void onBannerLoaded(BannerView bannerView) {
        Toast.makeText(bannerView.getContext(), "Loaded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerClick(BannerView bannerView) {
        Toast.makeText(bannerView.getContext(), "Clicked", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerFailedToLoad(BannerView bannerView, BannerErrorInfo bannerErrorInfo) {
        Toast.makeText(bannerView.getContext(), bannerErrorInfo.errorMessage, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBannerLeftApplication(BannerView bannerView) {
        Toast.makeText(bannerView.getContext(), "Left", Toast.LENGTH_SHORT).show();
    }
}
