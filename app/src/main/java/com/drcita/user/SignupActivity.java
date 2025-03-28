package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivitySignupBinding;
import com.drcita.user.models.signup.SignupRequest;
import com.drcita.user.models.signup.SignupResponse;
import com.drcita.user.retrofit.ApiClient;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FacebookAuthProvider;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends LanguageBaseActivity {

    private ActivitySignupBinding binding;
    private static final String EMAIL = "email";
    private CallbackManager callbackManager;
//    private FirebaseAuth mAuth = null;
//    private FirebaseUser firebaseUser = null;
    String selectedCountryCodes = "+91";

    String strMobile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup);


        binding.countryCodePicker.setCountryForNameCode("IN");

      //  selectedCountryCodes = binding.countryCodePicker.getSelectedCountryCodeWithPlus();

        binding.countryCodePicker.setOnCountryChangeListener(() -> {
            selectedCountryCodes = binding.countryCodePicker.getSelectedCountryCodeWithPlus();
            //Toast.makeText(SignupActivity.this, "Selected: " + selectedCountryCodes, Toast.LENGTH_SHORT).show();
           // Log.d("fghj", selectedCode);
            Log.d("fghj", selectedCountryCodes);
        });
      //  Log.d("fghj", selectedCountryCodes);
       // Log.d("fghj", selectedCountryCodes);


        binding.ivShowPasswordNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePasswordNewVisibility();
            }
        });

        binding.ivShowPasswordNewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePasswordConfirmVisibility();
            }
        });

        binding.signupRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                strMobile = binding.signupNumber.getText().toString().trim();

                // String selectedCountryCode = binding.countryCodePicker.getSelectedCountryCodeWithPlus();

                validations();
            }
        });
