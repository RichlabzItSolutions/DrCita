package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
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
import com.drcita.user.models.newProviderlist.NewProviderList;
import com.drcita.user.models.newProviderlist.ProviderResponse;
import com.drcita.user.models.newProviderlist.ProvidersRequestData;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class HospitalsListActivity extends LanguageBaseActivity {

    private HospitalsAdapter hospitalsAdapter;
    private ActivityHospitalsListBinding activityHospitalsListBinding;
    private List<NewProviderList> responses = new ArrayList<NewProviderList>();
    private int region;
    int regionID;
    private ProgressDialog progress;
    private int specailization;
    boolean isfromdental = false;

    ArrayList<Integer> setcharges=new ArrayList<>();
    ArrayList<Integer> specailizations=new ArrayList<>();

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
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        displayHospitals();
        getSupportActionBar().setTitle("Hospitals List");
        TabLayout tabLayout = findViewById(R.id.tabLayout);

// Add two tabs manually
        tabLayout.addTab(tabLayout.newTab().setText("By Hospital"));
        tabLayout.addTab(tabLayout.newTab().setText("By Specialization"));

// Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    displayHospitals();
                    // Show By Hospital content
                } else if (position == 1) {
                    // Show By Specialization content
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });



    }

    private void displayHospitals() {
        activityHospitalsListBinding.backHospitalsList.setOnClickListener(view -> finish());
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
            @SuppressLint("NotifyDataSetChanged")
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

                ProvidersRequestData providersRequestData=new ProvidersRequestData();
                providersRequestData.setArea("");
                providersRequestData.setCityId(14);
                providersRequestData.setSearchStr("");
                providersRequestData.setConsultationMode(setcharges);
                providersRequestData.setSpecializationId(specailizations);
//                HospitalsRequest hospitalsRequest = new HospitalsRequest();
//                if(isfromdental){
//                    hospitalsRequest.setSpecialisationId(14);
//                }else {
//                    hospitalsRequest.setSpecialisationId(specailization);
//                }
//                hospitalsRequest.setRegion(regionID);
                ApiClient.getRestAPI().getProviderResponse(providersRequestData).enqueue(new Callback<ProviderResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ProviderResponse> call, @NonNull retrofit2.Response<ProviderResponse> response) {

                        if(response.body().getData().size()>0) {
                            getHospitalsListResponse(Objects.requireNonNull(response.body()));
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<ProviderResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(HospitalsListActivity.this);
            }


    }

    private void getHospitalsListResponse(ProviderResponse hospitalsResponse) {
        dismissLoadingDialog();
        String description = hospitalsResponse.getMessage();
        if (hospitalsResponse.isSuccess()) {
            List<NewProviderList> dataItems = hospitalsResponse.getData();

            if (dataItems == null || dataItems.isEmpty()) {
                activityHospitalsListBinding.nodataimage.setVisibility(View.VISIBLE);
            } else {
                activityHospitalsListBinding.nodataimage.setVisibility(View.GONE);

                // Clear and update the list once
                responses.clear();
                responses.addAll(dataItems);
                hospitalsAdapter.notifyDataSetChanged();
            }

        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


}