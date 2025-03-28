package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.common.LanguageUtil;
import com.drcita.user.common.PreferenceUtil;
import com.drcita.user.databinding.ActivityLanguageBinding;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.language.UpdateLanguageRequest;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.GetProfileResponse;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LanguageActivity extends AppCompatActivity {

    private ActivityLanguageBinding activityLanguageBinding;
    private String languageRG = "";
    String userId,name,mobile;
    int selectedId;
    private ProgressDialog progress;
    private String language;
    private int languageId;
    private int defaultLanguageId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityLanguageBinding = DataBindingUtil.setContentView(this, R.layout.activity_language);
        language = LanguageUtil.currentLanguage(this);
        languageId = defaultLanguageId = getLanguageId(language);
        setSupportActionBar(activityLanguageBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Language");
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        name = sp.getString(Constants.NAME, name);
        mobile = sp.getString(Constants.MOBILE,mobile);
        activityLanguageBinding.backLanguage.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        activityLanguageBinding.languageRG.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                RadioButton checkedRadioButton = (RadioButton) radioGroup.findViewById(radioGroup.getCheckedRadioButtonId());
                languageRG = checkedRadioButton.getText().toString();
                 selectedId = 1;
                if(checkedRadioButton.getId()==R.id.englishRB){
                    selectedId = 1;
                    language = getString(LanguageEnum.English_India.getName());
                    languageId = LanguageEnum.English_India.getValue();
                } else {
                    selectedId = 2;
                    language = getString(LanguageEnum.Hindi_India.getName());
                    languageId = LanguageEnum.Hindi_India.getValue();
                }
            }
        });
        activityLanguageBinding.updatelanguageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateLanguage();
            }
        });
        getUserProfile();


    }

    public int getLanguageId(String name) {
        for (LanguageEnum language : LanguageEnum.values()) {
            if (getString(language.getName()).equalsIgnoreCase(name)) {
                return language.getValue();
            }
        }
        return LanguageEnum.English_India.getValue();
    }

    private void getUserProfile() {
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                GetProfileRequest request = new GetProfileRequest();
                request.setUserId(Integer.parseInt(userId));
                ApiClient.getRestAPI().getUserProfile(request).enqueue(new Callback<GetProfileResponse>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull Response<GetProfileResponse> response) {
                        if (response.isSuccessful()) {
                            dismissLoadingDialog();
                            getProfileResponse(Objects.requireNonNull(response.body()));
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
                    public void onFailure(@NonNull Call<GetProfileResponse> call, @NonNull Throwable t) {
                        dismissLoadingDialog();
                        t.printStackTrace();
                        Toast.makeText(LanguageActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Constants.InternetSettings(LanguageActivity.this);
            }


    }
    private void getProfileResponse(GetProfileResponse getProfileResponse) {
        getProfileResponse.getData().getLanguage();
        if (getProfileResponse.getData().getLanguage()==1){
            activityLanguageBinding.englishRB.setChecked(true);
        }else {
            activityLanguageBinding.somaliRB.setChecked(true);
        }

    }
    private void updateLanguage() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            UpdateLanguageRequest request = new UpdateLanguageRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setLanguage(selectedId);
            ApiClient.getRestAPI().updateLanguage(request).enqueue(new Callback<GlobalResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        updateLanguageResponse(Objects.requireNonNull(response.body()));
                        PreferenceUtil.getInstance().save(Constants.LANGUAGE1, language);
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
                    Toast.makeText(LanguageActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(LanguageActivity.this);
        }
    }

    private void updateLanguageResponse(GlobalResponse globalResponse) {
        if (globalResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            Log.d("TAG", "signupResponse: " + globalResponse.getMessage());
            Toast.makeText(LanguageActivity.this, globalResponse.getMessage(), Toast.LENGTH_LONG).show();
            finish();
        }
    }

    private void showLoadingDialog() {
        if (progress == null) {
            progress = new ProgressDialog(this,R.style.MyTheme);

        }
        progress.show();
        progress.setCancelable(false);
    }

    private void dismissLoadingDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}