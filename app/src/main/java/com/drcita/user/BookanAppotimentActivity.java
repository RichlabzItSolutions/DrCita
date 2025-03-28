package com.drcita.user;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import com.drcita.user.adapter.BookAppointmentAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityBookanAppotimentBinding;
import com.drcita.user.models.doctorDetails.DoctorRequest;
import com.drcita.user.models.doctorDetails.DoctorResponse;
import com.drcita.user.models.doctorslots.BookAppRequest;
import com.drcita.user.models.doctorslots.DataItem;
import com.drcita.user.models.doctorslots.DoctorslotsListRequest;
import com.drcita.user.models.doctorslots.DoctorslotsListResponse;
import com.drcita.user.models.doctorslots.SlotResponse;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.GetProfileResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookanAppotimentActivity extends LanguageBaseActivity implements View.OnClickListener {


    private ActivityBookanAppotimentBinding activityBookanAppotimentBinding;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    private BookAppointmentAdapter bookAppointmentAdapter;
    private List<DataItem> responses = new ArrayList<DataItem>();
    private ProgressDialog progress;
    private int doctorID;
    private int slotNo;
    String userId;
            int providerId;
    private String slotId;
    private com.drcita.user.models.doctors.DataItem doctorData;
    private com.drcita.user.models.scans.DataItems doctorData1;
    boolean isFromList = false;
    private String mobile;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityBookanAppotimentBinding = DataBindingUtil.setContentView(this, R.layout.activity_bookan_appotiment);
        setSupportActionBar(activityBookanAppotimentBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                doctorID = 0;
            } else {
                doctorID = extras.getInt("doctorId");
                doctorData = Parcels.unwrap(extras.getParcelable("doctorData"));
                doctorData1 = Parcels.unwrap(extras.getParcelable("doctorData1"));
                type=extras.getString("type");
                Log.d("TAG", "onCreate: "+doctorData);
                Log.d("TAG", "onCreate: "+doctorData1);
                isFromList = extras.getBoolean(Constants.list);
                if(doctorData!=null) {
                    providerId = doctorData.getProviderId();
                } else {
                    providerId = doctorData1.getProviderId();
                }
            }

        } else {
        }
       // Log.d("TAG", "doctorId: "+doctorData.getDoctorId());
     //   Log.d("TAG", "doctorId1: "+doctorData1.getScanId());


        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        //providerId = sp.getString(Constants.PROVIDER_ID, providerId);
       // providerId = String.valueOf(doctorData.getProviderId());
        activityBookanAppotimentBinding.backBooking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        activityBookanAppotimentBinding.calendardate.setOnClickListener(this);
        activityBookanAppotimentBinding.appotimentdate.setOnClickListener(this);

        if (isFromList){
            activityBookanAppotimentBinding.doctorname.setText(doctorData1.getScanName());
            activityBookanAppotimentBinding.specializationName.setText(doctorData1.getHospitalName());
            doctorID = doctorData1.getScanId();
            Log.d("TAG", "doctorID: "+doctorID);

            if (!doctorData1.getPicture().isEmpty()) {
                Picasso.with(this).load(doctorData1.getPicture()).into(activityBookanAppotimentBinding.doctorprofileimg);
            }
            activityBookanAppotimentBinding.bookappointmentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activityBookanAppotimentBinding.appotimentdate.getText().toString().equals(""))
                    {
                        Constants.ToastShort(BookanAppotimentActivity.this, "Select Date");
                        return;
                    }
                   /* if (slotNo == 0) {
                        Constants.ToastLong(BookanAppotimentActivity.this,"Slot number should not  empty");
                        return;
                    }*/
                    callBookAppointment(2);
                }
            });
        }else {
            activityBookanAppotimentBinding.doctorname.setText(doctorData.firstName + " " + doctorData.getLastName());
            activityBookanAppotimentBinding.specializationName.setText(doctorData.getSpecialisationName());
            doctorID = doctorData.getDoctorId();
            Log.d("TAG", "doctorId: "+doctorData.getDoctorId());
            if (!doctorData.getPicture().isEmpty()) {
                Picasso.with(this).load(doctorData.getPicture())
                        .into(activityBookanAppotimentBinding.doctorprofileimg);
            }
            activityBookanAppotimentBinding.bookappointmentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (activityBookanAppotimentBinding.appotimentdate.getText().toString().equals(""))
                    {
                        Constants.ToastShort(BookanAppotimentActivity.this, "Select Date");
                        return;
                    }
                    if (slotNo == 0) {
                        Constants.ToastLong(BookanAppotimentActivity.this,"Slot number should not  empty");
                        return;
                    }
                    callBookAppointment(1);
                }
            });
        }
        activityBookanAppotimentBinding.changeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup();
            }
        });
    }

    @Override
    protected void onResume() {
        getUserProfile();
        getDoctorDetails();
        super.onResume();
    }

    private void getDoctorDetails() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            DoctorRequest request = new DoctorRequest();
            request.setDoctorId(doctorID);
            ApiClient.getRestAPI().getDoctorDetails(request).enqueue(new Callback<DoctorResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<DoctorResponse> call, @NonNull Response<DoctorResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        getDoctordetailsResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<DoctorResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(BookanAppotimentActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(BookanAppotimentActivity.this);
        }
    }

    private void getDoctordetailsResponse(DoctorResponse doctorResponse) {
        if (doctorResponse.getStatus().equals("success")){
            String availabledays = doctorResponse.getData().getAvailableDays();
            activityBookanAppotimentBinding.availbilityDays.setText(availabledays);

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
                public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull Response<GetProfileResponse> response) {
                    if (response.isSuccessful()) {
                         dismissLoadingDialog();
                        getProfileResponse(Objects.requireNonNull(response.body()));
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
                    Toast.makeText(BookanAppotimentActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(BookanAppotimentActivity.this);
        }
    }

    private void getProfileResponse(GetProfileResponse getProfileResponse) {
        activityBookanAppotimentBinding.nameTV.setText(getProfileResponse.getData().getName());
        if (getProfileResponse.getData().getPicture()!=null && !getProfileResponse.getData().getPicture().isEmpty()) {
            Picasso.with(this).load(getProfileResponse.getData().getPicture())
                    .into(activityBookanAppotimentBinding.patientimageIV);
            activityBookanAppotimentBinding.patientimageIV.setImageTintList(null);
        } else {
            Picasso.with(this).load(R.drawable.avtar).into(activityBookanAppotimentBinding.patientimageIV);
//            activityBookanAppotimentBinding.patientimageIV.setColorFilter(Color.argb(255,255,255,255));
        }

        activityBookanAppotimentBinding.changeTV.setText(getString(R.string.change));
        activityBookanAppotimentBinding.changeTV.setTextColor(getApplicationContext().getResources().getColor(R.color.skyblue));
        activityBookanAppotimentBinding.changeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showpopup();
            }
        });

    }


    private void showpopup() {
        Dialog dialog = new Dialog(this,R.style.caafisomdailogtheme);
        dialog.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.change_appointment_popup, null);
        dialog.setContentView(view);
        TextView patientnameET,patientnumberET;
        Button submitBtn;
        patientnameET = view.findViewById(R.id.patientnameET);
        patientnumberET = view.findViewById(R.id.patientnumberET);
        submitBtn = view.findViewById(R.id.submitBtn);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if (patientnameET.getText().toString().equals("")){
                    Constants.ToastShort(BookanAppotimentActivity.this, "Enter Patient Name");
                    return;
                }
                if (patientnameET.getText().toString().length() < 3){
                    Constants.ToastShort(BookanAppotimentActivity.this, "Patient Name length should be 3");
                    return;
                }
                if (patientnumberET.getText().toString().equals("")) {
                    Constants.ToastShort(BookanAppotimentActivity.this, "Enter Patient Phone Number");
                    return;
                }
                if (!(patientnumberET.getText().toString().matches(Constants.MobilePattern))) {
                    Constants.ToastShort(BookanAppotimentActivity.this, Constants.enter_valid_mobile_no);
                    return;
                }
                popupBook(patientnameET.getText().toString(),patientnumberET.getText().toString());

            }
        });
        dialog.show();

    }

    @SuppressLint("ResourceAsColor")
    private void popupBook(String toString, String toString1) {
         mobile = toString1;

        activityBookanAppotimentBinding.nameTV.setText(toString);
        Picasso.with(this).load(R.drawable.avtar).into(activityBookanAppotimentBinding.patientimageIV);
        activityBookanAppotimentBinding.changeTV.setText("Remove");
        activityBookanAppotimentBinding.changeTV.setTextColor(getApplicationContext().getResources().getColor(R.color.red));
        activityBookanAppotimentBinding.changeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getUserProfile();
            }
        });
    }


    @SuppressLint("SetTextI18n")
    private void updateSlotResponse(SlotResponse body) {
        activityBookanAppotimentBinding.slotView.setVisibility(View.GONE);
        activityBookanAppotimentBinding.slotView.setText("" + body.getData().getSlotNumber());
        slotNo = body.getData().getSlotNumber();
    }

    private void callBookAppointment(int i) {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            BookAppRequest slotRequest = new BookAppRequest();
            slotRequest.setAppointmentDate(activityBookanAppotimentBinding.appotimentdate.getText().toString());
            slotRequest.setDoctorId(doctorID);
            slotRequest.setProviderId(providerId);
            slotRequest.setUserId(Integer.parseInt(userId));
            slotRequest.setSlotNumber(slotNo);
            slotRequest.setAppointmentMode(1);
            slotRequest.setAppointmentType(i);
            slotRequest.setPatientName(activityBookanAppotimentBinding.nameTV.getText().toString());
            slotRequest.setPatientMobile(mobile);
            ApiClient.getRestAPI().bookAppointment(slotRequest).enqueue(new Callback<SlotResponse>() {
                @Override
                public void onResponse(@NonNull Call<SlotResponse> call, @NonNull retrofit2.Response<SlotResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        Intent intent = new Intent(getApplicationContext(), CheckoutActivity.class);
                        intent.putExtra("position",i);
                        intent.putExtra("listFrom",true);
                        intent.putExtra("doctorData", Parcels.wrap(doctorData));
                        intent.putExtra("doctorData1", Parcels.wrap(doctorData1));
                        intent.putExtra("slotNo", slotNo);
                        intent.putExtra("id", response.body().getData().getId());
                        intent.putExtra("type",type);
                        intent.putExtra("date", activityBookanAppotimentBinding.appotimentdate.getText().toString());
                        startActivity(intent);
//                        Toast.makeText(BookanAppotimentActivity.this, SlotResponse.getMessage(), Toast.LENGTH_LONG).show();
                        finish();
                    }
//                    updateSlotResponse(response.body());
                }

                @Override
                public void onFailure(@NonNull Call<SlotResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(BookanAppotimentActivity.this);
        }
    }

    private void getDoctorSlots(int i, int doctorIDD) {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            DoctorslotsListRequest doctorslotsListRequest = new DoctorslotsListRequest();
            doctorslotsListRequest.setAppointmentDate(activityBookanAppotimentBinding.appotimentdate.getText().toString());
            doctorslotsListRequest.setDoctorId(doctorIDD);
            doctorslotsListRequest.setAppointmentType(i);
            doctorslotsListRequest.setUserId(userId);
            ApiClient.getRestAPI().getDoctorSlots(doctorslotsListRequest).enqueue(new Callback<DoctorslotsListResponse>() {
                @Override
                public void onResponse(@NonNull Call<DoctorslotsListResponse> call, @NonNull retrofit2.Response<DoctorslotsListResponse> response) {
                    if (response.isSuccessful() && response.code()==200) {
                        dismissLoadingDialog();
                        activityBookanAppotimentBinding.bookappointmentBtn.setVisibility(View.VISIBLE);
                        getDoctorSlotsResponse(Objects.requireNonNull(response.body()));
                    } else {
                        activityBookanAppotimentBinding.appointmentLayout.setVisibility(View.GONE);
                        activityBookanAppotimentBinding.bookappointmentBtn.setVisibility(View.GONE);
                        activityBookanAppotimentBinding.timingslayout.setVisibility(View.GONE);
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }
                }
                @Override
                public void onFailure(@NonNull Call<DoctorslotsListResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(BookanAppotimentActivity.this);
        }
    }

    private void getDoctorSlotsResponse(DoctorslotsListResponse doctorslotsListResponse) {
        dismissLoadingDialog();
        String description = doctorslotsListResponse.getMessage();
        activityBookanAppotimentBinding.slotView.setText("" + doctorslotsListResponse.getSlotNumber());
        slotNo = Integer.parseInt(doctorslotsListResponse.getSlotNumber());
        activityBookanAppotimentBinding.appointmentLayout.setVisibility(View.GONE);

        if (doctorslotsListResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            List<com.drcita.user.models.doctorslots.DataItem> dataItems = doctorslotsListResponse.getData();
            if (doctorslotsListResponse.getData()==null || doctorslotsListResponse.getData().isEmpty()){
                activityBookanAppotimentBinding.timingslayout.setVisibility(View.GONE);
            }else {
                activityBookanAppotimentBinding.timingslayout.setVisibility(View.VISIBLE);
                for (int i = 0; i < dataItems.size(); i++) {
                    if(i==0){
                        activityBookanAppotimentBinding.mng.setText(doctorslotsListResponse.getData().get(i).getSlot());
                        activityBookanAppotimentBinding.mngtime.setText(""+doctorslotsListResponse.getData().get(i).getFromTime()+" to " +doctorslotsListResponse.getData().get(i).getToTime());
                    } else {
                        activityBookanAppotimentBinding.evng.setText(doctorslotsListResponse.getData().get(i).getSlot());
                        activityBookanAppotimentBinding.evngtime.setText(""+doctorslotsListResponse.getData().get(i).getFromTime()+" to " +doctorslotsListResponse.getData().get(i).getToTime());
                    }
//                bookAppointmentAdapter.notifyDataSetChanged();
                }
            }
        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onClick(View view) {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DialogTheme, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                activityBookanAppotimentBinding.appotimentdate.setText(dayOfMonth + "-" + "" + (monthOfYear + 1) + "-" + "" + year);
                if (isFromList){
                    getDoctorSlots(2,doctorID);
                }else {
                    getDoctorSlots(1,doctorID);
                }
            }
        });
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

}