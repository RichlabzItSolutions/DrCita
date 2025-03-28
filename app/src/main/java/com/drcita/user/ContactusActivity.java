package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityContactusBinding;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.contact.ContactRequest;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ContactusActivity extends LanguageBaseActivity {

    private ActivityContactusBinding activityContactusBinding;
    private ProgressDialog progress;
    String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityContactusBinding = DataBindingUtil.setContentView(this, R.layout.activity_contactus);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        setSupportActionBar(activityContactusBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Contact us");
        activityContactusBinding.backContactus.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        activityContactusBinding.submitcontactus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });
    }

    private void validations() {
        if (activityContactusBinding.subject.getText().toString().equals("")) {
            Constants.ToastShort(ContactusActivity.this, "Enter Subject");
            return;
        }
        if (activityContactusBinding.decription.getText().toString().equals("")){
            Constants.ToastShort(ContactusActivity.this, "Enter Description");
            return;
        }
        callApi();
    }

    private void callApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ContactRequest request = new ContactRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setSubject(activityContactusBinding.subject.getText().toString());
            request.setDescription(activityContactusBinding.decription.getText().toString());
            ApiClient.getRestAPI().contactRequest(request).enqueue(new Callback<GlobalResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        contactRequestResponse(Objects.requireNonNull(response.body()));
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
                    Toast.makeText(ContactusActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(ContactusActivity.this);
        }
    }

    private void contactRequestResponse(GlobalResponse globalResponse) {
        dismissLoadingDialog();
        Toast.makeText(ContactusActivity.this, globalResponse.getMessage(), Toast.LENGTH_LONG).show();
        activityContactusBinding.subject.setText("");
        activityContactusBinding.decription.setText("");
    }



   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/
}