//        binding.fbLogin.setReadPermissions(Arrays.asList(EMAIL));
//        mAuth =FirebaseAuth.getInstance();
//        firebaseUser = mAuth.getCurrentUser();
        callbackManager = CallbackManager.Factory.create();
        LoginManager.getInstance().registerCallback(
                callbackManager,
                new FacebookCallback<LoginResult>() {
                    @Override
                    public void onSuccess(LoginResult loginResult) {
                        // Handle success
                        handleFacebookAccessToken(loginResult.getAccessToken());
                    }

                    @Override
                    public void onCancel() {
                    }

                    @Override
                    public void onError(FacebookException exception) {
                    }
                }
        );

        binding.login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
            }
        });

    }

    private void handleFacebookAccessToken(AccessToken token) {
        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
//        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
//            @Override
//            public void onComplete(@NonNull Task<AuthResult> task) {
//                if(task.isSuccessful()){
//                    GraphRequest.newMeRequest(token, new GraphRequest.GraphJSONObjectCallback() {
//                        @Override
//                        public void onCompleted(@Nullable JSONObject jsonObject, @Nullable GraphResponse graphResponse) {
//                            String first_name = jsonObject.optString("first_name");
//                            String last_name = jsonObject.optString("last_name");
//                            //TODO print jsonObject and see what values coming
//                        }
//                    });
//                }
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//
//            }
//        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void validations() {
        if (binding.signupName.getText().toString().equals("")) {
            Constants.ToastShort(SignupActivity.this, "Enter First Name");
            return;
        }
        if (binding.signupName.getText().toString().length() < 3) {
            Constants.ToastShort(SignupActivity.this, "First Name length should be 3");
            return;
        }
        if (binding.signupLName.getText().toString().equals("")) {
            Constants.ToastShort(SignupActivity.this, "Enter Last Name");
            return;
        }
        if (binding.signupLName.getText().toString().length() < 1) {
            Constants.ToastShort(SignupActivity.this, "Last Name length should be 1");
            return;
        }
        if (binding.signupNumber.getText().toString().equals("")) {
            Constants.ToastShort(SignupActivity.this, "Enter Phone Number");
            return;
        }



        boolean isValid = false;
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
            binding.signupNumber.setError(errorMsg);
            return;
        }



       /* if (selectedCountryCodes.equals("+91")) {

            if (strMobile.length() < 10){
                Toast.makeText(SignupActivity.this, R.string.please_enter_your_valid_mobile_number, Toast.LENGTH_SHORT).show();

            }

            if (!(Integer.valueOf(strMobile.substring(0, 1)) > 5)){
                Toast.makeText(SignupActivity.this, R.string.please_enter_your_valid_mobile_number, Toast.LENGTH_SHORT).show();

            }
        }

        else if (selectedCountryCodes.equals("+971")) {

            if (strMobile.length() < 7){
                Toast.makeText(SignupActivity.this, R.string.please_enter_your_valid_mobile_number, Toast.LENGTH_SHORT).show();

            }

            if (!(Integer.valueOf(strMobile.substring(0, 1)) > 5)){
                Toast.makeText(SignupActivity.this, R.string.please_enter_your_valid_mobile_number, Toast.LENGTH_SHORT).show();

            }

        }*/
       /* if (selectedCountryCodes.equals("+91") || strMobile.length() < 10  || !(Integer.valueOf(strMobile.substring(0, 1)) > 5)){

            Toast.makeText(SignupActivity.this, R.string.please_enter_your_valid_mobile_number, Toast.LENGTH_SHORT).show();

        }
        if (selectedCountryCodes.equals("+971") || strMobile.length() < 7  || !(Integer.valueOf(strMobile.substring(0, 1)) > 5)){

            Toast.makeText(SignupActivity.this, R.string.please_enter_your_valid_mobile_number, Toast.LENGTH_SHORT).show();

        }*/
         /*if (!(Integer.valueOf(strMobile.substring(0, 1)) > 5) || ){

            Toast.makeText(SignupActivity.this, R.string.not_valid_mobile_number, Toast.LENGTH_SHORT).show();

        }*/
         /*if (binding.signupNumber.getText().toString() < 10) {

            Toast.makeText(SignupActivity.this, R.string.please_enter_your_valid_mobile_number, Toast.LENGTH_SHORT).show();

        } */
     /*   if (!(binding.signupNumber.getText().toString().matches(Constants.MobilePattern)) &&(!(binding.signupNumber.getText().toString().matches(Constants.MobilePattern1)))) {
            Constants.ToastShort(SignupActivity.this, Constants.enter_valid_mobile_no);
            return;
        }*/

        if (binding.signupPassword.getText().toString().equals("")) {
            Constants.ToastShort(SignupActivity.this, "Enter Password");
            return;
        }
        if (binding.signupPassword.getText().toString().length() < 4) {
            Toast.makeText(getApplicationContext(), "Password min length should be 4", Toast.LENGTH_LONG).show();
            return;
        }
        if (binding.signupconfirmPassword.getText().toString().equals("")) {
            Constants.ToastShort(SignupActivity.this, "Enter Confirm Password");
            return;
        }
        if (binding.signupconfirmPassword.getText().toString().length() < 4) {
            Toast.makeText(getApplicationContext(), "Confirm Password min length should be 4", Toast.LENGTH_LONG).show();
            return;
        }
        if (!binding.signupPassword.getText().toString().equals(binding.signupconfirmPassword.getText().toString())){
            Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }


        callApi();
    }

    private void callApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            SignupRequest request = new SignupRequest();
            request.setFirstName(binding.signupName.getText().toString());
            request.setLastName(binding.signupLName.getText().toString());
            request.setMobile(binding.signupNumber.getText().toString());
            request.setPassword(binding.signupPassword.getText().toString());
            request.setConfirmPassword(binding.signupPassword.getText().toString());

            ApiClient.getRestAPI().register(request).enqueue(new Callback<SignupResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<SignupResponse> call, @NonNull Response<SignupResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        signupResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<SignupResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(SignupActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(SignupActivity.this);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void signupResponse(SignupResponse signupResponse) {
        if (signupResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            Log.d("TAG", "signupResponse: " + signupResponse.getData());
           // Toast.makeText(SignupActivity.this, ""+signupResponse.getData().getOtp(), Toast.LENGTH_LONG).show();
            Toast.makeText(SignupActivity.this, signupResponse.getData().getOtp(), Toast.LENGTH_LONG).show();
            SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
            preferences.edit().putString(Constants.USER_ID, signupResponse.getData().getUserId()).apply();
            preferences.edit().putString(Constants.NAME, signupResponse.getData().getName()).apply();
            Intent intent = new Intent(getApplicationContext(), VerifyOtpActivity.class);
            intent.putExtra("number", binding.signupNumber.getText().toString());
           /* intent.putExtra("otp", signupResponse.getData().getOtp());
            intent.putExtra("firstname", binding.signupName.getText().toString());
            intent.putExtra("lastname", binding.signupLName.getText().toString());
            intent.putExtra("password", binding.signupPassword.getText().toString());
            intent.putExtra("confirmpassword", binding.signupconfirmPassword.getText().toString());*/
            intent.putExtra("signup",true);
            startActivity(intent);
        }

    }


    private void togglePasswordNewVisibility() {
        if (binding.signupPassword.getTransformationMethod() == null) {
            binding.signupPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.ivShowPasswordNew.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            binding.signupPassword.setTransformationMethod(null);
            binding.ivShowPasswordNew.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        binding.signupPassword.setSelection(binding.signupPassword.getText().length());
    }

    private void togglePasswordConfirmVisibility() {
        if (binding.signupconfirmPassword.getTransformationMethod() == null) {
            binding.signupconfirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.ivShowPasswordNewConfirm.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            binding.signupconfirmPassword.setTransformationMethod(null);
            binding.ivShowPasswordNewConfirm.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        binding.signupconfirmPassword.setSelection(binding.signupconfirmPassword.getText().length());
    }

}