package com.drcita.user;

import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.drcita.user.adapter.DiseaseAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityNewProfileBinding;
import com.drcita.user.models.cities.CityRequestData;
import com.drcita.user.models.cities.CityResponse;
import com.drcita.user.models.profile.AddProfile;
import com.drcita.user.models.profile.BloodGroup;
import com.drcita.user.models.profile.BloodGroupResponse;
import com.drcita.user.models.profile.Disease;
import com.drcita.user.models.profile.DiseaseResponse;
import com.drcita.user.models.profile.DiseaseStatus;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.ProfileResponse;
import com.drcita.user.models.profile.RelationResponse;
import com.drcita.user.models.profile.UpdateMemberRequest;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.models.states.StateRequest;
import com.drcita.user.models.states.StateResponse;
import com.drcita.user.models.userprofile.MemeberProfileRequest;
import com.drcita.user.models.userprofile.ProfileDiseaseResponse;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileUpdateActivity extends LanguageBaseActivity {
    ActivityNewProfileBinding activityNewvprofileBinding;
    private int genderId, foodtypeId = 2, martialStausId;
    List<RelationResponse.Relation> relationshipdata;
    List<CityResponse.City> cityList = new ArrayList<>();
    List<StateResponse.State> stateList = new ArrayList<>();
    List<BloodGroup> bloodGroup = new ArrayList<>();


    private List<Disease> data;
    List<String> stateNames = new ArrayList<>();
    List<String> cityNames = new ArrayList<>();

    List<String> bloodGroups = new ArrayList<>();

    int selectedStateId = -1;
    int bloodgroupId = -1;
    int pastsurgeryid;
    int selectCityId = -1;
    String selectedStateName = "", selectedCityName = "", bloodgroup = "";


    //adapter
    DiseaseAdapter adapter;
    private int relationId;
    private String from, subuserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNewvprofileBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_profile);
        setupGender();
        setUpRelationShips();
        setupdateofBirth();
        setUpMaritalStatus();
        setupStateandCities();
        // setupDisease();
        setupBloodGroup();
        setUpPreviousSurgeryStatus();
        setupFoodPreference();
        activityNewvprofileBinding.llBack.setOnClickListener(view -> finish());

        activityNewvprofileBinding.btnUpdateProfile.setOnClickListener(view -> submitDataToServer());
        activityNewvprofileBinding.llAddmemeber.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileUpdateActivity.this, MembersListActivity.class);
            startActivity(intent);

        });

        if (getIntent().hasExtra("from")) {
            from = getIntent().getStringExtra("from");
            if (from.equals("profile")) {
                subuserId = getIntent().getStringExtra("subuserId");
                membersProfile(subuserId);
                activityNewvprofileBinding.llAddmemeber.setVisibility(View.GONE);
                activityNewvprofileBinding.tvMemberheader.setVisibility(View.VISIBLE);
                activityNewvprofileBinding.llMemberdetailswhom.setVisibility(View.VISIBLE);
                activityNewvprofileBinding.tvTitle.setText("Member Details");
                activityNewvprofileBinding.tvMemberheader.setText("Profile Details");
                activityNewvprofileBinding.tvMemberheader.setPaintFlags(activityNewvprofileBinding.tvMemberheader.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

            }
        } else {
            activityNewvprofileBinding.tvTitle.setText("My Profile");
            activityNewvprofileBinding.llAddmemeber.setVisibility(View.VISIBLE);
            activityNewvprofileBinding.tvMemberheader.setVisibility(View.GONE);
            activityNewvprofileBinding.llMemberdetailswhom.setVisibility(View.GONE);
            // retrieve user profile data
            setUpProfile();
        }
    }

    private void membersProfile(String subuserId) {
        try {

            try {
                if (Constants.haveInternet(getApplicationContext())) {
                    showLoadingDialog();
                    MemeberProfileRequest request = new MemeberProfileRequest();
                    request.setSubUserId(Integer.parseInt(subuserId));
                    ApiClient.getRestAPI().getProfileMember(request).enqueue(new Callback<ProfileDiseaseResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<ProfileDiseaseResponse> call, @NonNull Response<ProfileDiseaseResponse> response) {
                            dismissLoadingDialog();
                            if (response.isSuccessful()) {
                                if (response.isSuccessful() && response.body() != null) {
                                    getMembersProfile(response.body());


                                }

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<ProfileDiseaseResponse> call, @NonNull Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                            dismissLoadingDialog();
                        }
                    });
                }

            } catch (Exception ex) {
                ex.getMessage();
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void getMembersProfile(ProfileDiseaseResponse profile) {
        try {

            activityNewvprofileBinding.etFullname.setText(profile.getData().getProfile().getFullName());
            activityNewvprofileBinding.spinnerGender.setSelection(profile.getData().getProfile().getGender());
            activityNewvprofileBinding.etAge.setText(String.valueOf(profile.getData().getProfile().getAge()));

            activityNewvprofileBinding.etDOB.setText(profile.getData().getProfile().getDob());

            activityNewvprofileBinding.spinnerRelation.setSelection((profile.getData().getProfile().getMaritalStatus() - 1));

            activityNewvprofileBinding.etmobile.setText(profile.getData().getProfile().getMobile());
            activityNewvprofileBinding.etemail.setText(profile.getData().getProfile().getEmail());

            activityNewvprofileBinding.etAddress.setText(profile.getData().getProfile().getAddress());
            activityNewvprofileBinding.sppastsurgeries.setSelection(profile.getData().getProfile().getPastSurgeries());
            List<Disease> apiDiseases = profile.getData().getDiseases();
            for (Disease disease : apiDiseases) {
                disease.setSelection(disease.getStatus()); // Set it ONCE
            }
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            activityNewvprofileBinding.rvDiseases.setLayoutManager(layoutManager);
            adapter = new DiseaseAdapter(profile.getData().getDiseases());
            activityNewvprofileBinding.rvDiseases.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            int selectedIndex = -1;
            int selectedStateId = profile.getData().getProfile().getStateId();
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).getId() == selectedStateId) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex != -1) {
                activityNewvprofileBinding.spinnerStates.setSelection(selectedIndex + 1);
            }

            // get Blood Group
            int selectBloodGroupId = profile.getData().getProfile().getBloodGroupId();
            for (int i = 0; i < bloodGroup.size(); i++) {
                if (bloodGroup.get(i).getId() == selectBloodGroupId) {
                    bloodgroupId = i;
                    break;
                }
            }
            if (bloodgroupId != -1) {
                activityNewvprofileBinding.spbloodgroup.setSelection(bloodgroupId + 1);
            }
        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void setUpProfile() {
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        String userId = sp.getString(Constants.USER_ID, "");

        try {
            if (Constants.haveInternet(getApplicationContext())) {
                showLoadingDialog();
                GetProfileRequest request = new GetProfileRequest();
                request.setUserId(Integer.parseInt(userId));

                ApiClient.getRestAPI().getProfile(request).enqueue(new Callback<ProfileResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ProfileResponse> call, @NonNull Response<ProfileResponse> response) {
                        dismissLoadingDialog();
                        if (response.isSuccessful()) {
                            if (response.isSuccessful() && response.body() != null) {

                                getUserProfileResponse(response.body());
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ProfileResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                        dismissLoadingDialog();
                    }
                });
            }

        } catch (Exception e) {
            e.getMessage();
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private void getUserProfileResponse(ProfileResponse profile) {

        try {

            activityNewvprofileBinding.etFullname.setText(profile.getData().getProfile().getFullName());
            activityNewvprofileBinding.spinnerGender.setSelection(profile.getData().getProfile().getGender());
            activityNewvprofileBinding.etAge.setText(String.valueOf(profile.getData().getProfile().getAge()));

            activityNewvprofileBinding.etDOB.setText(profile.getData().getProfile().getDob());

            activityNewvprofileBinding.spinnerRelation.setSelection((profile.getData().getProfile().getMaritalStatus() - 1));

            activityNewvprofileBinding.etmobile.setText(profile.getData().getProfile().getMobile());
            activityNewvprofileBinding.etemail.setText(profile.getData().getProfile().getEmail());

            activityNewvprofileBinding.etAddress.setText(profile.getData().getProfile().getAddress());
            activityNewvprofileBinding.sppastsurgeries.setSelection(profile.getData().getProfile().getPastSurgeries());
            List<Disease> apiDiseases = profile.getData().getDiseases();
            for (Disease disease : apiDiseases) {
                disease.setSelection(disease.getStatus()); // Set it ONCE
            }
            GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
            activityNewvprofileBinding.rvDiseases.setLayoutManager(layoutManager);
            adapter = new DiseaseAdapter(profile.getData().getDiseases());
            activityNewvprofileBinding.rvDiseases.setAdapter(adapter);
            adapter.notifyDataSetChanged();

            int selectedIndex = -1;
            int selectedStateId = profile.getData().getProfile().getStateId();
            for (int i = 0; i < stateList.size(); i++) {
                if (stateList.get(i).getId() == selectedStateId) {
                    selectedIndex = i;
                    break;
                }
            }

            if (selectedIndex != -1) {
                activityNewvprofileBinding.spinnerStates.setSelection(selectedIndex + 1);
            }


            // get Blood Group

            int selectBloodGroupId = profile.getData().getProfile().getBloodGroupId();
            for (int i = 0; i < bloodGroup.size(); i++) {
                if (bloodGroup.get(i).getId() == selectBloodGroupId) {
                    bloodgroupId = i;
                    break;
                }
            }

            if (bloodgroupId != -1) {
                activityNewvprofileBinding.spbloodgroup.setSelection(bloodgroupId + 1);
            }


        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    private void setupFoodPreference() {
        try {

            // Set default selection
            activityNewvprofileBinding.foodGroup.check(R.id.rbNonVeg);

            // Listener for selection change
            activityNewvprofileBinding.foodGroup.setOnCheckedChangeListener((group, checkedId) -> {
                if (checkedId == R.id.rbVeg) {
                    foodtypeId = 1;
                } else if (checkedId == R.id.rbNonVeg) {
                    foodtypeId = 2;
                    // Toast.makeText(this, "Non-Veg selected", Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    private void setUpPreviousSurgeryStatus() {
        try {

            List<String> genderList = Arrays.asList("No", "Yes");
            // Gender variable
            final int[] pastSurgeryStatus = {1}; // Default to unmarried

            // Gender Spinner
            ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, genderList);
            activityNewvprofileBinding.sppastsurgeries.setAdapter(genderAdapter);
            activityNewvprofileBinding.sppastsurgeries.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    switch (pos) {
                        case 0: // unMarried
                            pastSurgeryStatus[0] = 0;
                            pastsurgeryid = 0;
                            break;
                        case 1: // married
                            pastSurgeryStatus[0] = 1;
                            pastsurgeryid = 1;
                            break;

                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void setupBloodGroup() {

        try {
            fetchBloodGroups();

        } catch (Exception e) {
            e.getMessage();
        }
    }

    private void fetchBloodGroups() {

        try {
            if (Constants.haveInternet(getApplicationContext())) {
                showLoadingDialog();

                ApiClient.getRestAPI().fetchBloodgroups().enqueue(new Callback<BloodGroupResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<BloodGroupResponse> call, @NonNull Response<BloodGroupResponse> response) {
                        dismissLoadingDialog();
                        if (response.isSuccessful()) {
                            if (response.isSuccessful() && response.body() != null) {

                                getBloodGroupResponse(response.body());
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<BloodGroupResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                        dismissLoadingDialog();
                    }
                });
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void submitDataToServer() {
        try {

            if (activityNewvprofileBinding.etFullname.getText().length() == 0) {

                activityNewvprofileBinding.etFullname.setError("Please enter full name");
                return;
            } else if (activityNewvprofileBinding.etAge.getText().length() == 0 && activityNewvprofileBinding.etDOB.getText().length() == 0) {
                Toast.makeText(this, "Enter your age  or  dob", LENGTH_SHORT).show();
            } else if ((activityNewvprofileBinding.etmobile.getText().length() == 0) && (!activityNewvprofileBinding.etmobile.getText().toString().matches(Constants.MobilePatternzaadevcezad))) {
                activityNewvprofileBinding.etmobile.setError("Please enter your  valid mobile number");
            } else if ((activityNewvprofileBinding.etemail.getText().length() == 0) && (!isValidEmail(activityNewvprofileBinding.etemail.getText().toString()))) {
                // Valid email
                activityNewvprofileBinding.etemail.setError("Please enter a valid email address");
            } else if (selectedStateName.equals("Select State") && selectedCityName.equals("Select City")) {

                Toast.makeText(this, "Please select both state and city", LENGTH_SHORT).show();
            } else if (bloodgroupId == -1) {
                Toast.makeText(this, "Please select blood group", LENGTH_SHORT).show();
            } else {

                if (from != null) {

                    calltoMemberAPI();
                } else {
                    calltoAPI();
                    Toast.makeText(this, "Please select both state and city", LENGTH_SHORT).show();
                }
            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void calltoMemberAPI() {
        try {
            List<DiseaseStatus> selectedDiseases = adapter.getSelectedDiseases();
            UpdateMemberRequest profile = new UpdateMemberRequest(
                    Integer.parseInt(subuserId),
                    activityNewvprofileBinding.etFullname.getText().toString(),
                    genderId,
                    Integer.parseInt(activityNewvprofileBinding.etAge.getText().toString()),
                    activityNewvprofileBinding.etDOB.getText().toString(),
                    martialStausId,
                    activityNewvprofileBinding.etmobile.getText().toString(),
                    activityNewvprofileBinding.etemail.getText().toString(),
                    selectedStateId,
                    selectCityId,
                    activityNewvprofileBinding.etAddress.getText().toString(),
                    pastsurgeryid,
                    bloodgroupId,
                    relationId,
                    foodtypeId,
                    selectedDiseases
            );


            if (Constants.haveInternet(getApplicationContext())) {
                showLoadingDialog();
                ApiClient.getRestAPI().updateMember(profile).enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RestResponse> call, @NonNull Response<RestResponse> response) {
                        dismissLoadingDialog();
                        if (response.isSuccessful()) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(ProfileUpdateActivity.this, response.body().getMessage(), LENGTH_SHORT).show();

                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                        dismissLoadingDialog();
                    }
                });

            } else {
                Constants.haveInternet(ProfileUpdateActivity.this);
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void calltoAPI() {
        try {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String userId = sp.getString(Constants.USER_ID, "");
            List<DiseaseStatus> selectedDiseases = adapter.getSelectedDiseases();
            AddProfile profile = new AddProfile(
                    Integer.parseInt(userId),
                    activityNewvprofileBinding.etFullname.getText().toString(),
                    genderId,
                    Integer.parseInt(activityNewvprofileBinding.etAge.getText().toString()),
                    activityNewvprofileBinding.etDOB.getText().toString(),
                    martialStausId,
                    activityNewvprofileBinding.etmobile.getText().toString(),
                    activityNewvprofileBinding.etemail.getText().toString(),
                    selectedStateId,
                    selectCityId,
                    activityNewvprofileBinding.etAddress.getText().toString(),
                    pastsurgeryid,
                    bloodgroupId,
                    foodtypeId,
                    selectedDiseases
            );


            if (Constants.haveInternet(getApplicationContext())) {
                showLoadingDialog();
                ApiClient.getRestAPI().addProfile(profile).enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RestResponse> call, @NonNull Response<RestResponse> response) {
                        dismissLoadingDialog();
                        if (response.isSuccessful()) {
                            if (response.isSuccessful() && response.body() != null) {
                                Toast.makeText(ProfileUpdateActivity.this, response.body().getMessage(), LENGTH_SHORT).show();

                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                        dismissLoadingDialog();
                    }
                });

            } else {
                Constants.haveInternet(ProfileUpdateActivity.this);
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    private void setupDisease() {
        GridLayoutManager layoutManager = new GridLayoutManager(this, 1);
        activityNewvprofileBinding.rvDiseases.setLayoutManager(layoutManager);
        try {
            fetchDisease();

        } catch (Exception ex) {
            ex.getMessage();
        }

    }

    private void fetchDisease() {

        if (Constants.haveInternet(getApplicationContext())) {
            showLoadingDialog();

            ApiClient.getRestAPI().getDiseaseResponse().enqueue(new Callback<DiseaseResponse>() {
                @Override
                public void onResponse(@NonNull Call<DiseaseResponse> call, @NonNull Response<DiseaseResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        if (response.isSuccessful() && response.body() != null) {

                            getDiseaseResponse(response.body());
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<DiseaseResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });

        } else {
            Constants.haveInternet(ProfileUpdateActivity.this);
        }
    }

    private void getDiseaseResponse(DiseaseResponse body) {
        try {

            adapter = new DiseaseAdapter(body.getData());
            activityNewvprofileBinding.rvDiseases.setAdapter(adapter);
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void setupStateandCities() {
        try {
            stateNames.add("Select State");
            cityNames.add("Select City");
            ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, stateNames);
            ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cityNames);
            activityNewvprofileBinding.spinnerStates.setAdapter(stateAdapter);
            activityNewvprofileBinding.spinnerCitys.setAdapter(cityAdapter);
            fetchStates();
            activityNewvprofileBinding.spinnerStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos == 0) {
                        selectedStateId = -1;
                        cityNames.clear();
                        cityNames.add("Select City");
                        activityNewvprofileBinding.spinnerCitys.setAdapter(new ArrayAdapter<>(ProfileUpdateActivity.this, R.layout.spinner_item, cityNames));
                        return;
                    }

                    selectedStateName = stateNames.get(pos);
                    selectedStateId = stateList.get(pos - 1).getId(); // Subtract 1 because of "Select State"

                    fetchCities(selectedStateId);
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            activityNewvprofileBinding.spinnerCitys.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    if (pos == 0) {
                        selectCityId = -1;
                        selectedCityName = "";
                        return;
                    }
                    selectedCityName = cityNames.get(pos);
                    selectCityId = cityList.get(pos - 1).getId(); // Subtract 1 because of "Select State"
                }

                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void setUpMaritalStatus() {
        try {

            List<String> genderList = Arrays.asList("Unmarried", "Married");
            // Gender variable
            final int[] maritaklValue = {1}; // Default to unmarried

            // Gender Spinner
            ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, genderList);
            activityNewvprofileBinding.spinnerRelation.setAdapter(genderAdapter);


            activityNewvprofileBinding.spinnerRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                    switch (pos) {
                        case 0: // unMarried
                            maritaklValue[0] = 1;
                            martialStausId = 1;
                            break;
                        case 1: // married
                            maritaklValue[0] = 2;
                            martialStausId = 2;
                            break;
                        case 2: // Other
                            maritaklValue[0] = 3;
                            martialStausId = 3;
                            break;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void setupdateofBirth() {
        try {

            // Date Picker
            activityNewvprofileBinding.etDOB.setOnClickListener(v -> {
                Calendar calendar = Calendar.getInstance();

                DatePickerDialog datePicker = new DatePickerDialog(this,
                        (view1, year, month, day) -> {
                            // Format with leading zeroes
                            String formattedDate = String.format("%02d-%02d-%04d", day, month + 1, year);
                            activityNewvprofileBinding.etDOB.setText(formattedDate);
                        },
                        calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH)
                );

                datePicker.show();
            });

        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void getRelationships() {
        try {
            showLoadingDialog();

            if (Constants.haveInternet(getApplicationContext())) {

                ApiClient.getRestAPI().getRelationships().enqueue(new Callback<RelationResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RelationResponse> call, @NonNull
                    retrofit2.Response<RelationResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            displayRelatiomshipsResponse(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RelationResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(ProfileUpdateActivity.this);
            }
        } catch (Exception ignored) {
        }

    }

    private void displayRelatiomshipsResponse(List<RelationResponse.Relation> data) {
        relationshipdata = data;
        List<String> relation = new ArrayList<>();
        for (RelationResponse.Relation user : relationshipdata) {
            relation.add(user.getRelation());
        }

        //        // Relation Spinner
        ArrayAdapter<String> relationAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, relation);
        activityNewvprofileBinding.spWhom.setAdapter(relationAdapter);
        activityNewvprofileBinding.spWhom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                relationId = relationshipdata.get(pos).getId();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setUpRelationShips() {
        try {
            getRelationships();

        } catch (Exception ex) {

        }

    }

    private void setupGender() {
        // Setup gender spinner items
        List<String> genderList = Arrays.asList("Female", "Male", "Others");
        // Gender variable
        final int[] genderValue = {1}; // Default to Female

        // Gender Spinner
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, genderList);
        activityNewvprofileBinding.spinnerGender.setAdapter(genderAdapter);

        activityNewvprofileBinding.spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                switch (pos) {
                    case 0: // Female
                        genderValue[0] = 1;
                        genderId = 1;
                        break;
                    case 1: // Male
                        genderValue[0] = 2;
                        genderId = 2;
                        break;
                    case 2: // Other
                        genderValue[0] = 3;
                        genderId = 3;
                        break;
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }


        });


    }

    private void fetchStates() {
        // Use your API manager or Retrofit call
        if (Constants.haveInternet(getApplicationContext())) {
            showLoadingDialog();
            StateRequest otpRequest = new StateRequest();
            otpRequest.setCountryId(76);

            ApiClient.getRestAPI().getStates(otpRequest).enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(@NonNull Call<StateResponse> call, @NonNull Response<StateResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        if (response.body() != null)
                            if (response.isSuccessful() && response.body() != null) {
                                stateList = response.body().getData();

                                for (StateResponse.State state : stateList) {
                                    stateNames.add(state.getStateName());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileUpdateActivity.this, R.layout.spinner_item, stateNames);
                                adapter.setDropDownViewResource(R.layout.spinner_item);
                                activityNewvprofileBinding.spinnerStates.setAdapter(adapter);
                            } else {
                                try {
                                    Constants.displayError(response.errorBody().string(), getBaseContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<StateResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });

        } else {
            Constants.haveInternet(ProfileUpdateActivity.this);
        }
    }

    private void getBloodGroupResponse(BloodGroupResponse body) {

        try {
            bloodGroup = body.getData();

            bloodGroups.clear(); // Optional: avoid duplicates on reload
            bloodGroups.add("Select Blood Group"); // Add this at index 0

            for (BloodGroup bloodGroup1 : bloodGroup) {
                bloodGroups.add(bloodGroup1.getBloodGroup());
            }

            ArrayAdapter<String> bloodadapter = new ArrayAdapter<>(this, R.layout.spinner_item, bloodGroups);
            activityNewvprofileBinding.spbloodgroup.setAdapter(bloodadapter);

            activityNewvprofileBinding.spbloodgroup.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                    if (pos == 0) {
                        // Default option selected
                        bloodgroupId = -1;
                        bloodgroup = "";
                    } else {
                        BloodGroup selectedGroup = bloodGroup.get(pos - 1); // Adjust for "Select Blood Group"
                        bloodgroupId = selectedGroup.getId();
                        bloodgroup = selectedGroup.getBloodGroup();
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // Optional: Reset state if needed
                }
            });

        } catch (Exception exception) {
            exception.printStackTrace(); // Log the exception for debugging
        }


    }

    private void fetchCities(int stateId) {

        if (Constants.haveInternet(getApplicationContext())) {
            showLoadingDialog();
            CityRequestData otpRequest = new CityRequestData();
            otpRequest.setStateId(stateId);

            ApiClient.getRestAPI().getCities(otpRequest).enqueue(new Callback<CityResponse>() {
                @Override
                public void onResponse(@NonNull Call<CityResponse> call, @NonNull Response<CityResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        if (response.isSuccessful() && response.body() != null) {
                            cityList = response.body().getData();
                            cityNames.clear();
                            cityNames.add("Select City");

                            for (CityResponse.City city : cityList) {
                                cityNames.add(city.getCityName());
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<>(ProfileUpdateActivity.this, R.layout.spinner_item, cityNames);
                            adapter.setDropDownViewResource(R.layout.spinner_item);
                            activityNewvprofileBinding.spinnerCitys.setAdapter(adapter);
                        }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CityResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(ProfileUpdateActivity.this, t.getMessage(), LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });

        } else {
            Constants.haveInternet(ProfileUpdateActivity.this);
        }
    }

    public boolean isValidEmail(String email) {
        return email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

}


