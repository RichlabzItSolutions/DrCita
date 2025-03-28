package com.drcita.user;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.databinding.ActivityCallBinding;

public class CallActivity extends AppCompatActivity {

    private ActivityCallBinding viewDataBinding;
    private String CUSTOMER_CARE_MOBILE_NO = "+91-9491363419";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewDataBinding = DataBindingUtil.setContentView(this, R.layout.activity_call);
        viewDataBinding.callBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + CUSTOMER_CARE_MOBILE_NO));
                startActivity(callIntent);
            }
        });
    }
}