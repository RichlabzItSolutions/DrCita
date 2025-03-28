package com.drcita.user;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.drcita.user.adapter.RegionAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityRegionBinding;
import com.drcita.user.models.GlobalRequest;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.GetProfileResponse;
import com.drcita.user.models.region.DataItem;
import com.drcita.user.models.region.Response;
import com.drcita.user.models.region.UpdateRegionRequest;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class RegionActivity extends LanguageBaseActivity {

    private ActivityRegionBinding activityRegionBinding;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private List<DataItem> dataItems = new ArrayList<DataItem>();
    private RegionAdapter regionAdapter;
    private ProgressDialog progress;
    private ArrayList<String> regionList;
    private ArrayList<Integer> regionIdInList;
    private String userId;
    private int region;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityRegionBinding = DataBindingUtil.setContentView(this, R.layout.activity_region);
        setSupportActionBar(activityRegionBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Region");
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, "0");
        activityRegionBinding.backRegion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        activityRegionBinding.regionRV.setLayoutManager(mLayoutManager);
        regionAdapter = new RegionAdapter(this, responses);
        activityRegionBinding.regionRV.setAdapter(regionAdapter);
        activityRegionBinding.updateRegionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int selectedId = -1;
                for (int i = 0; i < responses.size(); i++) {
                    if (responses.get(i).isSelected()) {
                        selectedId = responses.get(i).getId();
                        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                        preferences.edit().putString(Constants.Currancy_Type, String.valueOf(responses.get(i).getCurrencyType())).apply();
                        break;
                    }
                }
                updateRegion(selectedId);
            }
        });
        getUserProfile();
    }

    private void updateRegion(int selectedId) {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            UpdateRegionRequest updateRegionRequest = new UpdateRegionRequest();
            updateRegionRequest.setUserId(Integer.parseInt(sp.getString(Constants.USER_ID, "0")));
            updateRegionRequest.setRegion(selectedId);
            ApiClient.getRestAPI().updateRegion(updateRegionRequest).enqueue(new Callback<GlobalResponse>() {
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull retrofit2.Response<GlobalResponse> response) {
//                    getcityResponse(Objects.requireNonNull(response.body()));
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        Constants.ToastShort(RegionActivity.this, "Region updated successfully");
                        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                        preferences.edit().putInt(Constants.REGION, selectedId).apply();
                        finish();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GlobalResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(RegionActivity.this);
        }
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
                    Toast.makeText(RegionActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(RegionActivity.this);
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
            Constants.InternetSettings(RegionActivity.this);
        }
    }

    private void getcityResponse(com.drcita.user.models.region.Response response) {
        dismissLoadingDialog();
        String description = response.getMessage();
        regionList = new ArrayList<String>();
        regionIdInList = new ArrayList<>();
        if (response.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<DataItem> accademicyearModels = response.getData();
            for (int i = 0; i < accademicyearModels.size(); i++) {
                responses.clear();
                for (int j = 0; j < response.getData().size(); j++) {
                    DataItem dataItem = response.getData().get(j);
                    if(dataItem.getId()==region) {
                        dataItem.setSelected(true);
                        regionAdapter.setSelectionIndex(j);
                    }
                    responses.add(dataItem);
                }
                regionAdapter.notifyDataSetChanged();
            }
            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/


}