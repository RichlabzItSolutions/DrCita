package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.common.PreferenceUtil;
import com.drcita.user.databinding.ActivityLoginBinding;
import com.drcita.user.models.login.LoginRequest;
import com.drcita.user.models.login.LoginResponse;
import com.drcita.user.retrofit.ApiClient;
import com.facebook.internal.Validate;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends LanguageBaseActivity {

    private ActivityLoginBinding activityLoginBinding;
    private ProgressDialog progress;
    String selectedCountryCodes = "+91";

    String strMobile = "";

    String password = "";
    String countryCode = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLoginBinding = DataBindingUtil.setContentView(this,R.layout.activity_login);

        activityLoginBinding.countryCodePicker.setCountryForNameCode("IN");

       // String selectedCountryCodes = activityLoginBinding.countryCodePicker.getSelectedCountryCodeWithPlus();

        activityLoginBinding.countryCodePicker.setOnCountryChangeListener(() -> {
            selectedCountryCodes = activityLoginBinding.countryCodePicker.getSelectedCountryCodeWithPlus();
            //Toast.makeText(SignupActivity.this, "Selected: " + selectedCountryCodes, Toast.LENGTH_SHORT).show();
            // Log.d("fghj", selectedCode);
            Log.d("fghj", selectedCountryCodes);

            if (selectedCountryCodes.equals("+91")) {


                setEditTextMaxLength(10); // India: 10 digits
            } else if (selectedCountryCodes.equals("+971")) {
                setEditTextMaxLength(9); // UAE: 9 digits
            }
        });

        activityLoginBinding.signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });
        activityLoginBinding.forgotpasswordTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              Intent intent = new Intent(getApplicationContext(),ForgotPasswordActivity.class);
              startActivity(intent);
            }
        });
        activityLoginBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // countryCode = activityLoginBinding.countryCodePicker.getSelectedCountryCode();
                strMobile = activityLoginBinding.loginNumber.getText().toString();
                password = activityLoginBinding.loginPassword.getText().toString();

                validations(strMobile, password);
            }
        });

        activityLoginBinding.ivShowPasswordOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePasswordOldVisibility();

            }
        });
    }

    private void setEditTextMaxLength(int length) {
        activityLoginBinding.loginNumber.setFilters(new InputFilter[]{new InputFilter.LengthFilter(length)});
        activityLoginBinding.loginNumber.setText(""); // Clear previous input
        activityLoginBinding.loginNumber.setHint("Enter " + length + "-digit number");
    }
    private void validations(String strMobile, String password) {

      //  strMobile = activityLoginBinding.loginNumber.getText().toString();

        if (TextUtils.isEmpty(strMobile)){
            //Constants.ToastShort(LoginActivity.this, "Enter Phone Number");
            Toast.makeText(LoginActivity.this, "Enter Phone Number", Toast.LENGTH_SHORT).show();
            //return;
        }


        if (selectedCountryCodes.equals("+91")) {
            if (strMobile.length() < 10){
                Toast.makeText(LoginActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
            }
             if (!(Integer.valueOf(strMobile.substring(0, 1)) > 5)){
                Toast.makeText(LoginActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
            }
        }
        else{



        }



        if (selectedCountryCodes.equals("+971")) {
             if (strMobile.length() < 9){
                Toast.makeText(LoginActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();
             }
             if (!(strMobile.charAt(0) == '5')) {
                Toast.makeText(LoginActivity.this, "Invalid UAE number! Must start with 5.", Toast.LENGTH_SHORT).show();
             }
        }
        else{


        }
        if (TextUtils.isEmpty(password)) {
            //Constants.ToastShort(LoginActivity.this, "Enter Password");
            Toast.makeText(getApplicationContext(),"Enter Password",Toast.LENGTH_LONG).show();

        }  if (password.length()<4) {
            Toast.makeText(getApplicationContext(),"Password min length should be 4",Toast.LENGTH_LONG).show();
        }
        else {
            callApi();

        }

    }

    private void callApi() {
        showLoadingDialog();
        FirebaseApp.initializeApp(this);
        String token = FirebaseInstanceId.getInstance().getToken();
        if (Constants.haveInternet(getApplicationContext())) {
            Log.d("token",token);
            LoginRequest request = new LoginRequest();
            request.setMobile(activityLoginBinding.loginNumber.getText().toString());
            request.setPassword(activityLoginBinding.loginPassword.getText().toString());
            request.setDeviceToken(token);
            ApiClient.getRestAPI().loginUser(request).enqueue(new Callback<LoginResponse>() {
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
                    Toast.makeText(LoginActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(LoginActivity.this);
        }
    }
    private void loginResponse(LoginResponse loginResponse) {
            if (loginResponse.getStatus().equals("success")){
                dismissLoadingDialog();
                Log.d("TAG", "signupResponse: "+loginResponse.getData());

                Toast.makeText(LoginActivity.this,loginResponse.getMessage(),Toast.LENGTH_LONG).show();
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                preferences.edit().putString(Constants.USER_ID, loginResponse.getData().getUserId()).apply();
                preferences.edit().putString(Constants.NAME, loginResponse.getData().getName()).apply();
                preferences.edit().putString(Constants.MOBILE, loginResponse.getData().getMobile()).apply();
                preferences.edit().putInt(Constants.PROFILESTATUS, loginResponse.getData().getProfile_status()).apply();
                preferences.edit().putInt(Constants.REGION, (loginResponse.getData().getRegion())).apply();
                preferences.edit().putString(Constants.LANGUAGE, String.valueOf(loginResponse.getData().getLanguage())).apply();
                Log.e("TAG", "loginResponse: "+loginResponse.getData().getLanguage() );
                Log.e("TAG", "loginResponse: "+LanguageEnum.getByValue(loginResponse.getData().getLanguage()) );
                if(LanguageEnum.getByValue(loginResponse.getData().getLanguage())!=null) {
                    PreferenceUtil.getInstance().save(Constants.LANGUAGE1, getString(LanguageEnum.getByValue(loginResponse.getData().getLanguage()).getName()));
                }
                if (loginResponse.getData().getProfile_status()==1){
                    Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
                    //intent.putExtra("data", Parcels.wrap(loginResponse));
                   /* Bundle b = new Bundle();
                    b.putParcelable("data",Parcels.wrap(loginResponse));
                    intent.putExtra("mounika",b);*/
                    //getIntent().getExtras().getParcelable("food")
                    startActivity(intent);

                }
                else {
                    Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
                   // intent.putExtra("data", Parcels.wrap(loginResponse));
                    startActivity(intent);
                }


            }
    }

    private void togglePasswordOldVisibility() {
        if (activityLoginBinding.loginPassword.getTransformationMethod() == null) {
            activityLoginBinding.loginPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            activityLoginBinding.ivShowPasswordOld.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            activityLoginBinding.loginPassword.setTransformationMethod(null);
            activityLoginBinding.ivShowPasswordOld.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        activityLoginBinding.loginPassword.setSelection(activityLoginBinding.loginPassword.getText().length());
    }


}