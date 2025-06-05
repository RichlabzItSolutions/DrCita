package com.drcita.user;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.drcita.user.common.Constants;
import com.drcita.user.common.CustomDailog;
import com.drcita.user.common.LanguageUtil;
import com.drcita.user.common.PreferenceUtil;

public class LanguageBaseActivity extends AppCompatActivity {

    private String defaultLang;
    private CustomDailog progress;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        defaultLang = LanguageUtil.currentLanguage(this);
        LanguageUtil.setLanguage(this, LanguageUtil.currentLanguage(this));
    }

    @Override
    protected void onResume() {
        super.onResume();
        String languageId = PreferenceUtil.getInstance().getString(Constants.LANGUAGE1);
        if (!defaultLang.equalsIgnoreCase(languageId)) {
            recreate();
            CaafisomApp.getContext().setLanguage();
        }
    }
    public void showLoadingDialog() {
        if (progress == null) {

            progress = new CustomDailog(this);
        }
        progress.show();
        progress.setCancelable(false);
    }

    public void dismissLoadingDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}
