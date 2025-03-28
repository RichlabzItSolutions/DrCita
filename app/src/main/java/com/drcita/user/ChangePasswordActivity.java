package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.res.ResourcesCompat;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityChangePasswordBinding;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.resetpassword.ChangePasswodRequest;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePasswordActivity extends LanguageBaseActivity {

    private ProgressDialog progress;
    private ActivityChangePasswordBinding binding;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_change_password);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        binding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
                
            }
        });
        binding.backChangepassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        binding.ivShowPasswordOld.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                togglePasswordOldVisibility();

            }
        });

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


    }

    private void validations() {
        if (binding.currentPassword.getText().toString().equals("")) {
            Constants.ToastShort(ChangePasswordActivity.this, "Enter Current Password");
            return;
        }
        if (binding.currentPassword.getText().toString().length()<4) {
            Toast.makeText(getApplicationContext(),"Current Password min length should be 4",Toast.LENGTH_LONG).show();
            return;
        }
        if (binding.newPassword.getText().toString().equals("")) {
            Constants.ToastShort(ChangePasswordActivity.this, "Enter New Password");
            return;
        }
        if (binding.newPassword.getText().toString().length()<4) {
            Toast.makeText(getApplicationContext(),"Current Password min length should be 4",Toast.LENGTH_LONG).show();
            return;
        }
        if (!binding.newPassword.getText().toString().equals(binding.retypenewPassword.getText().toString())){
            Toast.makeText(ChangePasswordActivity.this, "Password doesn't match", Toast.LENGTH_SHORT).show();
            return;
        }
        callApi();

    }

    private void callApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ChangePasswodRequest request = new ChangePasswodRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setOldPassword(binding.currentPassword.getText().toString());
            request.setNewPassword(binding.newPassword.getText().toString());
            ApiClient.getRestAPI().changePassword(request).enqueue(new Callback<GlobalResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        changePasswordResponse(Objects.requireNonNull(response.body()));
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
                    Toast.makeText(ChangePasswordActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(ChangePasswordActivity.this);
        }
    }

    private void changePasswordResponse(GlobalResponse globalResponse) {
        dismissLoadingDialog();
        Log.d("TAG", "signupResponse: "+globalResponse.getMessage());
        Toast.makeText(ChangePasswordActivity.this,globalResponse.getMessage(),Toast.LENGTH_LONG).show();
        finish();

    }


    private void togglePasswordOldVisibility() {
        if (binding.currentPassword.getTransformationMethod() == null) {
            binding.currentPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.ivShowPasswordOld.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            binding.currentPassword.setTransformationMethod(null);
            binding.ivShowPasswordOld.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        binding.currentPassword.setSelection(binding.currentPassword.getText().length());
    }

    private void togglePasswordNewVisibility() {
        if (binding.newPassword.getTransformationMethod() == null) {
            binding.newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.ivShowPasswordNew.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            binding.newPassword.setTransformationMethod(null);
            binding.ivShowPasswordNew.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        binding.newPassword.setSelection(binding.newPassword.getText().length());
    }

    private void togglePasswordConfirmVisibility() {
        if (binding.retypenewPassword.getTransformationMethod() == null) {
            binding.retypenewPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
            binding.ivShowPasswordNewConfirm.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_off, null));
        } else {
            binding.retypenewPassword.setTransformationMethod(null);
            binding.ivShowPasswordNewConfirm.setImageDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.ic_password_open, null));
        }
        // Move cursor to the end of the text
        binding.retypenewPassword.setSelection(binding.retypenewPassword.getText().length());
    }

}