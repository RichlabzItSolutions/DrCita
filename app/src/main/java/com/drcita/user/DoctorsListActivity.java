package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.drcita.user.adapter.DoctorsListAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityDoctorsListBinding;
import com.drcita.user.models.GlobalRequest;
import com.drcita.user.models.doctors.DataItem;
import com.drcita.user.models.doctors.DoctorsListResponse;
import com.drcita.user.models.hospitalreviews.HospitalReviewRequest;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.GetProfileResponse;
import com.drcita.user.models.region.Response;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class DoctorsListActivity extends LanguageBaseActivity {


    private ActivityDoctorsListBinding activityDoctorsListBinding;
    private DoctorsListAdapter doctorsListAdapter;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private int region;
    private ProgressDialog progress;
    private int providerId;
    private String location;
    private String hospitalName;
    private int id =0 ;
    boolean isfromdental = false;
    private int regionID;
    private String userId;

    private List<com.drcita.user.models.region.DataItem> dataItems = new ArrayList<com.drcita.user.models.region.DataItem>();

    private ArrayList<String> regionList;
    private ArrayList<Integer> regionIdInList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityDoctorsListBinding = DataBindingUtil.setContentView(this,R.layout.activity_doctors_list);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                com.drcita.user.models.hospitals.DataItem dataItem = Parcels.unwrap(extras.getParcelable("dataItem"));
                providerId =  dataItem.getProviderId();
                SharedPreferences preferences = getApplicationContext().getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                preferences.edit().putString(Constants.PROVIDER_ID, String.valueOf(providerId)).apply();
                Log.d("providerId", String.valueOf(providerId));
                isfromdental = extras.getBoolean(Constants.isfromdental);
                location = dataItem.getRegion();
               hospitalName = dataItem.getHospitalName();
            }
        } else {
        }
        setSupportActionBar(activityDoctorsListBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activityDoctorsListBinding.backHospitalsDoctorsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityDoctorsListBinding.hospitalTitlename.setText(hospitalName);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        activityDoctorsListBinding.doctorsListRV.setLayoutManager(mLayoutManager);
        doctorsListAdapter = new DoctorsListAdapter(this,responses,location);
        activityDoctorsListBinding.doctorsListRV.setAdapter(doctorsListAdapter);


        getUserProfile();

    }

    private void getUserProfile() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            GetProfileRequest request = new GetProfileRequest();
            request.setUserId(Integer.parseInt(userId));
            ApiClient.getRestAPI().getUserProfile(request).enqueue(new Callback<GetProfileResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull retrofit2.Response<GetProfileResponse> response) {
                    getRegion();
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        region = response.body().getData().getRegion();
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
                public void onFailure(@NonNull Call<GetProfileResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    getRegion();
                    Toast.makeText(DoctorsListActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(DoctorsListActivity.this);
        }
    }

    private void getRegion() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            GlobalRequest globalRequest = new GlobalRequest();
            globalRequest.setUserId(userId);
            ApiClient.getRestAPI().city(globalRequest).enqueue(new Callback<Response>() {
                @Override
                public void onResponse(@NonNull Call<Response> call, @NonNull retrofit2.Response<Response> response) {
                    getcityResponse(Objects.requireNonNull(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<com.drcita.user.models.region.Response> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(DoctorsListActivity.this);
        }
    }

    private void getcityResponse(com.drcita.user.models.region.Response response) {
        dismissLoadingDialog();
        String description = response.getMessage();
        regionList = new ArrayList<String>();
        regionIdInList = new ArrayList<>();
        if (response.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<com.drcita.user.models.region.DataItem> accademicyearModels = response.getData();
            for (int i = 0; i < accademicyearModels.size(); i++) {
                responses.clear();
                for (int j = 0; j < response.getData().size(); j++) {
                    com.drcita.user.models.region.DataItem dataItem = response.getData().get(j);
                    if(dataItem.getId()==region) {
                        dataItem.setSelected(true);
                        SharedPreferences preferences =getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                        preferences.edit().putString(Constants.Currancy_Type, String.valueOf(dataItem.getCurrencyType())).apply();
                        getDoctorsList();
                    }

                }

            }
            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


    private void getDoctorsList() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            regionID = sp.getInt(Constants.REGION, regionID);
            HospitalReviewRequest hospitalReviewRequest = new HospitalReviewRequest();
            hospitalReviewRequest.setProviderId(providerId);
            hospitalReviewRequest.setRegion(regionID);
            hospitalReviewRequest.setUserId(userId);
            if(isfromdental){
                hospitalReviewRequest.setSpecialisationId(14);
            }else {
                hospitalReviewRequest.setSpecialisationId(id);
            }
            ApiClient.getRestAPI().getDoctorsList(hospitalReviewRequest).enqueue(new Callback<DoctorsListResponse>() {
                @Override
                public void onResponse(@NonNull Call<DoctorsListResponse> call, @NonNull retrofit2.Response<DoctorsListResponse> response) {
                    getHospitalsReviewResponse(Objects.requireNonNull(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<DoctorsListResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(DoctorsListActivity.this);
        }
    }
    private void getHospitalsReviewResponse(DoctorsListResponse doctorsListResponse) {
        dismissLoadingDialog();
        String description = doctorsListResponse.getMessage();
        if (doctorsListResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<com.drcita.user.models.doctors.DataItem> dataItems = doctorsListResponse.getData();
            if (doctorsListResponse.getData().isEmpty()){
                activityDoctorsListBinding.nodataimage.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < dataItems.size(); i++) {
                responses.clear();
                responses.addAll(doctorsListResponse.getData());
                doctorsListAdapter.notifyDataSetChanged();
            }
            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


}