package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;

import com.drcita.user.adapter.HospitalsAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityHospitalsListBinding;
import com.drcita.user.models.hospitals.DataItem;
import com.drcita.user.models.hospitals.HospitalsRequest;
import com.drcita.user.models.hospitals.HospitalsResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class HospitalsListActivity extends LanguageBaseActivity {

    private HospitalsAdapter hospitalsAdapter;
    private ActivityHospitalsListBinding activityHospitalsListBinding;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private int region;
    int regionID;
    private ProgressDialog progress;
    private int specailization;
    boolean isfromdental = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHospitalsListBinding = DataBindingUtil.setContentView(this, R.layout.activity_hospitals_list);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                specailization = 0;
            } else {
                specailization = extras.getInt("specailization");
                isfromdental = extras.getBoolean(Constants.isfromdental);
            }
        } else {
        }
        setSupportActionBar(activityHospitalsListBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Hospitals List");
        activityHospitalsListBinding.backHospitalsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        activityHospitalsListBinding.hospitalsRV.setLayoutManager(mLayoutManager);
        hospitalsAdapter = new HospitalsAdapter(this,responses, isfromdental);
        activityHospitalsListBinding.hospitalsRV.setAdapter(hospitalsAdapter);
        activityHospitalsListBinding.searcautoCompleteTextView.setThreshold(1);

        activityHospitalsListBinding.searcautoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                try {
                    hospitalsAdapter.getFilter().filter(s.toString());
                    hospitalsAdapter.notifyDataSetChanged();
                }
                catch (Exception ex)
                {
                    ex.getMessage();
                }
            }
        });


        getHospitalsList();

    }

    private void getHospitalsList() {
        showLoadingDialog();
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        regionID = sp.getInt(Constants.REGION, regionID);
            if (Constants.haveInternet(getApplicationContext())) {
                HospitalsRequest hospitalsRequest = new HospitalsRequest();
                if(isfromdental){
                    hospitalsRequest.setSpecialisationId(14);
                }else {
                    hospitalsRequest.setSpecialisationId(specailization);
                }
                hospitalsRequest.setRegion(regionID);
                ApiClient.getRestAPI().getProvidersList(hospitalsRequest).enqueue(new Callback<HospitalsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<HospitalsResponse> call, @NonNull retrofit2.Response<HospitalsResponse> response) {
                        getHospitalsListResponse(Objects.requireNonNull(response.body()));
                    }
                    @Override
                    public void onFailure(@NonNull Call<HospitalsResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(HospitalsListActivity.this);
            }


    }

    private void getHospitalsListResponse(HospitalsResponse hospitalsResponse) {
        dismissLoadingDialog();
        String description = hospitalsResponse.getMessage();
        if (hospitalsResponse.getStatus().equals("success")){
            dismissLoadingDialog();
            List<DataItem> dataItems = hospitalsResponse.getData();
            if (hospitalsResponse.getData().isEmpty()){
                activityHospitalsListBinding.nodataimage.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < dataItems.size(); i++) {
                responses.clear();
                responses.addAll(hospitalsResponse.getData());
                hospitalsAdapter.notifyDataSetChanged();
            }

        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }

}