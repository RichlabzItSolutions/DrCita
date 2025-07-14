package com.drcita.user;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

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
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.setStatusBarColor(Color.parseColor("#2A9145")); // or your color
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//            );
//        }
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//            // Set light status bar (dark icons)
//            getWindow().getDecorView().setSystemUiVisibility(
//                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
//            );
//        }
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
