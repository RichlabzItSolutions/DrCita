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
import android.view.View;
import android.widget.Toast;

import com.drcita.user.adapter.AppointmentsAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityAppointmentsBinding;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.appointment.AppointmentListResponse;
import com.drcita.user.models.appointment.AppointmentRequest;
import com.drcita.user.models.appointment.CAncelAppointmentRequest;
import com.drcita.user.models.appointment.DataItem;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AppointmentsActivity extends LanguageBaseActivity {

    private ActivityAppointmentsBinding activityAppointmentsBinding;
    private AppointmentsAdapter appointmentsAdapter;
    private List<AppointmentListResponse> appointmentListResponses = new ArrayList<AppointmentListResponse>();
    private List<DataItem> responses = new ArrayList<DataItem>();

    private ProgressDialog progress;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityAppointmentsBinding = DataBindingUtil.setContentView(this,R.layout.activity_appointments);
        setSupportActionBar(activityAppointmentsBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Appointments");
        activityAppointmentsBinding.backAppotiment.setOnClickListener(view -> finish());
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        activityAppointmentsBinding.appointmentsRV.setLayoutManager(mLayoutManager);
        appointmentsAdapter = new AppointmentsAdapter(this,responses);
        activityAppointmentsBinding.appointmentsRV.setAdapter(appointmentsAdapter);
        getAppointmentsList();

    }
    public void callApi(int id, int position) {
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            CAncelAppointmentRequest request = new CAncelAppointmentRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setAppointmentId(id);

            ApiClient.getRestAPI().cancelAppointment(request).enqueue(new Callback<GlobalResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        Toast.makeText(getApplicationContext(), response.body().getMessage(), Toast.LENGTH_LONG).show();
                        getAppointmentsList();
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
                public void onFailure(@NonNull Call<GlobalResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(getApplicationContext(), "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(getApplicationContext());
        }
    }


    private void getAppointmentsList() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            AppointmentRequest appointmentRequest = new AppointmentRequest();
            appointmentRequest.setUserId(Integer.parseInt(userId));
            ApiClient.getRestAPI().getUserAppointments(appointmentRequest).enqueue(new Callback<AppointmentListResponse>() {
                @Override
                public void onResponse(@NonNull Call<AppointmentListResponse> call, @NonNull retrofit2.Response<AppointmentListResponse> response) {
                    getAppointmentResponse(Objects.requireNonNull(response.body()));
                }
                @Override
                public void onFailure(@NonNull Call<AppointmentListResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(AppointmentsActivity.this);
        }
    }

    private void getAppointmentResponse(AppointmentListResponse appointmentListResponse) {
        dismissLoadingDialog();
        String description = appointmentListResponse.getMessage();
        if (appointmentListResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<com.drcita.user.models.appointment.DataItem> dataItems = appointmentListResponse.getData();
            if (appointmentListResponse.getData().isEmpty()){
                activityAppointmentsBinding.nodataimage.setVisibility(View.VISIBLE);
            }
            for (int i = 0; i < dataItems.size(); i++) {
                responses.clear();
                responses.addAll(appointmentListResponse.getData());
                appointmentsAdapter.notifyDataSetChanged();
            }
            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


   /* @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/
}