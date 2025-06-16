package com.drcita.user.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Editable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.drcita.user.DashBoardActivity;
import com.drcita.user.LanguageBaseActivity;
import com.drcita.user.LoginActivity;
import com.drcita.user.R;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityNewverifyOtpBinding;
import com.drcita.user.models.login.LoginRequest;
import com.drcita.user.models.login.LoginResponse;
import com.drcita.user.models.otp.OtpResponse;
import com.drcita.user.models.otp.VerifyotpRequest;
import com.drcita.user.retrofit.ApiClient;
import com.marcinorlowski.fonty.Fonty;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NewVerifyOtpActivity extends LanguageBaseActivity {

        private CountDownTimer countDownTimer;
        String mobile;
        private static final long OTP_TIMEOUT = 60000; // 60 seconds
    ActivityNewverifyOtpBinding activityNewverifyOtpBinding;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            activityNewverifyOtpBinding = DataBindingUtil.setContentView(this, R.layout.activity_newverify_otp);

            if(getIntent()!=null)
            {
                mobile=getIntent().getStringExtra("mobile");
            }
            activityNewverifyOtpBinding.tvMobile.setText("We sent a verification code to +91-"+mobile);

            setOtpEditTextListeners();
            startCountdown();
            activityNewverifyOtpBinding.btnVerify.setOnClickListener(v -> {
                String otp = activityNewverifyOtpBinding.otp1.getText().toString() +
                        activityNewverifyOtpBinding.otp2.getText().toString() +
                        activityNewverifyOtpBinding.otp3.getText().toString() +
                        activityNewverifyOtpBinding.otp4.getText().toString();

                if (otp.length() == 4) {
                    showLoadingDialog();
                    verifyOtp(otp);
                } else {
                    Toast.makeText(this, "Enter 4 digit OTP", Toast.LENGTH_SHORT).show();
                }
            });

            activityNewverifyOtpBinding.tvResend.setOnClickListener(v -> {
                if (activityNewverifyOtpBinding.tvResend.isEnabled()) {
                    Toast.makeText(this, "OTP Resent", Toast.LENGTH_SHORT).show();
                    callResendOtp();
                    startCountdown(); // Restart countdown
                }
            });
            activityNewverifyOtpBinding.tvchange.setOnClickListener(view -> {
                Intent intent=new Intent(this,LoginActivity.class);
                startActivity(intent);

            });

            SpannableString content = new SpannableString("Re-send");
            content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
           activityNewverifyOtpBinding.tvResend.setText(content);
            Fonty.setFonts(this);
        }

    private void callResendOtp() {

            showLoadingDialog();
//        FirebaseApp.initializeApp(this);

            if (Constants.haveInternet(getApplicationContext())) {

                LoginRequest request = new LoginRequest();
                request.setMobile(mobile);
//
                ApiClient.getRestAPI().loginUser(request).enqueue(new Callback<LoginResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(@NonNull Call<LoginResponse> call, @NonNull Response<LoginResponse> response) {
                        if (response.isSuccessful()) {
                            dismissLoadingDialog();
                            Toast.makeText(NewVerifyOtpActivity.this,response.body().getData().getOtp(),Toast.LENGTH_LONG).show();
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
                        Toast.makeText(NewVerifyOtpActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Constants.InternetSettings(NewVerifyOtpActivity.this);
            }
        }


    private void setOtpEditTextListeners() {
            activityNewverifyOtpBinding.otp1.addTextChangedListener(new GenericTextWatcher(activityNewverifyOtpBinding.otp1, activityNewverifyOtpBinding.otp2));
            activityNewverifyOtpBinding.otp2.addTextChangedListener(new GenericTextWatcher(activityNewverifyOtpBinding.otp2, activityNewverifyOtpBinding.otp3));
            activityNewverifyOtpBinding.otp3.addTextChangedListener(new GenericTextWatcher(activityNewverifyOtpBinding.otp3, activityNewverifyOtpBinding.otp4));
            activityNewverifyOtpBinding.otp4.addTextChangedListener(new GenericTextWatcher(activityNewverifyOtpBinding.otp4, null));


            activityNewverifyOtpBinding.otp2.setOnKeyListener(new GenericKeyEvent(activityNewverifyOtpBinding.otp2, activityNewverifyOtpBinding.otp1));
            activityNewverifyOtpBinding.otp3.setOnKeyListener(new GenericKeyEvent(activityNewverifyOtpBinding.otp3, activityNewverifyOtpBinding.otp2));
            activityNewverifyOtpBinding.otp4.setOnKeyListener(new GenericKeyEvent(activityNewverifyOtpBinding.otp4, activityNewverifyOtpBinding.otp3));
        }


        private void verifyOtp(String otp) {
            getOtp(otp);
        }

        private void startCountdown() {
            activityNewverifyOtpBinding.tvResend.setEnabled(false);
            activityNewverifyOtpBinding.tvResend.setAlpha(0.5f);

            countDownTimer = new CountDownTimer(OTP_TIMEOUT, 1000) {
                public void onTick(long millisUntilFinished) {
                    activityNewverifyOtpBinding.tvTimer.setText("Resend in " + millisUntilFinished / 1000 + "s");
                }

                public void onFinish() {
                    activityNewverifyOtpBinding.tvTimer.setText("");
                    activityNewverifyOtpBinding.tvResend.setEnabled(true);
                    activityNewverifyOtpBinding.tvResend.setAlpha(1f);
                }
            }.start();
        }

        @Override
        protected void onDestroy() {
            if (countDownTimer != null) {
                countDownTimer.cancel();
            }
            super.onDestroy();
        }

        private static class GenericTextWatcher implements TextWatcher {
            private final EditText currentView, nextView;
            public GenericTextWatcher(EditText currentView, EditText nextView) {
                this.currentView = currentView;
                this.nextView = nextView;
            }
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 1 && nextView != null) {
                    nextView.requestFocus();
                }
            }
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        }

    private static class GenericKeyEvent implements View.OnKeyListener {
        private final EditText currentView, previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if (event.getAction() == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL) {
                if (currentView.getText().toString().isEmpty() && previousView != null) {
                    previousView.requestFocus();
                    previousView.setSelection(previousView.getText().length());
                    return true;
                }
            }
            return false;
        }
    }

    private void getOtp(String otp) {
        if (Constants.haveInternet(getApplicationContext())) {
            showLoadingDialog();
            VerifyotpRequest otpRequest = new VerifyotpRequest();
            otpRequest.setMobile(mobile);
            otpRequest.setOtp(otp);
            ApiClient.getRestAPI().verifyOtp(otpRequest).enqueue(new Callback<OtpResponse>() {
                @Override
                public void onResponse(@NonNull Call<OtpResponse> call, @NonNull Response<OtpResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        if(response.body()!=null)
                        otpResponse(Objects.requireNonNull(response.body()));
                        else {
                            Toast.makeText(NewVerifyOtpActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<OtpResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(NewVerifyOtpActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });

        } else {
            Constants.haveInternet(NewVerifyOtpActivity.this);
        }
    }
    private void otpResponse(OtpResponse verifyotpResponse) {
        if (verifyotpResponse.isSuccess()) {
            dismissLoadingDialog();
            SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
// Save state and city IDs
            preferences.edit().putString(Constants.STATE_ID, String.valueOf(verifyotpResponse.getData().getUser().getStateId())).apply();
            preferences.edit().putString(Constants.CITY_ID, String.valueOf(verifyotpResponse.getData().getUser().getCityId())).apply();
            preferences.edit().putString(Constants.USER_ID,String.valueOf (verifyotpResponse.getData().getUser().getId())).apply();

// Get the saved values with default fallback "0"
            int stateId = Integer.parseInt(preferences.getString(Constants.STATE_ID, "0"));
            int cityId = Integer.parseInt(preferences.getString(Constants.CITY_ID, "0"));

// Check if both are 0
            if (stateId == 0 && cityId == 0) {
                // Your logic here
                Intent intent = new Intent(getApplicationContext(), UserLocationScreenActivity.class);
                startActivity(intent);
                finish();


            }
            else {
                Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                startActivity(intent);
                finish();
            }



        }
    }

}

