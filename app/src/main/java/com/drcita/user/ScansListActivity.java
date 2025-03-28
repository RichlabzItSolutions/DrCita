package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.adapter.ScansListAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityScansListBinding;
import com.drcita.user.models.scans.DataItems;
import com.drcita.user.models.scans.ScansListRequest;
import com.drcita.user.models.scans.ScansListResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ScansListActivity extends LanguageBaseActivity {

    private ActivityScansListBinding binding;
    private String userId;
    private ScansListAdapter scansListAdapter;
    private final List<DataItems> responses = new ArrayList<>();
    private ProgressDialog progress;
    private int providerID;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_scans_list);
        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Diagnostics Scan");
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                providerID = 0;
                type=null;
            } else {
                providerID = extras.getInt("providerID");
                type=extras.getString("type");
            }
        } else {

        }
        binding.backHospitalsDoctorsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.diagnosticsListRV.setLayoutManager(mLayoutManager);
        scansListAdapter = new ScansListAdapter(this,responses,type);
        binding.diagnosticsListRV.setAdapter(scansListAdapter);
        getScanList();
    }
    private void getScanList() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ScansListRequest diagnosticRequest = new ScansListRequest();
            diagnosticRequest.setProviderId(providerID);
            diagnosticRequest.setUserType(1);
            ApiClient.getRestAPI().getScansList(diagnosticRequest).enqueue(new Callback<ScansListResponse>() {
                @Override
                public void onResponse(@NonNull Call<ScansListResponse> call, @NonNull retrofit2.Response<ScansListResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        getDiagnosticResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<ScansListResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(ScansListActivity.this);
        }
    }

    private void getDiagnosticResponse(ScansListResponse diagnosticListResponse) {
        dismissLoadingDialog();
        String description = diagnosticListResponse.getMessage();
        if (diagnosticListResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<com.drcita.user.models.scans.DataItems> dataItems = diagnosticListResponse.getData();
            if (diagnosticListResponse.getData().isEmpty()){
                binding.nodataimage.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < dataItems.size(); i++) {
                responses.clear();
                responses.addAll(diagnosticListResponse.getData());
                scansListAdapter.notifyDataSetChanged();
            }
            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


}