package com.drcita.user.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.databinding.DataBindingUtil;

import com.drcita.user.LanguageBaseActivity;
import com.drcita.user.R;
import com.drcita.user.databinding.ActivityAboutusBinding;

public class TermsWebViewActivity extends LanguageBaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        WebView webView = new WebView(this);
        setContentView(webView);

        webView.setWebViewClient(new WebViewClient());
        webView.getSettings().setJavaScriptEnabled(true);

        // Replace this URL with your actual Terms & Conditions URL
        webView.loadUrl("https://yourwebsite.com/terms-and-conditions");
    }
}