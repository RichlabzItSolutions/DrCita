package com.drcita.user;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import androidx.annotation.NonNull;
import androidx.collection.ArraySet;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import com.drcita.user.adapter.HospitalsAdapter;
import com.drcita.user.adapter.specilaization.DoctorAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityHospitalsListBinding;
import com.drcita.user.filter.DoctorFilterBottomSheet;
import com.drcita.user.models.newProviderlist.NewProviderList;
import com.drcita.user.models.newProviderlist.ProviderResponse;
import com.drcita.user.models.newProviderlist.ProvidersRequestData;
import com.drcita.user.models.specilization.DocterModelResponse;
import com.drcita.user.models.specilization.DoctorModel;
import com.drcita.user.models.specilization.DoctorSearchRequest;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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

    private final ArrayList<Integer> docterlist = new ArrayList<>();
    private final ArrayList<Integer> specailizations = new ArrayList<>();

    private String cityId;
    private int specalization,doctorId;
    private static final int FILTER_REQUEST_CODE = 101;

    private int selectedTabPosition = 0;
    private String gender="";
    private String experience="";
    String tolatitude="",tolongitude="";
    private String minExperience,maxExperience;
    private ArrayList<Integer> consultationModes=new ArrayList<>();

    @SuppressLint("SuspiciousIndentation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHospitalsListBinding = DataBindingUtil.setContentView(this,
                R.layout.activity_hospitals_list);

        if (savedInstanceState == null && getIntent().getExtras() != null) {
            hospitalist  = getIntent().getIntExtra("hospitalId", 0);
            specalization  =getIntent().getIntExtra("specialization",0);
            doctorId=getIntent().getIntExtra("doctorId",0);
            isfromdental = getIntent().getBooleanExtra(Constants.isfromdental, false);
        }

        setSupportActionBar(activityHospitalsListBinding.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);

        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        cityId = sp.getString(Constants.CITY_ID, "");

        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("By Hospital"));
        tabLayout.addTab(tabLayout.newTab().setText("By Specialization"));

        if ( hospitalist> 0  || specalization>0 || doctorId>0) {
            TabLayout.Tab second = tabLayout.getTabAt(1);
            if (second != null) second.select();
            activityHospitalsListBinding.hospitalsRV.setVisibility(GONE);
            if(hospitalist>0) {  // for getting hospital list
                hospitallist.add(hospitalist);
            }
            if(specalization>0)
            providerlist.add(specalization);
           if(doctorId>0)
           {
               docterlist.add(doctorId);
           }
            selectedTabPosition=1;
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

                selectedTabPosition = tab.getPosition();
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
        activityHospitalsListBinding.ivFilter.setOnClickListener(view -> {
            Intent intent = new Intent(HospitalsListActivity.this, DoctorFilterBottomSheet.class);
            startActivityForResult(intent, FILTER_REQUEST_CODE); // only this
            overridePendingTransition(R.anim.slide_in_bottom, R.anim.no_anim);
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
                    docterlist, providerlist, consultationModes, "", gender,
                    new ArrayList<>(), new DoctorSearchRequest.Experience(minExperience, maxExperience), "", "",
                    tolatitude,tolongitude
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
                            activityHospitalsListBinding.tvNodata.setText("No Docters Found !!");
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

                            hospitalsAdapter.getFilter().filter(s.toString());
                            hospitalsAdapter.notifyDataSetChanged();


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
            ProvidersRequestData data = new ProvidersRequestData(Integer.parseInt(cityId), "", setcharges, specailizations,"",tolatitude,tolongitude);
            ApiClient.getRestAPI().getProviderResponse(data).enqueue(new Callback<ProviderResponse>() {
                public void onResponse(@NonNull Call<ProviderResponse> call, @NonNull retrofit2.Response<ProviderResponse> response) {
                    if (response.isSuccessful() && response.body() != null && !response.body().getData().isEmpty()) {
                        getHospitalsListResponse(response.body());
                        activityHospitalsListBinding.tvNodata.setVisibility(GONE);
                        activityHospitalsListBinding.hospitalsRV.setVisibility(VISIBLE);
                    } else {
                        activityHospitalsListBinding.tvNodata.setVisibility(VISIBLE);
                        activityHospitalsListBinding.tvNodata.setText("No Hospitals Found !!");
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
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == FILTER_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String filterJson = data.getStringExtra("filters");

            Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
            Map<String, List<String>> selectedFilters = new Gson().fromJson(filterJson, type);
        // 2) Get the latitude & longitude extras
            String lat = data.getStringExtra("latitude");
            String lng = data.getStringExtra("longitude");
            if (selectedTabPosition == 0) {
                // 👇 Tab 0: By Hospital
                applyHospitalFilters(selectedFilters,lat,lng);
            } else if (selectedTabPosition == 1) {
                // 👇 Tab 1: By Specialization
                applyDoctorFilters(selectedFilters,lat,lng);
            }
        }
    }
    private void applyHospitalFilters(Map<String, List<String>> filters, String lat, String lng) {
        setcharges.clear();
        specailizations.clear();
        consultationModes.clear();

        if (filters.containsKey("consultation")) {
            List<String> consultation = filters.get("consultation");
            for (String mode : consultation) {
                try {
                    consultationModes.add(Integer.parseInt(mode)); // ✅ Parse each mode
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Optionally handle invalid input
                }
            }
        }

        if (filters.containsKey("specialization")) {
            for (String id : filters.get("specialization")) {
                specailizations.add(Integer.parseInt(id));
            }
        }

        if (filters.containsKey("set_charge")) {
            for (String id : filters.get("set_charge")) {
                setcharges.add(Integer.parseInt(id));
            }
        }
        if((lat!=null) || (lng!=null)) {
            tolatitude = lat ;
            tolongitude = lng;
        }
        else
        {
            tolatitude="";
            tolongitude="";
        }

        displayHospitals(); // reload
    }

    private void applyDoctorFilters(Map<String, List<String>> filters, String latitude,
                                    String longitude) {
        providerlist.clear();
        hospitallist.clear();
        docterlist.clear();
        minExperience="";
        maxExperience="";
        consultationModes.clear();
        if (filters.containsKey("consultation")) {
            List<String> consultation = filters.get("consultation");
            for (String mode : consultation) {
                try {
                    consultationModes.add(Integer.parseInt(mode)); // ✅ Parse each mode
                } catch (NumberFormatException e) {
                    e.printStackTrace(); // Optionally handle invalid input
                }
            }
        }
        if (filters.containsKey("specialization")) {
            for (String id : filters.get("specialization")) {
                providerlist.add(Integer.parseInt(id));
            }
        }

        if (filters.containsKey("hospital")) {
            for (String id : filters.get("hospital")) {
                hospitallist.add(Integer.parseInt(id));
            }
        }

        if (filters.containsKey("doctor")) {
            for (String id : filters.get("doctor")) {
                docterlist.add(Integer.parseInt(id));
            }
        }
        if (filters.containsKey("gender")) {
            for (String id : filters.get("gender")) {
                gender = id;
            }
        }
        if (filters.containsKey("experience")) {
            String range = filters.get("experience").get(0); // e.g., "3-12"
            if (range.contains("-")) {
                String[] parts = range.split("-");
                minExperience = parts[0];
                maxExperience = parts[1];

                experience = String.valueOf(new DoctorSearchRequest.Experience(minExperience, maxExperience));
            }
            else
            {
                minExperience="";
                maxExperience="";
            }
        }

        if (filters.containsKey("city"))

            for (String id : filters.get("city")) {
                cityId = id;
            }
        if((latitude!=null) || (longitude!=null)) {
            tolatitude = latitude ;
            tolongitude = longitude;
        }
        else
        {
            tolatitude="";
            tolongitude="";
        }

        displaySpecailizations(); // reload
    }

}
