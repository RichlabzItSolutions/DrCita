package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityVerifyOtpBinding;
import com.drcita.user.models.otp.VerifyotpRequest;
import com.drcita.user.models.otp.VerifyotpResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.auth.api.phone.SmsRetrieverClient;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.ContentValues.TAG;

public class VerifyOtpActivity extends LanguageBaseActivity {


    private ActivityVerifyOtpBinding activityVerifyOtpBinding;
    private ProgressDialog progress;
    String number, firstname, lastname, password, confirmpassword;
    boolean isFromSignup = false;
    private BroadcastReceiver mySMSBroadcastReceiver;
    private String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityVerifyOtpBinding = DataBindingUtil.setContentView(this, R.layout.activity_verify_otp);
        SmsRetrieverClient client = SmsRetriever.getClient(this);
        Task<Void> task = client.startSmsRetriever();
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {

            }
        });
        mySMSBroadcastReceiver = new MySMSBroadcastReceiver();
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                number = null;
            } else {
                number = extras.getString("number");
              /*  firstname = extras.getString("firstname");
                lastname = extras.getString("lastname");
                password = extras.getString("password");
                confirmpassword = extras.getString("confirmpassword");*/
                isFromSignup = extras.getBoolean(Constants.signup);
            }
        } else {
        }



        activityVerifyOtpBinding.resendOtp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        StringBuilder sb = new StringBuilder();
        activityVerifyOtpBinding.editOtp1.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 0 & activityVerifyOtpBinding.editOtp1.length() == 1) {
                    sb.append(s);
                    activityVerifyOtpBinding.editOtp1.clearFocus();
                    activityVerifyOtpBinding.editOtp2.requestFocus();
                    activityVerifyOtpBinding.editOtp2.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    activityVerifyOtpBinding.editOtp1.requestFocus();
                }
            }
        });
        activityVerifyOtpBinding.editOtp2.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 0 & activityVerifyOtpBinding.editOtp2.length() == 1) {
                    sb.append(s);
                    activityVerifyOtpBinding.editOtp2.clearFocus();
                    activityVerifyOtpBinding.editOtp3.requestFocus();
                    activityVerifyOtpBinding.editOtp3.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    activityVerifyOtpBinding.editOtp2.requestFocus();
                }
            }
        });
        activityVerifyOtpBinding.editOtp3.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 0 & activityVerifyOtpBinding.editOtp3.length() == 1) {
                    sb.append(s);
                    activityVerifyOtpBinding.editOtp3.clearFocus();
                    activityVerifyOtpBinding.editOtp4.requestFocus();
                    activityVerifyOtpBinding.editOtp4.setCursorVisible(true);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    activityVerifyOtpBinding.editOtp3.requestFocus();
                }
            }
        });
        activityVerifyOtpBinding.editOtp4.addTextChangedListener(new TextWatcher() {
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (sb.length() == 0 & activityVerifyOtpBinding.editOtp4.length() == 1) {
                    sb.append(s);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                if (sb.length() == 1) {
                    sb.deleteCharAt(0);
                }
            }

            public void afterTextChanged(Editable s) {
                if (sb.length() == 0) {
                    activityVerifyOtpBinding.editOtp4.requestFocus();
                }
            }
        });
        activityVerifyOtpBinding.verifyotpSubmitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String one = activityVerifyOtpBinding.editOtp1.getText().toString().trim();
                String two = activityVerifyOtpBinding.editOtp2.getText().toString().trim();
                String three = activityVerifyOtpBinding.editOtp3.getText().toString().trim();
                String four = activityVerifyOtpBinding.editOtp4.getText().toString().trim();
                 otp = one + two + three + four;
                if (otp.equals("")) {
                    Toast.makeText(getApplicationContext(), "Enter Otp", Toast.LENGTH_LONG).show();
                    return;
                }
                getOtp(otp);
            }
        });
    }
    private void getOtp(String otp) {
        if (Constants.haveInternet(getApplicationContext())) {
            showLoadingDialog();
            VerifyotpRequest otpRequest = new VerifyotpRequest();
            otpRequest.setMobile(number);
            otpRequest.setOtp(otp);
            ApiClient.getRestAPI().verifyOTP(otpRequest).enqueue(new Callback<VerifyotpResponse>() {
                @Override
                public void onResponse(@NonNull Call<VerifyotpResponse> call, @NonNull Response<VerifyotpResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        Log.e(TAG, "onResponse:1 ");
                        otpResponse(Objects.requireNonNull(response.body()));
                    } else {
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<VerifyotpResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(VerifyOtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });

        } else {
            Constants.haveInternet(VerifyOtpActivity.this);
        }
    }
    private void otpResponse(VerifyotpResponse verifyotpResponse) {
        if (verifyotpResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
           // Toast.makeText(VerifyOtpActivity.this, verifyotpResponse.getMessage(), Toast.LENGTH_LONG).show();
            if (isFromSignup) {
                Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(getApplicationContext(), ResetPasswordActivity.class);
                intent.putExtra("number", number);
                startActivity(intent);
                finish();
            }
        }
    }



    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter mIntentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        registerReceiver(mySMSBroadcastReceiver, mIntentFilter);
    }

    private class MySMSBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                Status status = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                switch (status.getStatusCode()) {
                    case CommonStatusCodes.SUCCESS:
                        String message = (String) extras.get(SmsRetriever.EXTRA_SMS_MESSAGE);
                        otp = parsedString(message);
                        activityVerifyOtpBinding.editOtp1.setText(String.valueOf(otp.charAt(0)));
                        activityVerifyOtpBinding.editOtp2.setText(String.valueOf(otp.charAt(1)));
                        activityVerifyOtpBinding.editOtp3.setText(String.valueOf(otp.charAt(2)));
                        activityVerifyOtpBinding.editOtp4.setText(String.valueOf(otp.charAt(3)));
                        getOtp(otp);
                        break;
                    case CommonStatusCodes.TIMEOUT:
                        // Waiting for SMS timed out (5 minutes)
                        // Handle the error ...
                        break;
                }
            }
        }
    }

    private String parsedString(String message) {
        Pattern mPattern = Pattern.compile("(|^)\\d{4}");
        Matcher mMatcher = mPattern.matcher(message);
        if (mMatcher.find()) {
            return mMatcher.group(0);
        } else {
            return "";
        }
    }
}