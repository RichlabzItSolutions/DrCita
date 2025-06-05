package com.drcita.user;

import android.app.Application;
import android.content.res.Configuration;

import com.androidnetworking.AndroidNetworking;
import com.drcita.user.common.AppSignatureHelper;
import com.drcita.user.common.LanguageUtil;
import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.FirebaseApp;
import com.marcinorlowski.fonty.Fonty;

import live.videosdk.rtc.android.VideoSDK;

public class CaafisomApp extends Application {
    private static CaafisomApp context;

    @Override
    public void onCreate() {
        super.onCreate();
        AppSignatureHelper appSignatureHelper = new AppSignatureHelper(this);
        appSignatureHelper.getAppSignatures();
        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        FirebaseApp.initializeApp(this);
        VideoSDK.initialize(getApplicationContext());
        AndroidNetworking.initialize(getApplicationContext());
        context = this;

        Fonty.context(this)
                .normalTypeface("jakartharegular.ttf")
                .italicTypeface("jakarthasemibold.ttf")
                .boldTypeface("jakarthabold.ttf")
                .build();


        setLanguage();
    }

    public static CaafisomApp getContext() {
        return context;
    }

    public void setLanguage(){
        LanguageUtil.setLanguage(this, LanguageUtil.currentLanguage(this));
        this.context = this;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        setLanguage();
    }
}
