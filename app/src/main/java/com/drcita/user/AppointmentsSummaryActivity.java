package com.drcita.user;
import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.drcita.user.adapter.BookingAppointmentAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityAppointmentsBinding;
import com.drcita.user.models.GlobalRequest;

import com.drcita.user.models.appointmentbookingsummary.AppointmentsBookingResponse;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class AppointmentsSummaryActivity   extends LanguageBaseActivity {
    ActivityAppointmentsBinding appointmentsBinding;
    private String userId="";
    BookingAppointmentAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        appointmentsBinding = DataBindingUtil.setContentView(this, R.layout.activity_appointments);
        appointmentsBinding.llBack.setOnClickListener(view -> {
            finish();
        });
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        appointmentsBinding.appointmentsRV.setLayoutManager(gridLayoutManager);
         fetchAppointmentBooking();

    }
    private void fetchAppointmentBooking() {

        try{
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
             userId = sp.getString(Constants.USER_ID, userId);
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                GlobalRequest appointmentRequest = new GlobalRequest();
                appointmentRequest.setUserId(userId);
                ApiClient.getRestAPI().getBookingAppointment(appointmentRequest).enqueue(new Callback<AppointmentsBookingResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AppointmentsBookingResponse> call, @NonNull retrofit2.Response<AppointmentsBookingResponse> response) {
                        dismissLoadingDialog();
                         if(response.isSuccessful()) {
                             if (response.body() != null) {
                                 appointmentsBinding.appointmentsRV.setVisibility(VISIBLE);
                                 appointmentsBinding.nodataimage.setVisibility(GONE);
                                 getAppointmentResponse(Objects.requireNonNull(response.body()));
                             } else if (response.code() == 204) {
                                 appointmentsBinding.appointmentsRV.setVisibility(GONE);
                                 appointmentsBinding.nodataimage.setVisibility(VISIBLE);
                             }
                         }
                          else {
                             try {
                                 assert response.errorBody() != null;
                                 Constants.displayError(response.errorBody().string(), getBaseContext());
                                 dismissLoadingDialog();
                             } catch (IOException e) {
                                 throw new RuntimeException(e);
                             }

                         }
                    }
                    @Override
                    public void onFailure(@NonNull Call<AppointmentsBookingResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(AppointmentsSummaryActivity.this);
            }


        }catch (Exception ex)
        {
            ex.getMessage();
        }
            }

    private void getAppointmentResponse(AppointmentsBookingResponse body) {
        try {
            adapter =new BookingAppointmentAdapter(AppointmentsSummaryActivity.this,body.getData());
            appointmentsBinding.appointmentsRV.setAdapter(adapter);
            dismissLoadingDialog();

        }catch (Exception ex)

        {
            ex.getMessage();
        }

    }
}
