package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityForgotPasswordBinding;
import com.drcita.user.models.forgotpassword.ForgotPasswordRequest;
import com.drcita.user.models.forgotpassword.ForgotPasswordResponse;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ForgotPasswordActivity extends LanguageBaseActivity {

    private ActivityForgotPasswordBinding activityForgotPasswordBinding;
    private ProgressDialog progress;
    String selectedCountryCodes = "+91";

    String strMobile = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityForgotPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_forgot_password);

        activityForgotPasswordBinding.countryCodePicker.setCountryForNameCode("IN");

      //  String selectedCountryCodes = activityForgotPasswordBinding.countryCodePicker.getSelectedCountryCodeWithPlus();

        activityForgotPasswordBinding.countryCodePicker.setOnCountryChangeListener(() -> {
            selectedCountryCodes = activityForgotPasswordBinding.countryCodePicker.getSelectedCountryCodeWithPlus();
            Log.d("fghj", selectedCountryCodes);

            if (selectedCountryCodes.equals("+91")) {


                setEditTextMaxLength(10); // India: 10 digits
            } else if (selectedCountryCodes.equals("+971")) {
                setEditTextMaxLength(9); // UAE: 9 digits
            }
        });

        activityForgotPasswordBinding.forgotpswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                strMobile = activityForgotPasswordBinding.forgotNumber.getText().toString();

               /* if (activityForgotPasswordBinding.forgotNumber.getText().toString().equals("")){
                    Constants.ToastShort(ForgotPasswordActivity.this, "Enter Phone Number");
                    return;
                }*/
              /*  if (!(activityForgotPasswordBinding.forgotNumber.getText().toString().matches(Constants.MobilePattern))) {
                    Constants.ToastShort(ForgotPasswordActivity.this, Constants.enter_valid_mobile_no);
                    return;
                }*/


                if (selectedCountryCodes.equals("+91")) {
                    if (strMobile.length() < 10 || strMobile.isEmpty()){
                        Toast.makeText(ForgotPasswordActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
                    }
                    if (!(Integer.valueOf(strMobile.substring(0, 1)) > 5)){
                        Toast.makeText(ForgotPasswordActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
                    }
                }
                else {

                    if (strMobile.length() < 9){
                        Toast.makeText(ForgotPasswordActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
                    }
                    if (!(strMobile.charAt(0) == '5')) {
                        Toast.makeText(ForgotPasswordActivity.this, "Invalid UAE number! Must start with 5.", Toast.LENGTH_SHORT).show();
                    }

                }

               /*  if (selectedCountryCodes.equals("+971")){
                    if (strMobile.length() < 9){
                        Toast.makeText(ForgotPasswordActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
                    }
                    if (!(Integer.valueOf(strMobile.substring(0, 1)) > 5)){
                        Toast.makeText(ForgotPasswordActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
                    }
                }

                else {
                  //  callApi();

                }*/


               /* boolean isValid = false;
                String errorMsg = "Invalid phone number";

                if (selectedCountryCodes.equals("+91")) {
                    // Validate Indian number (10 digits, starts with 6-9)
                    isValid = Pattern.matches("^[6-9]\\d{9}$", strMobile);
                    errorMsg = "Invalid Indian number! Should be 10 digits and start with 6-9.";
                } else if (selectedCountryCodes.equals("+971")) {
                    // Validate UAE number (9 digits, starts with 50, 52, 54, 55, 56, 58)
                    isValid = Pattern.matches("^(50|52|54|55|56|58)\\d{7}$", strMobile);
                    errorMsg = "Invalid UAE number! Should be 9 digits and start with 50, 52, 54, 55, 56, 58.";

                }

                if (isValid) {
                    //Toast.makeText(this, "Valid number: " + selectedCountryCodes + " " + strMobile, Toast.LENGTH_LONG).show();

                } else {
                    activityForgotPasswordBinding.forgotNumber.setError(errorMsg);
                    return;
                }*/

                callApi();

            }
        });
    }

    private void callApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ForgotPasswordRequest request = new ForgotPasswordRequest();
            request.setMobile(activityForgotPasswordBinding.forgotNumber.getText().toString());
            ApiClient.getRestAPI().forgotPassword(request).enqueue(new Callback<ForgotPasswordResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<ForgotPasswordResponse> call, @NonNull Response<ForgotPasswordResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        forgotPasswordResponse(Objects.requireNonNull(response.body()));
                    } else {
                        dismissLoadingDialog();
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
                @Override
                public void onFailure(@NonNull Call<ForgotPasswordResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(ForgotPasswordActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(ForgotPasswordActivity.this);
        }
    }
    private void forgotPasswordResponse(ForgotPasswordResponse forgotPasswordResponse) {
        if (forgotPasswordResponse.getStatus().equals("success")){
            dismissLoadingDialog();
            Log.d("TAG", "signupResponse: "+forgotPasswordResponse.getData());
            Toast.makeText(ForgotPasswordActivity.this,forgotPasswordResponse.getMessage(),Toast.LENGTH_LONG).show();
            Toast.makeText(ForgotPasswordActivity.this,forgotPasswordResponse.getData().getOtp()+"", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(getApplicationContext(),VerifyOtpActivity.class);
            intent.putExtra("number",activityForgotPasswordBinding.forgotNumber.getText().toString());
            startActivity(intent);
        }
    }

    private void setEditTextMaxLength(int length) {
        activityForgotPasswordBinding.forgotNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        activityForgotPasswordBinding.forgotNumber.setText(""); // Clear previous input
        activityForgotPasswordBinding.forgotNumber.setHint("Enter " + length + "-digit number");
    }

}