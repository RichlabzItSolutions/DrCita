package com.drcita.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityBookanambulanceBinding;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.ambulance.AmbulanceRequest;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookanambulanceActivity extends LanguageBaseActivity {

    private ActivityBookanambulanceBinding binding;
    private String userId;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_bookanambulance);

        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Book an Ambulance");
        binding.backBookanambulance.setOnClickListener(view -> finish());
        binding.submitbookanambulance.setOnClickListener(view -> validations());

        binding.callView.setOnClickListener(view -> {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + binding.callView.getText()));
            startActivity(intent);
        });
    }

    private void validations() {
        if (binding.bookanambulancename.getText().toString().equals("")) {
            Constants.ToastShort(BookanambulanceActivity.this, "Enter Name");
            return;
        }
        if (binding.bookanambulancename.getText().toString().length() < 3) {
            Constants.ToastShort(BookanambulanceActivity.this, "Name length should be 3");
            return;
        }
        if (binding.bookanambulancenumber.getText().toString().equals("")) {
            Constants.ToastShort(BookanambulanceActivity.this, "Enter Phone Number");
            return;
        }


        if (!(binding.bookanambulancenumber.getText().toString().matches(Constants.MobilePattern)) &&(!(binding.bookanambulancenumber.getText().toString().matches(Constants.MobilePattern1)))) {
            Constants.ToastShort(BookanambulanceActivity.this, Constants.enter_valid_mobile_no);
            return;
        }

        if (binding.bookanambulancedescription.getText().toString().equals("")) {
            Constants.ToastShort(BookanambulanceActivity.this, "Enter Description");
            return;
        }
        callApi();
    }

    private void callApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            AmbulanceRequest request = new AmbulanceRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setName(binding.bookanambulancename.getText().toString());
            request.setMobile(binding.bookanambulancenumber.getText().toString());
            request.setDescription(binding.bookanambulancedescription.getText().toString());
            ApiClient.getRestAPI().requestAmbulance(request).enqueue(new Callback<GlobalResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        bookanAmbulanceResponse(Objects.requireNonNull(response.body()));
                    } else {
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GlobalResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(BookanambulanceActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(BookanambulanceActivity.this);
        }
    }

    private void bookanAmbulanceResponse(GlobalResponse globalResponse) {
        dismissLoadingDialog();
        Toast.makeText(BookanambulanceActivity.this, globalResponse.getMessage(), Toast.LENGTH_LONG).show();
        binding.bookanambulancename.setText("");
        binding.bookanambulancenumber.setText("");
        binding.bookanambulancedescription.setText("");


    }


}