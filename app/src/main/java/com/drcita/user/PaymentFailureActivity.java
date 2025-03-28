package com.drcita.user;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;

import com.drcita.user.databinding.ActivityPaymentFailureBinding;

public class PaymentFailureActivity extends LanguageBaseActivity {

    private ActivityPaymentFailureBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_payment_failure);

        binding.backPaymentfailure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();;
            }
        });
        binding.tryagainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/
}