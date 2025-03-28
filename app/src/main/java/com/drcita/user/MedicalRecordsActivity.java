package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.adapter.MedicalRecordsAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityMedicalRecordsBinding;
import com.drcita.user.models.medicalrecords.DataItem;
import com.drcita.user.models.medicalrecords.GetMedicalRecordsRequest;
import com.drcita.user.models.medicalrecords.GetMedicalRecordsResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class MedicalRecordsActivity extends LanguageBaseActivity {

    private ActivityMedicalRecordsBinding binding;
    private MedicalRecordsAdapter medicalRecordsAdapter;
    private String userId;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private ProgressDialog progress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_medical_records);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Medical Records");
        binding.backMedicalrecords.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.medicalrecordsRV.setLayoutManager(mLayoutManager);
        medicalRecordsAdapter = new MedicalRecordsAdapter(this,responses);
        binding.medicalrecordsRV.setAdapter(medicalRecordsAdapter);
        getUserMedicalRecords();
    }

    private void getUserMedicalRecords() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            GetMedicalRecordsRequest getMedicalRecordsRequest = new GetMedicalRecordsRequest();
            getMedicalRecordsRequest.setUserId(Integer.parseInt(userId));
            ApiClient.getRestAPI().getUserMedicalRecords(getMedicalRecordsRequest).enqueue(new Callback<GetMedicalRecordsResponse>() {
                @Override
                public void onResponse(@NonNull Call<GetMedicalRecordsResponse> call, @NonNull retrofit2.Response<GetMedicalRecordsResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        getMedicalRecordsResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<GetMedicalRecordsResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(MedicalRecordsActivity.this);
        }
    }

    private void getMedicalRecordsResponse(GetMedicalRecordsResponse getMedicalRecordsResponse) {
        dismissLoadingDialog();
        String description = getMedicalRecordsResponse.getMessage();
        if (getMedicalRecordsResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<com.drcita.user.models.medicalrecords.DataItem> dataItems = getMedicalRecordsResponse.getData();
            if (getMedicalRecordsResponse.getData().isEmpty()){
                binding.nodataimage.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < dataItems.size(); i++) {
                responses.clear();
                responses.addAll(getMedicalRecordsResponse.getData());
                medicalRecordsAdapter.notifyDataSetChanged();
            }
            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }

    }

}