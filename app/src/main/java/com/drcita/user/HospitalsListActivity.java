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
import com.drcita.user.adapter.specilaization.DoctorAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityHospitalsListBinding;
import com.drcita.user.models.newProviderlist.NewProviderList;
import com.drcita.user.models.newProviderlist.ProviderResponse;
import com.drcita.user.models.newProviderlist.ProvidersRequestData;
import com.drcita.user.models.specilization.DocterModelResponse;
import com.drcita.user.models.specilization.DoctorModel;
import com.drcita.user.models.specilization.DoctorSearchRequest;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class HospitalsListActivity extends LanguageBaseActivity {

    private HospitalsAdapter hospitalsAdapter;
    private DoctorAdapter doctorAdapter;
    private ActivityHospitalsListBinding activityHospitalsListBinding;
    private List<NewProviderList> responses = new ArrayList<NewProviderList>();

    private List<DoctorModel> doctorModelList=new ArrayList<>();
    private int region;
    int regionID;
    private ProgressDialog progress;
    private int specailization;
    boolean isfromdental = false;

    ArrayList<Integer> setcharges=new ArrayList<>();
    ArrayList<Integer> providerlist=new ArrayList<>();
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

        // Read your extra
        if (getIntent().hasExtra("specailization")) {
            specailization = getIntent().getIntExtra("specailization", 0);
        }
        TabLayout tabLayout = findViewById(R.id.tabLayout);


// Add two tabs manually
        tabLayout.addTab(tabLayout.newTab().setText("By Hospital"));
        tabLayout.addTab(tabLayout.newTab().setText("By Specialization"));
// **Immediately select the second tab if your condition is true**
        if (specailization > 0) {
            TabLayout.Tab second = tabLayout.getTabAt(1);
            if (second != null) second.select();
            providerlist.add(specailization);// hospitalId
            displaySpecailizations();

        } else {
            // otherwise default to first
            displayHospitals();
        }
// Handle tab selection
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                if (position == 0) {
                    displayHospitals();
                    // Show By Hospital content
                } else if (position == 1) {
                    // Show By Specialization cont

                    displaySpecailizations();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });



    }

    private void displaySpecailizations() {

        try {

            getSpecializations();

        }catch (Exception ex)
        {
            ex.getMessage();
        }

    }

    private void getSpecializations() {
        showLoadingDialog();
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        regionID = sp.getInt(Constants.REGION, regionID);
        if (Constants.haveInternet(getApplicationContext())) {
            DoctorSearchRequest request = new DoctorSearchRequest(
                    14,
                    "",
                    providerlist,  // hospitalId
                    new ArrayList<>(),  // specializationId
                    new ArrayList<>(),  // specializationId
                    new ArrayList<>(),  // consultationMode
                    "",
                    "",
                    new ArrayList<>(),  // languageIds
                    new DoctorSearchRequest.Experience("", ""),  // experience
                    "",
                    ""
            );

            ApiClient.getRestAPI().getDocterList(request).enqueue(new Callback<DocterModelResponse>() {
                @Override
                public void onResponse(@NonNull Call<DocterModelResponse> call, @NonNull retrofit2.Response<DocterModelResponse> response) {
                    if(response.body()!=null && response.body().getData().size()>0) {
                        getSpecilaizationResponse(Objects.requireNonNull(response.body()));
                    }

                }
                @Override
                public void onFailure(@NonNull Call<DocterModelResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(HospitalsListActivity.this);
        }

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
    private void getSpecilaizationResponse(DocterModelResponse hospitalsResponse) {
        dismissLoadingDialog();


        String description = hospitalsResponse.getMessage();
        if (hospitalsResponse.isSuccess()) {
            List<DoctorModel> dataItems = hospitalsResponse.getData();

            if (dataItems == null || dataItems.isEmpty()) {
                activityHospitalsListBinding.nodataimage.setVisibility(View.VISIBLE);
            } else {
                activityHospitalsListBinding.nodataimage.setVisibility(View.GONE);

                // Clear and update the list once
                doctorModelList.clear();
                doctorModelList.addAll(dataItems);
                activityHospitalsListBinding.backHospitalsList.setOnClickListener(view -> finish());
                LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
                activityHospitalsListBinding.hospitalsRV.setLayoutManager(mLayoutManager);
                doctorAdapter = new DoctorAdapter(this,doctorModelList);
                activityHospitalsListBinding.hospitalsRV.setAdapter(doctorAdapter);
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
                            doctorAdapter.getFilter().filter(s.toString());
                            doctorAdapter.notifyDataSetChanged();
                        }
                        catch (Exception ex)
                        {
                            ex.getMessage();
                        }
                    }
                });
                doctorAdapter.notifyDataSetChanged();
            }

        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
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