package com.drcita.user;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
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
    private List<NewProviderList> responses = new ArrayList<>();
    private List<DoctorModel> doctorModelList = new ArrayList<>();

    private int regionID;
    private ProgressDialog progress;
    private int hospitalist;
    private boolean isfromdental = false;

    private final ArrayList<Integer> setcharges = new ArrayList<>();
    private final ArrayList<Integer> providerlist = new ArrayList<>();
    private final ArrayList<Integer> hospitallist = new ArrayList<>();
    private final ArrayList<Integer> specailizations = new ArrayList<>();

    private String cityId;
    private int specalization;

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHospitalsListBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_hospitals_list);

        if (savedInstanceState == null && getIntent().getExtras() != null) {
            hospitalist  = getIntent().getIntExtra("hospitalId", 0);
            specalization  =getIntent().getIntExtra("specialization",0);
            isfromdental = getIntent().getBooleanExtra(Constants.isfromdental, false);
        }

        setSupportActionBar(activityHospitalsListBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        cityId = sp.getString(Constants.CITY_ID, "");

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("By Hospital"));
        tabLayout.addTab(tabLayout.newTab().setText("By Specialization"));

        if ( hospitalist> 0  || specalization>0) {
            TabLayout.Tab second = tabLayout.getTabAt(1);
            if (second != null) second.select();
            activityHospitalsListBinding.hospitalsRV.setVisibility(GONE);
            if(hospitalist>0) {  // for getting hospital list
                hospitallist.add(hospitalist);
            }
            if(specalization>0)
            providerlist.add(specalization);

            displaySpecailizations();
        } else {
            TabLayout.Tab first = tabLayout.getTabAt(0);
            if (first != null) first.select();

            activityHospitalsListBinding.hospitalsRV.setVisibility(VISIBLE);
            activityHospitalsListBinding.docterssRV.setVisibility(GONE);
            displayHospitals();
        }
        activityHospitalsListBinding.llBack.setOnClickListener(view -> finish());

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    displayHospitals();
                } else if (tab.getPosition() == 1) {
                    hospitalist = 0;
                    specalization=0;
                    displaySpecailizations();
                }
            }
            public void onTabUnselected(TabLayout.Tab tab) {}
            public void onTabReselected(TabLayout.Tab tab) {}
        });
    }

    private void displaySpecailizations() {
        try {
            activityHospitalsListBinding.hospitalsRV.setVisibility(GONE);
            activityHospitalsListBinding.docterssRV.setVisibility(VISIBLE);
            getSpecializations();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    private void getSpecializations() {
        showLoadingDialog();
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        regionID = sp.getInt(Constants.REGION, regionID);
        if (Constants.haveInternet(getApplicationContext())) {
            DoctorSearchRequest request = new DoctorSearchRequest(
                    Integer.parseInt(cityId), "", hospitallist,
                    new ArrayList<>(), providerlist, new ArrayList<>(), "", "",
                    new ArrayList<>(), new DoctorSearchRequest.Experience("", ""), "", ""
            );

            ApiClient.getRestAPI().getDocterList(request).enqueue(new Callback<DocterModelResponse>() {
                @Override
                public void onResponse(@NonNull Call<DocterModelResponse> call, @NonNull retrofit2.Response<DocterModelResponse> response) {
                    if (response.body() != null && !response.body().getData().isEmpty()) {
                        if(response.code()==200) {
                            activityHospitalsListBinding.tvNodata.setVisibility(GONE);
                            activityHospitalsListBinding.docterssRV.setVisibility(VISIBLE);
                            providerlist.clear();
                            hospitallist.clear();
                            getSpecilaizationResponse(response.body());
                            dismissLoadingDialog();
                        }
                        else {
                            Constants.displayError(response.errorBody().toString(),getBaseContext());
                            activityHospitalsListBinding.tvNodata.setVisibility(VISIBLE);
                            activityHospitalsListBinding.docterssRV.setVisibility(GONE);
                            dismissLoadingDialog();
                        }
                    } else {
                        activityHospitalsListBinding.tvNodata.setVisibility(VISIBLE);
                        activityHospitalsListBinding.docterssRV.setVisibility(GONE);
                        dismissLoadingDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<DocterModelResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(this);
        }
    }

    private void displayHospitals() {
        try {
            activityHospitalsListBinding.hospitalsRV.setVisibility(VISIBLE);
            activityHospitalsListBinding.docterssRV.setVisibility(GONE);
            activityHospitalsListBinding.backHospitalsList.setOnClickListener(view -> finish());
            activityHospitalsListBinding.hospitalsRV.setLayoutManager(new LinearLayoutManager(this));
            hospitalsAdapter = new HospitalsAdapter(this, responses, isfromdental);
                activityHospitalsListBinding.hospitalsRV.setAdapter(hospitalsAdapter);
                activityHospitalsListBinding.searcautoCompleteTextView.setHint("Search By Hospital Name");
                activityHospitalsListBinding.searcautoCompleteTextView.setThreshold(1);
                activityHospitalsListBinding.searcautoCompleteTextView.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }

                    @SuppressLint("NotifyDataSetChanged")
                    public void afterTextChanged(Editable s) {
                        if(s.length()>0) {
                            hospitalsAdapter.getFilter().filter(s.toString());
                            hospitalsAdapter.notifyDataSetChanged();
                        }
                    }
                });

            getHospitalsList();
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void getHospitalsList() {
        showLoadingDialog();
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        regionID = sp.getInt(Constants.REGION, regionID);

        if (Constants.haveInternet(getApplicationContext())) {
            ProvidersRequestData data = new ProvidersRequestData(Integer.parseInt(cityId), "", setcharges, specailizations,"");
            ApiClient.getRestAPI().getProviderResponse(data).enqueue(new Callback<ProviderResponse>() {
                public void onResponse(@NonNull Call<ProviderResponse> call, @NonNull retrofit2.Response<ProviderResponse> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                        getHospitalsListResponse(response.body());
                        activityHospitalsListBinding.tvNodata.setVisibility(GONE);
                        activityHospitalsListBinding.hospitalsRV.setVisibility(VISIBLE);
                    } else {
                        activityHospitalsListBinding.tvNodata.setVisibility(VISIBLE);
                        activityHospitalsListBinding.hospitalsRV.setVisibility(GONE);
                    }
                    dismissLoadingDialog();
                }

                public void onFailure(@NonNull Call<ProviderResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(this);
        }
    }

    private void getSpecilaizationResponse(DocterModelResponse response) {
        try {
            dismissLoadingDialog();
            if (response.isSuccess()) {
                doctorModelList.clear();
                doctorModelList.addAll(response.getData());
                activityHospitalsListBinding.nodataimage.setVisibility(doctorModelList.isEmpty() ? VISIBLE : GONE);
                activityHospitalsListBinding.docterssRV.setLayoutManager(new LinearLayoutManager(this));
                doctorAdapter = new DoctorAdapter(this, doctorModelList);
                activityHospitalsListBinding.docterssRV.setAdapter(doctorAdapter);
                activityHospitalsListBinding.searcautoCompleteTextView.setHint("Search By Doctor Name or Specialization");
                activityHospitalsListBinding.searcautoCompleteTextView.setThreshold(1);
                activityHospitalsListBinding.searcautoCompleteTextView.addTextChangedListener(new TextWatcher() {
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    }

                    public void onTextChanged(CharSequence s, int start, int before, int count) {
                    }
                    @SuppressLint("NotifyDataSetChanged")
                    public void afterTextChanged(Editable s) {
                        doctorAdapter.getFilter().filter(s.toString());
                        doctorAdapter.notifyDataSetChanged();
                    }
                });
            } else {
                Snackbar.make(findViewById(android.R.id.content), response.getMessage(), Snackbar.LENGTH_SHORT).show();
            }
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void getHospitalsListResponse(ProviderResponse response) {
        dismissLoadingDialog();
        if (response.isSuccess()) {
            responses.clear();
            responses.addAll(response.getData());
            activityHospitalsListBinding.nodataimage.setVisibility(responses.isEmpty() ? VISIBLE : GONE);
            hospitalsAdapter.notifyDataSetChanged();
        } else {
            Snackbar.make(findViewById(android.R.id.content), response.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }
}
