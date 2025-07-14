package com.drcita.user.filter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.HospitalsListActivity;
import com.drcita.user.R;
import com.drcita.user.adapter.search.CategoryTabAdapter;
import com.drcita.user.adapter.search.FilterOptionAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.models.cities.CityRequestData;
import com.drcita.user.models.cities.CityResponse;
import com.drcita.user.models.dashboard.specilization.Specialization;
import com.drcita.user.models.departments.SpecializationRequest;
import com.drcita.user.models.departments.SpecializationResponse;
import com.drcita.user.models.fliter.FilterCategoryModel;
import com.drcita.user.models.fliter.FilterOption;
import com.drcita.user.models.states.StateRequest;
import com.drcita.user.models.states.StateResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorFilterBottomSheet extends AppCompatActivity {

    private RecyclerView rvCategoryTabs, rvFilterOptions;
    private TextView txtTitle,etlocation;
    private ImageView ivClose;
    private EditText edtSearch;
    private CategoryTabAdapter categoryTabAdapter;
    private FilterOptionAdapter optionAdapter;

    private final List<FilterCategoryModel> categoryList = new ArrayList<>();
    private final Map<String, List<FilterOption>> optionMap = new HashMap<>();
    private List<Specialization> specializationList = new ArrayList<>();
    private FilterCategoryModel currentCategory;

    private SharedPreferences sharedPreferences;
    AppCompatButton btnClear, btnApply;
    private List<StateResponse.State> stateList;
    private static final String PREF_SELECTED_STATE_ID = "selected_state_id";
    private String stateId="";
    private String to_Latitude;
    private String to_Longitude;
    FusedLocationProviderClient mFusedLocationClient;
    int PERMISSION_ID = 44;
    Geocoder geocoder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter_bottom_sheet);
        Places.initialize(DoctorFilterBottomSheet.this, getResources().getString(R.string.mapkey));
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        rvCategoryTabs = findViewById(R.id.rvCategoryTabs);
        rvFilterOptions = findViewById(R.id.rvOptions);
        txtTitle = findViewById(R.id.tvFilterTitle);
        ivClose = findViewById(R.id.ivClose);
        edtSearch = findViewById(R.id.etSearch);
        etlocation = findViewById(R.id.etlocation);
        btnClear = findViewById(R.id.btnClear);
        btnApply = findViewById(R.id.btnApply);

        btnApply.setOnClickListener(view -> applyFilters());


        sharedPreferences = getSharedPreferences("filter_prefs", Context.MODE_PRIVATE);
        // clearAllSelections(); // ✅ Clear selections before loading filters
        ivClose.setOnClickListener(v -> finish());

        setupRecyclerViews();
        loadFilterCategories();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (optionAdapter != null) {
                    optionAdapter.filter(s.toString());
                }
            }
        });
        btnClear.setOnClickListener(v -> {
            clearAllSelections(); // clear from SharedPreferences and memory
            loadFilterOptions(currentCategory); // reload current category
            categoryTabAdapter.notifyDataSetChanged(); // refresh tab counts
        });

    }

    private void setupRecyclerViews() {
        categoryTabAdapter = new CategoryTabAdapter(this, categoryList, selectedCategory -> {
            currentCategory = selectedCategory;
            if ("city".equalsIgnoreCase(selectedCategory.getCategoryId())) {
                String selectedStateId = sharedPreferences.getString("selected_state_id", null);
                if (selectedStateId == null || selectedStateId.isEmpty()) {
                    Toast.makeText(this, "Please select a state first.", Toast.LENGTH_SHORT).show();
                    return; // ❌ block city tab
                }
            }
            txtTitle.setText("Filter By : " + selectedCategory.getCategoryName());
            loadFilterOptions(selectedCategory);
        });

        rvCategoryTabs.setLayoutManager(new LinearLayoutManager(this));
        rvCategoryTabs.setAdapter(categoryTabAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_line));
        rvCategoryTabs.addItemDecoration(divider);

        rvFilterOptions.setLayoutManager(new LinearLayoutManager(this));
    }


    private void loadFilterCategories() {
        categoryList.clear();
        categoryList.add(new FilterCategoryModel("Specialization", "specialization", true));
        categoryList.add(new FilterCategoryModel("Consultation", "consultation", false));
        categoryList.add(new FilterCategoryModel("Availability", "availability", false));
        categoryList.add(new FilterCategoryModel("Gender", "gender", false));
        categoryList.add(new FilterCategoryModel("Language", "language", false));
        categoryList.add(new FilterCategoryModel("Experience", "experience", false));
        categoryList.add(new FilterCategoryModel("State", "state", false));
        categoryList.add(new FilterCategoryModel("City", "city", false));
        categoryList.add(new FilterCategoryModel("Location", "location", false));
        categoryTabAdapter.notifyDataSetChanged();
        txtTitle.setText("Filter By : " + categoryList.get(0).getCategoryName());
        currentCategory = categoryList.get(0);
        loadFilterOptions(currentCategory);
    }

    private void loadFilterOptions(FilterCategoryModel category) {
        String key = category.getCategoryId();

        List<FilterOption> optionList = optionMap.containsKey(key) ? optionMap.get(key) : new ArrayList<>();

        if (optionList.isEmpty()) {
            optionList = new ArrayList<>();

            switch (key) {
                case "gender":
                    rvFilterOptions.setVisibility(VISIBLE);
                    edtSearch.setVisibility(GONE);
                    etlocation.setVisibility(GONE);
                    optionList.add(new FilterOption("1", "Male", false));
                    optionList.add(new FilterOption("2", "Female", false));
                    optionList.add(new FilterOption("3", "Other", false));
                    break;

                case "consultation":
                    rvFilterOptions.setVisibility(VISIBLE);
                    edtSearch.setVisibility(GONE);
                    etlocation.setVisibility(GONE);
                    optionList.add(new FilterOption("1", "Online", false));
                    optionList.add(new FilterOption("2", "In-Person", false));
                    optionList.add(new FilterOption("3", "Both", false));
                    break;

                case "availability":
                    rvFilterOptions.setVisibility(VISIBLE);
                    edtSearch.setVisibility(GONE);
                    etlocation.setVisibility(GONE);
                    optionList.add(new FilterOption("1", "Morning", true));
                    optionList.add(new FilterOption("2", "Afternoon", false));
                    optionList.add(new FilterOption("3", "Evening", false));
                    break;

                case "language":
                    rvFilterOptions.setVisibility(VISIBLE);
                    edtSearch.setVisibility(GONE);
                    etlocation.setVisibility(GONE);
                    optionList.add(new FilterOption("1", "English", true));
                    optionList.add(new FilterOption("2", "Telugu", false));
                    optionList.add(new FilterOption("3", "Hindi", false));
                    break;
                case "experience":
                    rvFilterOptions.setVisibility(VISIBLE);
                    edtSearch.setVisibility(GONE);
                    etlocation.setVisibility(GONE);
                    if (optionList.isEmpty()) {
                        optionList.add(new FilterOption("experience_range", "Experience Range", false));
                    }
                    break;
                case "specialization":
                    rvFilterOptions.setVisibility(VISIBLE);
                    edtSearch.setVisibility(VISIBLE);
                    etlocation.setVisibility(GONE);
                    loadOptionsFromApi(category);
                    return;
                case "state":
                    rvFilterOptions.setVisibility(VISIBLE);
                    loadStatesFromAPI(category);
                    return;
                case "city":
                    rvFilterOptions.setVisibility(VISIBLE);
                    String selectedStateId = sharedPreferences.getString("selected_state_id", null);
                    if (selectedStateId == null || selectedStateId.isEmpty()) {
                        Toast.makeText(this, "Please select a state first", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    stateId=selectedStateId;
                    int stated = Integer.parseInt(selectedStateId);
                    loadCitiesFromAPI(category);
                    return;
                case "location":
                    edtSearch.setVisibility(GONE);
                    etlocation.setVisibility(VISIBLE);
                    rvFilterOptions.setVisibility(GONE);

                    if (checkPermissions()) {
                        requestNewLocationData();
                        etlocation.setOnClickListener(view -> {
                            List<Place.Field> fieldList= Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                            Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,fieldList).build(DoctorFilterBottomSheet.this);
                            startActivityForResult(intent,100);
                        });
                    } else {
                        requestPermissions();
                    }
                    return;
            }

            if (key.equals("consultation") || key.equals("availability") || key.equals("language")) {
                category.setSelectedCount(1);
                categoryTabAdapter.notifyDataSetChanged();
            }

            optionMap.put(key, optionList);
        }

        updateSelectedCount(category, optionList);

        optionAdapter = new FilterOptionAdapter(this, optionList, sharedPreferences, isSingleChoice(key), count -> {
            category.setSelectedCount(count);
            categoryTabAdapter.notifyDataSetChanged();
        }, key, stateId -> {
            // ✅ Reload city list when new state selected

        });

        rvFilterOptions.setAdapter(optionAdapter);
    }

    private void loadStatesFromAPI(FilterCategoryModel category) {
        if (!Constants.haveInternet(getApplicationContext())) {
            Constants.haveInternet(DoctorFilterBottomSheet.this); // show no internet toast
            return;
        }

        StateRequest request = new StateRequest();
        request.setCountryId(76);

        ApiClient.getRestAPI().getStates(request).enqueue(new Callback<StateResponse>() {
            @Override
            public void onResponse(@NonNull Call<StateResponse> call, @NonNull Response<StateResponse> response) {
                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<StateResponse.State> states = response.body().getData();
                    List<FilterOption> options = new ArrayList<>();
                    for (StateResponse.State state : states) {
                        options.add(new FilterOption(String.valueOf(state.getId()), state.getStateName(), false));
                    }

                    optionMap.put(category.getCategoryId(), options);
                    updateSelectedCount(category, options);

                    optionAdapter = new FilterOptionAdapter(DoctorFilterBottomSheet.this, options, sharedPreferences, isSingleChoice(category.getCategoryId()), count -> {
                        category.setSelectedCount(count);
                        categoryTabAdapter.notifyDataSetChanged();
                    }, category.getCategoryId(),
                            sid -> {
                                stateId=sid;
                                Toast.makeText(DoctorFilterBottomSheet.this, "State ID: " + stateId, Toast.LENGTH_SHORT).show();
                               // loadCitiesFromAPI(new FilterCategoryModel("City", "city", false), stateId);
                            }
                    );

                    rvFilterOptions.setAdapter(optionAdapter);
                    optionAdapter.notifyDataSetChanged();
                } else {
                    try {
                        if (response.errorBody() != null)
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<StateResponse> call, @NonNull Throwable t) {
                Toast.makeText(DoctorFilterBottomSheet.this, t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadCitiesFromAPI(FilterCategoryModel category) {
        if (!Constants.haveInternet(getApplicationContext())) {
            Constants.haveInternet(DoctorFilterBottomSheet.this); // show no internet toast
            return;
        }

        ApiClient.getRestAPI().getCities(new CityRequestData(Integer.parseInt(stateId))).enqueue(new Callback<CityResponse>() {
            @Override
            public void onResponse(@NonNull Call<CityResponse> call, @NonNull Response<CityResponse> response) {
                if (response.code() == 204) {
                    // No Content
                    Toast.makeText(DoctorFilterBottomSheet.this, "No cities available for the selected state.", Toast.LENGTH_SHORT).show();
                    optionMap.put("city", new ArrayList<>()); // Clear city options
                    rvFilterOptions.setAdapter(null); // Optionally clear city list UI
                    return;
                }

                if (response.isSuccessful() && response.body() != null && response.body().getData() != null) {
                    List<CityResponse.City> cities = response.body().getData();
                    List<FilterOption> cityOptions = new ArrayList<>();
                    for (CityResponse.City city : cities) {
                        cityOptions.add(new FilterOption(String.valueOf(city.getId()), city.getCityName(), false));
                    }

                    optionMap.put("city", cityOptions);
                    updateSelectedCount(category, cityOptions);

                    optionAdapter = new FilterOptionAdapter(
                            DoctorFilterBottomSheet.this,
                            cityOptions,
                            sharedPreferences,
                            isSingleChoice("city"),
                            count -> {
                                category.setSelectedCount(count);
                                categoryTabAdapter.notifyDataSetChanged();
                            },
                            "city",
                            stateId -> {

                            }
                    );
                    rvFilterOptions.setAdapter(optionAdapter);
                    optionAdapter.notifyDataSetChanged();
                } else {
                    try {
                        if (response.errorBody() != null)
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Toast.makeText(DoctorFilterBottomSheet.this, "Failed to load cities", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NonNull Call<CityResponse> call, @NonNull Throwable t) {
                Toast.makeText(DoctorFilterBottomSheet.this, "Failed to load cities", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void loadOptionsFromApi(FilterCategoryModel category) {
        String key = category.getCategoryId();
        if (key.equals(""))
            if (!Constants.haveInternet(this)) return;
        SpecializationRequest request = new SpecializationRequest("");
        ApiClient.getRestAPI().getSpecoilizationByDepartment(request).enqueue(new Callback<SpecializationResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpecializationResponse> call, @NonNull Response<SpecializationResponse> response) {
                if (response.body() != null && response.body().getData() != null) {
                    specializationList.clear();
                    specializationList.addAll(response.body().getData());

                    List<FilterOption> specializationOptions = new ArrayList<>();
                    for (Specialization spec : specializationList) {
                        specializationOptions.add(new FilterOption(
                                String.valueOf(spec.getId()),
                                spec.getSpecialityName(),
                                false
                        ));
                    }

                    optionMap.put(key, specializationOptions);
                    updateSelectedCount(category, specializationOptions);

                    optionAdapter = new FilterOptionAdapter(DoctorFilterBottomSheet.this, specializationOptions, sharedPreferences, isSingleChoice(key), count -> {
                        category.setSelectedCount(count);
                        categoryTabAdapter.notifyDataSetChanged();
                    }, key, stateId -> {

                    }
                    );

                    rvFilterOptions.setAdapter(optionAdapter);
                    optionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpecializationResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateSelectedCount(FilterCategoryModel category, List<FilterOption> options) {
        int count = 0;
        for (FilterOption option : options) {
            if (option.isSelected()) count++;
        }
        category.setSelectedCount(count);
        categoryTabAdapter.notifyDataSetChanged();
    }

    private boolean isSingleChoice(String key) {
        return key.equals("gender") || key.equals("consultation") || key.equals("state") || key.equals("city");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    private void clearAllSelections() {
        // Clear from shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Clear selection status in memory
        for (List<FilterOption> options : optionMap.values()) {
            for (FilterOption option : options) {
                option.setSelected(false);
                option.setMin("");
                option.setMax("");
            }
        }

        // Reset count in each category
        for (FilterCategoryModel category : categoryList) {
            category.setSelectedCount(0);
        }
        // clear latitude and longtude
        to_Latitude="";
        to_Longitude="";


        if (optionAdapter != null) {
            optionAdapter.notifyDataSetChanged();
        }
        editor.remove("gender");
        editor.remove("experience");
        applyFilters();
    }

    private void applyFilters() {
        Map<String, List<String>> selectedFilters = new HashMap<>();

        for (Map.Entry<String, List<FilterOption>> entry : optionMap.entrySet()) {
            String categoryId = entry.getKey();
            List<FilterOption> options = entry.getValue();
            List<String> selectedValues = new ArrayList<>();

            if ("experience".equalsIgnoreCase(categoryId)) {
                // Handle experience range like "2-10"
                for (FilterOption option : options) {
                    if ("experience_range".equalsIgnoreCase(option.getId())) {
                        String min = option.getMin();
                        String max = option.getMax();
                        if (!min.isEmpty() || !max.isEmpty()) {
                            selectedValues.add(min + "-" + max);
                        }
                    }
                }
            } else if ("consultation".equalsIgnoreCase(categoryId)) {
                // ✅ Handle consultation mode selection
                for (FilterOption option : options) {
                    if (option.isSelected()) {
                        selectedValues.add(option.getId()); // e.g., "1" for Online, "2" for Offline
                    }
                }
            } else {
                // ✅ Handle other filters like specialization, gender, etc.
                for (FilterOption option : options) {
                    if (option.isSelected()) {
                        selectedValues.add(option.getId());
                    }
                }
            }

            // ✅ Add to map if there's a valid selection
            if (!selectedValues.isEmpty()) {
                selectedFilters.put(categoryId, selectedValues);
            }
        }

        // Debug: Print all selected filters
        for (Map.Entry<String, List<String>> entry : selectedFilters.entrySet()) {
            Log.d("FILTER_APPLIED", entry.getKey() + " = " + entry.getValue());
        }

        // Prepare result intent
        Intent resultIntent = new Intent();
        resultIntent.putExtra("filters", new Gson().toJson(selectedFilters));
        resultIntent.putExtra("latitude", to_Latitude != null ? to_Latitude : "");
        resultIntent.putExtra("longitude", to_Longitude != null ? to_Longitude : "");

        setResult(RESULT_OK, resultIntent);
        finish();
    }


    @SuppressLint("MissingPermission")
    private void requestNewLocationData() {
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(5);
        mLocationRequest.setFastestInterval(0);
        mLocationRequest.setNumUpdates(1);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }
    private final LocationCallback mLocationCallback = new LocationCallback() {

        @Override
        public void onLocationResult(LocationResult locationResult) {
            Location mLastLocation = locationResult.getLastLocation();
        }
    };

    private boolean checkPermissions() {
        return ActivityCompat.checkSelfPermission(Objects.requireNonNull(this), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        ActivityCompat.requestPermissions(Objects.requireNonNull(this), new String[]{
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSION_ID);
    }
    private boolean isLocationEnabled() {
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==100 && resultCode== Activity.RESULT_OK)
        {
            Place place= Autocomplete.getPlaceFromIntent(data);
            etlocation.setText(place.getAddress());
            to_Latitude= String.valueOf(place.getLatLng().latitude);
            to_Longitude= String.valueOf(place.getLatLng().longitude);

        }
        else if(resultCode== AutocompleteActivity.RESULT_ERROR)
        {
            Status status=Autocomplete.getStatusFromIntent(data);
            Toast.makeText(this,status.getStatusMessage(),Toast.LENGTH_SHORT).show();
        }
    }

    @SuppressLint("MissingPermission")
    private void getLastLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.getLastLocation().addOnCompleteListener(task -> {
                    Location location = task.getResult();
                    if (location == null) {
                        requestNewLocationData();
                    } else {
                        List<Address> addresses;
                        geocoder = new Geocoder(this, Locale.getDefault());
                        try {
                            addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Please turn on" + " your location...", Toast.LENGTH_LONG).show();
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
            }
        } else {
            requestPermissions();
        }
    }

    // If everything is alright then
    @Override
    public void
    onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_ID) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLastLocation();
            }
        }
    }
}


