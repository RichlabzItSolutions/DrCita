package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.adapter.DiagnosticsAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityDiagnosticsBinding;
import com.drcita.user.models.hospitals.DataItem;
import com.drcita.user.models.scans.DiagnosticsRequest;
import com.drcita.user.models.scans.DiagnosticsResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class DiagnosticsActivity extends LanguageBaseActivity {

    private ActivityDiagnosticsBinding binding;
    private int region;
    private DiagnosticsAdapter diagnosticsAdapter;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private ProgressDialog progress;
     int regionID;
     String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_diagnostics);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                type = null;
            } else {
                type = extras.getString("type");


            }
        } else {
        }
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Hospitals List");
        binding.backHospitalsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.hospitalsRV.setLayoutManager(mLayoutManager);
        diagnosticsAdapter = new DiagnosticsAdapter(this,responses,type);
        binding.hospitalsRV.setAdapter(diagnosticsAdapter);
        getScanProvidersList();
    }

    private void getScanProvidersList() {
        showLoadingDialog();
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        regionID = sp.getInt(Constants.REGION, regionID);
        if (Constants.haveInternet(getApplicationContext())) {
            DiagnosticsRequest diagnosticsRequest = new DiagnosticsRequest();
            diagnosticsRequest.setRegion(regionID);
            ApiClient.getRestAPI().getScanProvidersList(diagnosticsRequest).enqueue(new Callback<DiagnosticsResponse>() {
                @Override
                public void onResponse(@NonNull Call<DiagnosticsResponse> call, @NonNull retrofit2.Response<DiagnosticsResponse> response) {
                    getScanProvidersListResponse(Objects.requireNonNull(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<DiagnosticsResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(DiagnosticsActivity.this);
        }
    }

    private void getScanProvidersListResponse(DiagnosticsResponse diagnosticsResponse) {
        dismissLoadingDialog();
        String description = diagnosticsResponse.getMessage();
        if (diagnosticsResponse.getStatus().equals("success")){
            dismissLoadingDialog();
            List<DataItem> dataItems = diagnosticsResponse.getData();
            if (diagnosticsResponse.getData().isEmpty()){
                binding.nodataimage.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < dataItems.size(); i++) {
                responses.clear();
                responses.addAll(diagnosticsResponse.getData());
                diagnosticsAdapter.notifyDataSetChanged();
            }

        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


}