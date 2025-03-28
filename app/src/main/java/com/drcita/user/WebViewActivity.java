package com.drcita.user;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;

import com.drcita.user.databinding.ActivityWebViewBinding;

public class WebViewActivity extends AppCompatActivity {

   ActivityWebViewBinding binding;
    WebSettings webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityWebViewBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);
        binding.mainWebView.getSettings().setJavaScriptEnabled(true);
        binding.mainWebView.clearHistory();
        binding.mainWebView.clearFormData();
        binding.mainWebView.clearCache(true);
        webview = binding.mainWebView.getSettings();
        webview.setCacheMode(WebSettings.LOAD_NO_CACHE);
        try {
            if (getIntent() != null) {
                String url = getIntent().getStringExtra("url");

                if (url!= null) {
                    binding.mainWebView.loadUrl(url);
                }
            }
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }
}