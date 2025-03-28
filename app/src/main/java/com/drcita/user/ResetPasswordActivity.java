package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityResetPasswordBinding;
import com.drcita.user.models.resetpassword.ResetPasswordRequest;
import com.drcita.user.models.resetpassword.ResetPasswordResponse;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ResetPasswordActivity extends LanguageBaseActivity {

    String number;
    private ActivityResetPasswordBinding activityResetPasswordBinding;
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityResetPasswordBinding = DataBindingUtil.setContentView(this,R.layout.activity_reset_password);


        activityResetPasswordBinding.ivShowPasswordNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePasswordNewVisibility();
            }
        });

        activityResetPasswordBinding.ivShowPasswordNewConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePasswordConfirmVisibility();
            }
        });

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                number = null;
            } else {
                number = extras.getString("number");
            }
        } else {
        }
        activityResetPasswordBinding.resetpswdBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityResetPasswordBinding.newPassword.getText().toString().equals("")) {
                    Constants.ToastShort(ResetPasswordActivity.this, "Enter New Password");
                    return;
                }
                if (activityResetPasswordBinding.newPassword.getText().toString().length()<4) {
                    Toast.makeText(getApplicationContext(),"Password min length should be 4",Toast.LENGTH_LONG).show();
                    return;
                }
                if (activityResetPasswordBinding.confirmPassword.getText().toString().equals("")) {
                    Constants.ToastShort(ResetPasswordActivity.this, "Enter Confirm Password");
                    return;
                }
                if (!activityResetPasswordBinding.newPassword.getText().toString().equals(activityResetPasswordBinding.confirmPassword.getText().toString())){
                    Toast.makeText(ResetPasswordActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
                    return;
                }
                callApi();
            }
        });
    }

    private void callApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ResetPasswordRequest request = new ResetPasswordRequest();
            request.setMobile(number);
            request.setPassword(activityResetPasswordBinding.newPassword.getText().toString());
            request.setConfirmPassword(activityResetPasswordBinding.confirmPassword.getText().toString());
            ApiClient.getRestAPI().resetPassword(request).enqueue(new Callback<ResetPasswordResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<ResetPasswordResponse> call, @NonNull Response<ResetPasswordResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        resetPasswordResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<ResetPasswordResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(ResetPasswordActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(ResetPasswordActivity.this);
        }
    }

    private void resetPasswordResponse(ResetPasswordResponse resetPasswordResponse) {
        if (resetPasswordResponse.getStatus().equals("success")){
            dismissLoadingDialog();
            Log.d("TAG", "signupResponse: "+resetPasswordResponse.getData());
            Toast.makeText(ResetPasswordActivity.this,resetPasswordResponse.getMessage(),Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(intent);
        }
    }


    private void togglePasswordNewVisibility() {
        if (activityResetPasswordBinding.newPassword.getTransformationMethod() == null) {
            activityResetPasswordBinding.newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            activityResetPasswordBinding.ivShowPasswordNew.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            activityResetPasswordBinding.newPassword.setTransformationMethod(null);
            activityResetPasswordBinding.ivShowPasswordNew.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        activityResetPasswordBinding.newPassword.setSelection(activityResetPasswordBinding.newPassword.getText().length());
    }

    private void togglePasswordConfirmVisibility() {
        if (activityResetPasswordBinding.confirmPassword.getTransformationMethod() == null) {
            activityResetPasswordBinding.confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            activityResetPasswordBinding.ivShowPasswordNewConfirm.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            activityResetPasswordBinding.confirmPassword.setTransformationMethod(null);
            activityResetPasswordBinding.ivShowPasswordNewConfirm.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        activityResetPasswordBinding.confirmPassword.setSelection(activityResetPasswordBinding.confirmPassword.getText().length());
    }



}