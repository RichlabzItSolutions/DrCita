package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.adapter.CategoriesListAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityCategoriesListBinding;
import com.drcita.user.models.doctors.DataItem;
import com.drcita.user.models.doctors.DoctorsListResponse;
import com.drcita.user.models.hospitalreviews.HospitalReviewRequest;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class CategoriesListActivity extends LanguageBaseActivity {

    private ActivityCategoriesListBinding binding;
    private String providerId;
    private CategoriesListAdapter doctorsListAdapter;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private int id;
    private ProgressDialog progress;
    private int regionID;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_categories_list);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {

            } else {
                id = extras.getInt("id");

            }
        } else {
        }

        setSupportActionBar(binding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.backHospitalsDoctorsList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        binding.doctorsListRV.setLayoutManager(mLayoutManager);
        doctorsListAdapter = new CategoriesListAdapter(this,responses);
        binding.doctorsListRV.setAdapter(doctorsListAdapter);
    }
    @Override
    protected void onResume() {
        getDoctorsList();
        super.onResume();
    }
    private void getDoctorsList() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            regionID = sp.getInt(Constants.REGION, regionID);
            HospitalReviewRequest hospitalReviewRequest = new HospitalReviewRequest();
            hospitalReviewRequest.setProviderId(0);
            hospitalReviewRequest.setSpecialisationId(id);
            hospitalReviewRequest.setRegion(regionID);
            hospitalReviewRequest.setUserId(userId);
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
            Constants.InternetSettings(CategoriesListActivity.this);
        }
    }
    private void getHospitalsReviewResponse(DoctorsListResponse doctorsListResponse) {
        dismissLoadingDialog();
        String description = doctorsListResponse.getMessage();
        if (doctorsListResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<DataItem> dataItems = doctorsListResponse.getData();
            if (doctorsListResponse.getData().isEmpty()){
                binding.nodataimage.setVisibility(View.VISIBLE);
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