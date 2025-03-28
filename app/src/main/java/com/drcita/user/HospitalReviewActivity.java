package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.drcita.user.adapter.HospitalReviewsAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityHospitalReviewBinding;
import com.drcita.user.models.hospitalreviews.DataItem;
import com.drcita.user.models.hospitalreviews.HospitalReviewRequest;
import com.drcita.user.models.hospitalreviews.HospitalReviewResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class HospitalReviewActivity extends LanguageBaseActivity {
    ActivityHospitalReviewBinding activityHospitalReviewBinding;
    String hospitalName;
    String userId;
    String rating;
    double ratingStar;
    String location;
    RecyclerView reviewListRV;
    HospitalReviewsAdapter hospitalReviewsAdapter;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private ProgressDialog progress;
    int providerId, time, ratingcount;
    TextView hospitalTitleName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityHospitalReviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_hospital_review);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                hospitalName = null;
                providerId = 0;
                rating = null;
                time = 0;
                ratingcount = 0;
                ratingStar = 0.0;
            } else {
                com.drcita.user.models.hospitals.DataItem dataItem = Parcels.unwrap(extras.getParcelable("dataItem"));
                hospitalName = dataItem.hospitalName;
                providerId = dataItem.providerId;
                rating = String.valueOf(dataItem.rating);
                location = dataItem.getRegion();
                ratingcount = dataItem.ratedCount;
                ratingStar = dataItem.getRating();
            }
        } else {
        }
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        setSupportActionBar(activityHospitalReviewBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        activityHospitalReviewBinding.hospitalTitleName.setText(hospitalName);
        activityHospitalReviewBinding.hospitalname.setText(hospitalName);
        activityHospitalReviewBinding.ratingTV.setText("" + rating);
        activityHospitalReviewBinding.rating.setRating((float) ratingStar);
        activityHospitalReviewBinding.location.setText(location);
        activityHospitalReviewBinding.ratingcount.setText("" + ratingcount);
        activityHospitalReviewBinding.ratingcountCustomerreview.setText("" + ratingcount);
        //getSupportActionBar().setTitle(hospitalName);
        activityHospitalReviewBinding.backHospitalreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityHospitalReviewBinding.hospitalname.setText(hospitalName);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        activityHospitalReviewBinding.reviewListRV.setLayoutManager(mLayoutManager);
        hospitalReviewsAdapter = new HospitalReviewsAdapter(this, responses);
        activityHospitalReviewBinding.reviewListRV.setAdapter(hospitalReviewsAdapter);
        activityHospitalReviewBinding.ratingedit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), WriteReview.class);
                intent.putExtra("providerId",providerId);
                startActivity(intent);
            }
        });
        getHospitalReviews();

    }

    private void getHospitalReviews() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            HospitalReviewRequest hospitalReviewRequest = new HospitalReviewRequest();
            hospitalReviewRequest.setProviderId(providerId);
            ApiClient.getRestAPI().getProviderReviews(hospitalReviewRequest).enqueue(new Callback<HospitalReviewResponse>() {
                @Override
                public void onResponse(@NonNull Call<HospitalReviewResponse> call, @NonNull retrofit2.Response<HospitalReviewResponse> response) {
                    getHospitalsReviewResponse(Objects.requireNonNull(response.body()));
                }

                @Override
                public void onFailure(@NonNull Call<HospitalReviewResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(HospitalReviewActivity.this);
        }
    }

    private void getHospitalsReviewResponse(HospitalReviewResponse hospitalReviewResponse) {
        dismissLoadingDialog();
        String description = hospitalReviewResponse.getMessage();
        if (hospitalReviewResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<DataItem> dataItems = hospitalReviewResponse.getData();
            for (int i = 0; i < dataItems.size(); i++) {
                responses.clear();
                responses.addAll(hospitalReviewResponse.getData());
                hospitalReviewsAdapter.notifyDataSetChanged();
            }
            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


}