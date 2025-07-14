package com.drcita.user.Activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.drcita.user.ForgotPasswordActivity;
import com.drcita.user.LanguageBaseActivity;
import com.drcita.user.LanguageEnum;
import com.drcita.user.R;
import com.drcita.user.SignupActivity;
import com.drcita.user.common.Constants;
import com.drcita.user.common.PreferenceUtil;
import com.drcita.user.databinding.ActivityChangePasswordBinding;
import com.drcita.user.databinding.ActivityChangemobilenumberBinding;
import com.drcita.user.models.login.ChangeMobileNumber;
import com.drcita.user.models.login.LoginRequest;
import com.drcita.user.models.login.LoginResponse;
import com.drcita.user.retrofit.ApiClient;
import com.marcinorlowski.fonty.Fonty;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangeMobileNumberActivity extends LanguageBaseActivity {

    private ActivityChangemobilenumberBinding activityChangemobilenumberBinding;
    private ProgressDialog progress;
    String selectedCountryCodes = "+91";

    String strMobile = "";

    String password = "";
    String countryCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityChangemobilenumberBinding = DataBindingUtil.setContentView(this, R.layout.activity_changemobilenumber);
        Fonty.setFonts(this);

//        activityLoginBinding.countryCodePicker.setCountryForNameCode("IN");


        activityChangemobilenumberBinding.signup.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
            startActivity(intent);
        });
//        activityLoginBinding.forgotpasswordTV.setOnClickListener(view -> {
//            Intent intent = new Intent(getApplicationContext(), ForgotPasswordActivity.class);
//            startActivity(intent);
//        });
        activityChangemobilenumberBinding.btnLogin.setOnClickListener(view -> {

            // countryCode = activityLoginBinding.countryCodePicker.getSelectedCountryCode();
            strMobile = activityChangemobilenumberBinding.edtMobile.getText().toString();

            validations(strMobile);
        });

//        activityChangemobilenumberBinding.txtTermsLink.setOnClickListener(view -> {
//            Intent intent = new Intent(ChangeMobileNumberActivity.this, TermsWebViewActivity.class);
//            startActivity(intent);
//        });

//
    }

    private void validations(String strMobile) {
        if (TextUtils.isEmpty(strMobile)) {
            activityChangemobilenumberBinding.edtMobile.setError("Enter Phone Number");

        }
       else if (strMobile.length() < 10) {
            activityChangemobilenumberBinding.edtMobile.setError(getString(R.string.please_enter_your_valid_mobile_number));
            return;
        }
       else if (!strMobile.matches("^[6-9]\\d{9}$")) {
           // Toast.makeText(LoginActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
            activityChangemobilenumberBinding.edtMobile.setError(getString( R.string.not_valid_mobile_number));
            return;
        }


        else {
            callApi();

        }

    }

    private void callApi() {
        showLoadingDialog();
//        FirebaseApp.initializeApp(this);

        if (Constants.haveInternet(getApplicationContext())) {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String userId = sp.getString(Constants.USER_ID, "");


            ChangeMobileNumber request = new ChangeMobileNumber();
            request.setMobile(activityChangemobilenumberBinding.edtMobile.getText().toString());
            request.setUserId(Integer.parseInt(userId));
//            request.setPassword(activityLoginBinding.loginPassword.getText().toString());
//            request.setDeviceToken(token);
            ApiClient.getRestAPI().changeMobile(request).enqueue(new Callback<LoginResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        loginResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<LoginResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(ChangeMobileNumberActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(ChangeMobileNumberActivity.this);
        }
    }
    private void loginResponse(LoginResponse loginResponse) {
            if (loginResponse.isSuccess()){
                dismissLoadingDialog();
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                preferences.edit().putString(Constants.USER_ID, loginResponse.getData().getUserId()).apply();
                preferences.edit().putString(Constants.NAME, loginResponse.getData().getName()).apply();
                preferences.edit().putString(Constants.MOBILE, loginResponse.getData().getMobile()).apply();
                preferences.edit().putInt(Constants.PROFILESTATUS, loginResponse.getData().getProfile_status()).apply();
                preferences.edit().putInt(Constants.REGION, (loginResponse.getData().getRegion())).apply();
                preferences.edit().putString(Constants.LANGUAGE, String.valueOf(loginResponse.getData().getLanguage())).apply();
                Log.e("TAG", "loginResponse: "+loginResponse.getData().getLanguage() );
                Log.e("TAG", "loginResponse: "+ LanguageEnum.getByValue(loginResponse.getData().getLanguage()) );
                if(LanguageEnum.getByValue(loginResponse.getData().getLanguage())!=null) {
                    PreferenceUtil.getInstance().save(Constants.LANGUAGE1, getString(LanguageEnum.getByValue(loginResponse.getData().getLanguage()).getName()));
                }
                if (loginResponse.getData().getProfile_status()==1){
                    Intent intent = new Intent(getApplicationContext(), NewVerifyOtpActivity.class);
                    intent.putExtra("mobile",activityChangemobilenumberBinding.edtMobile.getText().toString());
                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(getApplicationContext(), NewVerifyOtpActivity.class);
                    intent.putExtra("mobile",activityChangemobilenumberBinding.edtMobile.getText().toString());
                    startActivity(intent);
                    Toast.makeText(ChangeMobileNumberActivity.this,loginResponse.getMessage()+"-"+loginResponse.getData().getOtp(),Toast.LENGTH_LONG).show();
                }


            }
    }




}