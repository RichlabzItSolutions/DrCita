package com.drcita.user;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.bumptech.glide.Glide;
import com.drcita.user.Activity.CoupnsActivity;
import com.drcita.user.adapter.coupon.CouponAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.DocterBookingBinding;
import com.drcita.user.models.appointment.AppointmentResponse;
import com.drcita.user.models.appointment.BookAppointmentRequest;
import com.drcita.user.models.appointment.DocterDetailResoponse;
import com.drcita.user.models.appointment.DocterDetailsRequest;
import com.drcita.user.models.appointment.DoctorData;
import com.drcita.user.models.appointment.ResechudleAppointmentRequest;
import com.drcita.user.models.appointment.TimeSlot;
import com.drcita.user.models.appointment.singleAppointmentRequest;
import com.drcita.user.models.coupns.ApplyCouponResponse;
import com.drcita.user.models.coupns.CouponApplyRequest;
import com.drcita.user.models.coupns.CouponModel;
import com.drcita.user.models.coupns.CouponRequest;
import com.drcita.user.models.coupns.CouponResponse;
import com.drcita.user.models.profile.AddNewMember;
import com.drcita.user.models.profile.CheckUserRequest;
import com.drcita.user.models.profile.CheckUserResponse;
import com.drcita.user.models.profile.RelationResponse;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.models.userprofile.UserData;
import com.drcita.user.models.userprofile.UserRequestData;
import com.drcita.user.models.userprofile.UsersResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.flexbox.FlexboxLayout;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;

public class DoctorAppointmentActivity extends LanguageBaseActivity {

    private String providerId, docterId, amount, paymenttype,
            subuserId, stotTime = "",
            subuserName = "", slotdate, isresechedule;
    private DocterBookingBinding docterBookingBinding;
    private String userId;
    int appointmentMode, totalamount;
    List<RelationResponse.Relation> relationshipdata;
    private int relationId, genderId;
    ArrayAdapter<String> relationAdapter;
    private boolean isSummary = false;
    String selectedDate = null;
    private int profilestatus;
    CouponAdapter  couponAdapter;


    private CouponAdapter adapter;
    private List<CouponModel> couponList = new ArrayList<>();
    private int currentPosition = 0;
    private Handler autoScrollHandler = new Handler();
    private int systemcharge;
    private int couponAmount=0;
    private int couponId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        docterBookingBinding = DataBindingUtil.setContentView(this, R.layout.docter_booking);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                providerId = getIntent().getStringExtra("providerId");
                docterId = getIntent().getStringExtra("docterId");
                amount = getIntent().getStringExtra("amount");
                slotdate = getIntent().getStringExtra("slotdate");
                isresechedule = getIntent().getStringExtra("isresechedule");
                paymenttype = getIntent().getStringExtra("paymentype");
                docterBookingBinding.tvMode.setText(paymenttype);
                docterBookingBinding.tvamount.setText("₹" + amount);
                if (paymenttype.equals("Online")) {
                    appointmentMode = 1;
                } else {
                    appointmentMode = 2;
                }
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            selectedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        }
        getRelationships();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            docterBookingBinding.llAddmemeber.setOnClickListener(v -> showAddMemberDialog());
        }
        docterBookingBinding.llBack.setOnClickListener(view1 -> {
            if (isSummary) {
                docterBookingBinding.llContinuebooking.setVisibility(VISIBLE);
                docterBookingBinding.btnContinue.setVisibility(VISIBLE);
                docterBookingBinding.llSlot.setVisibility(GONE);
                isSummary = false;
            } else {
                finish();
            }

        });
        docterBookingBinding.dateTextView.setOnClickListener(v -> showDatePicker());
        getUserProfile();
        if (isresechedule.equals("0")) { // for  new appointment booking only
            docterBookingBinding.llAddmemeber.setVisibility(VISIBLE);
            docterBookingBinding.tvTitle.setText("Doctor Appointment");
            Calendar today = Calendar.getInstance();
            int year = today.get(Calendar.YEAR);
            int month = today.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero‐based
            int day = today.get(Calendar.DAY_OF_MONTH);
            // 3. Format & set it
            selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", day, month, year);
            docterBookingBinding.dateTextView.setText(selectedDate);
            docterBookingBinding.btnPayNow.setText("Pay Now");
            docterBookingBinding.tvamount.setVisibility(VISIBLE);
            docterBookingBinding.bookingForSpinner.setEnabled(true);
            getDoctorDetails();
        } else if (isresechedule.equals("1")) { // for resechudling
            docterBookingBinding.llAddmemeber.setVisibility(GONE);
            docterBookingBinding.tvTitle.setText("Reshedule");
            docterBookingBinding.dateTextView.setText(slotdate);
            docterBookingBinding.bookingForSpinner.setEnabled(false);
            selectedDate=slotdate;
            getRescheduleApi();
            docterBookingBinding.tvamount.setVisibility(GONE);
            docterBookingBinding.btnPayNow.setText("Reshedule");

            // hide the coupon code section
            docterBookingBinding.sectionCoupon.setVisibility(GONE);
            docterBookingBinding.rvCoupons.setVisibility(GONE);


        }
        docterBookingBinding.btnContinue.setOnClickListener(view -> {
            if (stotTime.isEmpty()) {
                Toast.makeText(DoctorAppointmentActivity.this, "Please select slot", Toast.LENGTH_SHORT).show();
            } else {
                isSummary = true;
                docterBookingBinding.llContinuebooking.setVisibility(GONE);
                docterBookingBinding.btnContinue.setVisibility(GONE);
                docterBookingBinding.llSlot.setVisibility(VISIBLE);
                docterBookingBinding.tvAppoinmentddate.setText(docterBookingBinding.dateTextView.getText().toString());
                docterBookingBinding.tvConsultationfees.setText("₹" + amount);
                docterBookingBinding.tvSlotime.setText(stotTime);
            }
        });
        docterBookingBinding.btnPayNow.setOnClickListener(view -> {

           if(isresechedule.equals("0"))
              CallAPI();
           else if(isresechedule.equals("1")) {
               RescheduledApI();

           }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
         docterBookingBinding.rvCoupons.setLayoutManager(layoutManager);
         adapter = new CouponAdapter(this, couponList);
        docterBookingBinding.rvCoupons.setAdapter(adapter);
        fetchCouponsFromApi();
        docterBookingBinding.tvApplycode.setOnClickListener(view -> showApplyCouponDialog());

        docterBookingBinding.tvSpecilaizationAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i =new Intent(DoctorAppointmentActivity.this, CoupnsActivity.class);
                startActivity(i);
            }
        });
        docterBookingBinding.ivClearCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clearAppliedCoupon();
            }
        });


    }

    private void clearAppliedCoupon() {
        docterBookingBinding.tvCoupon.setText("");
        docterBookingBinding.tvApplycode.setText("Apply Coupon");
        totalamount = Integer.parseInt(amount) + systemcharge;
        docterBookingBinding.tvTotalamount.setText("₹" + String.valueOf(totalamount));
        docterBookingBinding.ivClearCoupon.setVisibility(View.GONE);
        Toast.makeText(this, "Coupon removed", Toast.LENGTH_SHORT).show();
    }
    private void setupAutoSlide() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                if (adapter != null && adapter.getItemCount() > 0) {
                    currentPosition = (currentPosition + 1) % adapter.getItemCount();
                   docterBookingBinding.rvCoupons.smoothScrollToPosition(currentPosition);
                    autoScrollHandler.postDelayed(this, 4000); // 4 sec interval
                }
            }
        };
        autoScrollHandler.postDelayed(runnable, 4000);
    }
    private void fetchCouponsFromApi() {
        try {
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                CouponRequest request=new CouponRequest();
                request.setAppointmentMode(2);
                ApiClient.getRestAPI().getCoupons(request).enqueue(new Callback<CouponResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CouponResponse> call, @NonNull retrofit2.Response<CouponResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                                 couponList = response.body().getData();
                                couponAdapter = new CouponAdapter(DoctorAppointmentActivity.this, couponList);
                                docterBookingBinding.rvCoupons.setAdapter(couponAdapter);
                                 setupAutoSlide(); // Start auto-slide
                       }
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<CouponResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
        }

    }



    private void RescheduledApI() {
        try {
            showLoadingDialog();
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            userId = sp.getString(Constants.USER_ID, "");

            if (Constants.haveInternet(getApplicationContext())) {
                ResechudleAppointmentRequest bookAppointmentRequest = new ResechudleAppointmentRequest
                                (Integer.parseInt(docterId),
                                docterBookingBinding.dateTextView.getText().toString(),
                                stotTime, appointmentMode,userId);
                ApiClient.getRestAPI().bookAppointment(bookAppointmentRequest).enqueue(new Callback<AppointmentResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AppointmentResponse> call, @NonNull retrofit2.Response<AppointmentResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            Toast.makeText(DoctorAppointmentActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PaymentsucessActivity.class);
                            intent.putExtra("doctorData", Parcels.wrap(response.body().getData()));
                            intent.putExtra("doctorData1", Parcels.wrap(response.body().getData()));
                            intent.putExtra("id", Parcels.wrap(response.body().getData().getAppointmentId()));
                            intent.putExtra("slotNo", Parcels.wrap(response.body().getData().getSlotTime()));
                            intent.putExtra("position", "1");
                            intent.putExtra("payment", totalamount);
                            startActivity(intent);
                            finish();
                            dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AppointmentResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
            dismissLoadingDialog();
        }

    }

    private void CallAPI() {
        try {
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                CheckUserRequest checkUserRequest = new CheckUserRequest();
                checkUserRequest.setSubUserId(Integer.parseInt(subuserId));
                ApiClient.getRestAPI().checkUser(checkUserRequest).enqueue(new Callback<CheckUserResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CheckUserResponse> call, @NonNull retrofit2.Response<CheckUserResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            profilestatus = response.body().getData().getStatus();
                            if (profilestatus == 1) {
                                bookAppointmentApI(); // Profile complete
                            } else {
                                showProfileNotUpdatedDialog(response.body()); // Show alert if profile is not complete
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CheckUserResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
        }

    }

    private void getRelationships() {
        try {
            showLoadingDialog();

            if (Constants.haveInternet(getApplicationContext())) {

                ApiClient.getRestAPI().getRelationships().enqueue(new Callback<RelationResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RelationResponse> call, @NonNull retrofit2.Response<RelationResponse> response) {
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
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
        }

    }

    private void displayRelatiomshipsResponse(List<RelationResponse.Relation> data) {
        relationshipdata = data;

    }

    private void bookAppointmentApI() {
        try {
            showLoadingDialog();
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            userId = sp.getString(Constants.USER_ID, "");

            if (Constants.haveInternet(getApplicationContext())) {
                BookAppointmentRequest bookAppointmentRequest = new BookAppointmentRequest(Integer.parseInt(docterId), Integer.parseInt(providerId), docterBookingBinding.dateTextView.getText().toString(), stotTime, Integer.parseInt(userId), Integer.parseInt(subuserId), appointmentMode, "1",// 1-> docter ,2->Scan
                        Integer.parseInt(amount), couponId, couponAmount, Integer.parseInt(docterBookingBinding.tvSystemcharges.getText().toString()), totalamount, "dggdgdg2222"


                );


                ApiClient.getRestAPI().bookAppointment(bookAppointmentRequest).enqueue(new Callback<AppointmentResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AppointmentResponse> call, @NonNull retrofit2.Response<AppointmentResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            Toast.makeText(DoctorAppointmentActivity.this, response.message(), Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(), PaymentsucessActivity.class);
                            intent.putExtra("doctorData", Parcels.wrap(response.body().getData()));
                            intent.putExtra("doctorData1", Parcels.wrap(response.body().getData()));
                            intent.putExtra("id", Parcels.wrap(response.body().getData().getAppointmentId()));
                            intent.putExtra("slotNo", Parcels.wrap(response.body().getData().getSlotTime()));
                            intent.putExtra("position", "1");
                            intent.putExtra("payment", totalamount);
                            startActivity(intent);
                            finish();
                            dismissLoadingDialog();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<AppointmentResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
            dismissLoadingDialog();
        }


    }


    private void getUserProfile() {
        try {
            showLoadingDialog();
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            userId = sp.getString(Constants.USER_ID, "");
            if (Constants.haveInternet(getApplicationContext())) {
                UserRequestData request = new UserRequestData();
                request.setUserId(userId);
                String currentDate = null;

                ApiClient.getRestAPI().getUserProfile(request).enqueue(new Callback<UsersResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<UsersResponse> call, @NonNull retrofit2.Response<UsersResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            displayUserResponse(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<UsersResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
        }


    }

    private void displayUserResponse(List<UserData> data) {

        try {

            // Extract names from UserData list
            List<String> names = new ArrayList<>();
            for (UserData user : data) {
                if (user.getSelf() == 1) {
                    names.add("Self");  // Mark the self user
                } else {
                    names.add(user.getName());
                }
                relationAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, names);
                docterBookingBinding.bookingForSpinner.setAdapter(relationAdapter);

                docterBookingBinding.bookingForSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                        subuserId = String.valueOf(data.get(pos).getSubUserId());
                        subuserName = data.get(pos).getName();
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }

    private void showDatePicker() {
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH); // Don't add +1 here
        int day = today.get(Calendar.DAY_OF_MONTH);

        // Optional: Format & show current date
        String formatted = String.format(Locale.getDefault(), "%02d/%02d/%04d", day, month + 1, year);
        docterBookingBinding.dateTextView.setText(formatted);

        // Create and show date picker
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, (view, year1, month1, dayOfMonth) -> {
            // Month is 0-based in callback, so add +1 for display
            selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
            docterBookingBinding.dateTextView.setText(selectedDate);
            // Call after setting date
            if(isresechedule.equals("0")) {
                getDoctorDetails();
            }
            else if(isresechedule.equals("1"))
            {
                getRescheduleApi();
            }

        }, year, month, day);
// Set minimum date to today
        datePickerDialog.getDatePicker().setMinDate(today.getTimeInMillis());

// Set maximum date to 15 days from today
        today.add(Calendar.DAY_OF_MONTH, 15);
        datePickerDialog.getDatePicker().setMaxDate(today.getTimeInMillis());
        datePickerDialog.show();
    }

    private void getRescheduleApi() {
        try {
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                singleAppointmentRequest request = new singleAppointmentRequest();
                request.setId(Integer.parseInt(docterId));
                request.setSlotDate(selectedDate);

                ApiClient.getRestAPI().getBookedAppointments(request).enqueue(new Callback<DocterDetailResoponse>() {
                    @Override
                    public void onResponse(@NonNull Call<DocterDetailResoponse> call, @NonNull retrofit2.Response<DocterDetailResoponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            displayDoctorSlots(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DocterDetailResoponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
        }
    }

    private void getDoctorDetails() {
        try {
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                DocterDetailsRequest request = new DocterDetailsRequest();
                request.setProviderId(Integer.parseInt(providerId));
                request.setDoctorId(Integer.parseInt(docterId));
                request.setSlotDate(selectedDate);

                ApiClient.getRestAPI().getdocterDetails(request).enqueue(new Callback<DocterDetailResoponse>() {
                    @Override
                    public void onResponse(@NonNull Call<DocterDetailResoponse> call, @NonNull retrofit2.Response<DocterDetailResoponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                             displayDoctorSlots(response.body().getData());
                        }
                        else {
                            dismissLoadingDialog();
                            Constants.displayError(String.valueOf(response.errorBody()),getApplicationContext());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DocterDetailResoponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
        }
    }

    private void displayDoctorSlots(DoctorData data) {

        try {
            docterBookingBinding.tvDoctorName.setText(data.getDoctorName());
            docterBookingBinding.tvHospitalName.setText(data.getHospitalName());
              docterBookingBinding.tvdesegnation.setText(TextUtils.join(", ", data.getQualifications()));
            docterBookingBinding.tvSpecialization.setText(TextUtils.join(", ", data.getSpecializations()));
            docterBookingBinding.tvlanguage.setText(TextUtils.join(", ", data.getLanguages()));
            docterBookingBinding.tvTotalslot.setText("Total Slots \n" + data.getSlots().getTotalSlots());
            docterBookingBinding.tvAvailableslots.setText("Available Slots \n" + data.getSlots().getAvailableSlots());
            docterBookingBinding.llCoupon.setVisibility(VISIBLE);
            docterBookingBinding.tvSystemcharges.setText(String.valueOf(data.getSystemCharges()));
            systemcharge=data.getSystemCharges();
            totalamount = Integer.parseInt(amount) + systemcharge - data.getCoupon();
            docterBookingBinding.tvTotalamount.setText("₹" + String.valueOf(totalamount));
            docterBookingBinding.tvCoupon.setText(String.valueOf(data.getCoupon()));
            Glide.with(this).load(data.getPicture()).placeholder(R.drawable.docter_img).into(docterBookingBinding.imgDoctor);
            docterBookingBinding.morningLayout.removeAllViews();
            docterBookingBinding.afternoonLayout.removeAllViews();
            docterBookingBinding.eveningLayout.removeAllViews();

            List<Slot> morningSlots = new ArrayList<>();
            List<Slot> afternoonSlots = new ArrayList<>();
            List<Slot> eveningSlots = new ArrayList<>();

            if (data.getAvailability() != null) {
                if (data.getAvailability().getMorning() != null) {
                    for (TimeSlot slot : data.getAvailability().getMorning().getSlots()) {
                        morningSlots.add(new Slot(slot.getSlot(), slot.getIsAvailable() == 1));
                    }
                }
                if (data.getAvailability().getAfternoon() != null) {
                    for (TimeSlot slot : data.getAvailability().getAfternoon().getSlots()) {
                        afternoonSlots.add(new Slot(slot.getSlot(), slot.getIsAvailable() == 1));
                    }
                }
                if (data.getAvailability().getEvening() != null) {
                    for (TimeSlot slot : data.getAvailability().getEvening().getSlots()) {
                        eveningSlots.add(new Slot(slot.getSlot(), slot.getIsAvailable() == 1));
                    }
                }
            }

            addSlots(docterBookingBinding.morningLayout, morningSlots);
            addSlots(docterBookingBinding.afternoonLayout, afternoonSlots);
            addSlots(docterBookingBinding.eveningLayout, eveningSlots);
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private Button selectedSlotButton = null; // Track currently selected slot button

    private void addSlots(FlexboxLayout layout, List<Slot> slots) {
        layout.removeAllViews();

        if (slots == null || slots.isEmpty()) {
            TextView noSlotsText = new TextView(this);
            noSlotsText.setText("No slots available");
            noSlotsText.setTextSize(14);
            noSlotsText.setTextColor(getResources().getColor(R.color.gray));
            noSlotsText.setGravity(Gravity.CENTER);

            FlexboxLayout.LayoutParams textParams = new FlexboxLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            textParams.setMargins(16, 16, 16, 16);
            noSlotsText.setLayoutParams(textParams);

            layout.addView(noSlotsText);
            return;
        }


        for (Slot slot : slots) {
            Button slotButton = new Button(this);
            slotButton.setText(slot.time);
            slotButton.setAllCaps(false);
            slotButton.setTextSize(13);
            slotButton.setPadding(24, 8, 24, 8);

            if (slot.isAvailable) {
                slotButton.setBackgroundResource(R.drawable.slot_available_bg);
                slotButton.setEnabled(true);
                slotButton.setTextColor(getResources().getColor(R.color.black));

                slotButton.setOnClickListener(v -> {
                    // Reset previously selected slot
                    if (selectedSlotButton != null) {
                        selectedSlotButton.setBackgroundResource(R.drawable.slot_available_bg);
                        selectedSlotButton.setTextColor(getResources().getColor(R.color.black));
                    }

                    // Set new selected slot
                    slotButton.setBackgroundResource(R.drawable.slot_selected_green_bg);
                    slotButton.setTextColor(Color.WHITE); // Optional: change text color on selection
                    selectedSlotButton = slotButton;
                    stotTime = slot.time;

                });

            } else {
                slotButton.setBackgroundResource(R.drawable.slot_booked_bg);
                slotButton.setEnabled(false);
                slotButton.setTextColor(getResources().getColor(R.color.gray));
            }
            // Increase button width (e.g., 120dp)
            int widthInDp = 100;
            float scale = getResources().getDisplayMetrics().density;
            int heightInDp = 36; // or any height you want
            int heightInPx = (int) (heightInDp * scale + 0.5f);
            int widthInPx = (int) (widthInDp * scale + 0.5f);
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(widthInPx, heightInPx);
            params.setMargins(8, 8, 8, 8);
            slotButton.setLayoutParams(params);

            layout.addView(slotButton);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void showAddMemberDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_add_member, null);
        builder.setView(view);
        AlertDialog dialog = builder.create();

        // Views
        ImageView ivClose = view.findViewById(R.id.ivClose);
        EditText etName = view.findViewById(R.id.etName);
        EditText etAge = view.findViewById(R.id.etAge);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Spinner spinnerGender = view.findViewById(R.id.spinnerGender);
        EditText etDOB = view.findViewById(R.id.etDOB);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Spinner spinnerRelation = view.findViewById(R.id.spinnerRelation);
        EditText etAddress = view.findViewById(R.id.etAddress);
        Button btnSubmit = view.findViewById(R.id.btnSubmit);
        List<String> relation = new ArrayList<>();
        for (RelationResponse.Relation user : relationshipdata) {
            relation.add(user.getRelation());
        }
        // Setup gender spinner items
        List<String> genderList = Arrays.asList("Female", "Male", "Other");
        // Gender variable
        final int[] genderValue = {1}; // Default to Female

        // Gender Spinner
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, genderList);
        spinnerGender.setAdapter(genderAdapter);

        // Relation Spinner
        ArrayAdapter<String> relationAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, relation);
        spinnerRelation.setAdapter(relationAdapter);
        spinnerRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


                relationId = relationshipdata.get(pos).getId();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
        // Date Picker

        etDOB.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePicker = new DatePickerDialog(this, (view1, year, month, day) -> {
                // Format with leading zeroes
                String formattedDate = String.format("%02d-%02d-%04d", day, month + 1, year);
                etDOB.setText(formattedDate);
            }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

            datePicker.show();
        });


        // Submit logic
        btnSubmit.setOnClickListener(v -> {

            if (etName.getText().length() == 0) {
                Toast.makeText(v.getContext(), "Enter your name", Toast.LENGTH_SHORT).show();
                return; // stop execution
            }

            if (etAge.getText().length() == 0 && etDOB.getText().length() == 0) {
                Toast.makeText(v.getContext(), "Please enter your age or DOB", Toast.LENGTH_SHORT).show();
                return; // stop execution
            }

            // ✅ All validation passed — proceed
            addMember(etName, etAge, etDOB);

            // Only dismiss after successful input
            dialog.dismiss();
        });


        // Close dialog
        ivClose.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private void addMember(EditText etName, EditText etAge, EditText etDOB) {
        try {
            showLoadingDialog();
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            userId = sp.getString(Constants.USER_ID, "");
            if (Constants.haveInternet(getApplicationContext())) {

                int age = 0;
                if (etAge.getText().length() > 0) {
                    try {
                        age = Integer.parseInt(etAge.getText().toString().trim());
                    } catch (NumberFormatException e) {
                        age = 0; // or show error message
                    }
                }

                String fullName = etName.getText().toString().trim();
                String dob = etDOB.getText().toString().trim(); // can be empty
                String maritalStatus = "0";   // from spinner
                String mobile = "";
                String email = "";
                String stateId = ""; // from spinner or dropdown

                String cityId = sp.getString(Constants.CITY_ID, "");   // same as above
                String address = "";

                String pastSurgeries = ""; // optional
                String bloodGroupId = ""; // dropdown or input

                // 4. Prepare diseases list (can be empty)
                List<AddNewMember.Disease> diseaseList = new ArrayList<>();
                AddNewMember request = new AddNewMember(Integer.parseInt(userId), fullName, genderId, age, dob, maritalStatus, mobile, email, stateId, cityId, address, pastSurgeries, bloodGroupId, relationId, 2, diseaseList);

                ApiClient.getRestAPI().addMemberNew(request).enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RestResponse> call, @NonNull retrofit2.Response<RestResponse> response) {
                        dismissLoadingDialog();
                        if (response.isSuccessful()) {
                            Toast.makeText(DoctorAppointmentActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            getUserProfile();
                            relationAdapter.notifyDataSetChanged();
                        } else {
                            try {
                                Constants.displayError(response.errorBody().string(), getBaseContext());
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(DoctorAppointmentActivity.this);
            }
        } catch (Exception ignored) {
        }

    }

    private void showProfileNotUpdatedDialog(CheckUserResponse body) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.CustomAlertDialog);

        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_profile_incomplete, null);
        TextView dialogMessage = view.findViewById(R.id.dialogMessage);
        builder.setView(view);
        builder.setCancelable(false);


        dialogMessage.setText(subuserName + " profile is not updated. Please update your profile before you make an appointment.");
        AlertDialog dialog = builder.create();

        // Initialize views
        Button btnUpdateNow = view.findViewById(R.id.btnUpdateNow);
        Button btnCancel = view.findViewById(R.id.btnCancel);

        btnUpdateNow.setOnClickListener(v -> {
            Intent i = new Intent(this, ProfileUpdateActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

            if (body.getData().getSelf() == 0) {
                i.putExtra("from", "profile");
                i.putExtra("subuserId", String.valueOf(body.getData().getSubUserId()));
            }

            startActivity(i);
            dialog.dismiss();
        });

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }

    private static class Slot {
        public String time;
        public boolean isAvailable;

        public Slot(String time, boolean isAvailable) {
            this.time = time;
            this.isAvailable = isAvailable;
        }
    }
    private void showApplyCouponDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.dialog_apply_coupon, null);
        builder.setView(view);

        AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        EditText etCouponCode = view.findViewById(R.id.etCouponCode);
        Button btnCancel = view.findViewById(R.id.btnCancel);
        Button btnApply = view.findViewById(R.id.btnApply);

        btnCancel.setOnClickListener(v -> dialog.dismiss());

        btnApply.setOnClickListener(v -> {
            String couponCode = etCouponCode.getText().toString().trim();
            if (!couponCode.isEmpty()) {
                applyEnteredCoupon(couponCode);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Please enter a valid coupon code", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }


    private void applyEnteredCoupon(String couponCode) {
        try{
            if (Constants.haveInternet(getApplicationContext())) {
                CouponApplyRequest request = new CouponApplyRequest(
                        couponCode,
                        Integer.parseInt(amount),
                        Integer.parseInt(userId),
                        Integer.parseInt(providerId),
                        appointmentMode,
                        1
                );

                                ApiClient.getRestAPI().applyCoupon(request).enqueue(new Callback<ApplyCouponResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ApplyCouponResponse> call, @NonNull retrofit2.Response<ApplyCouponResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            couponAmount=response.body().getData().getDiscount();
                            docterBookingBinding.tvApplycode.setText("Applied Coupon Code : "+couponCode);
                            docterBookingBinding.tvCoupon.setText("-"+(String.valueOf(response.body().getData().getDiscount())));
                            totalamount = Integer.parseInt(amount) + systemcharge - response.body().getData().getDiscount();
                            docterBookingBinding.tvTotalamount.setText("₹" + String.valueOf(totalamount));
                           docterBookingBinding.ivClearCoupon.setVisibility(VISIBLE);
                            couponId=response.body().getData().getCouponId();
                        } else {
                            dismissLoadingDialog();
                            if (response.errorBody() != null) {
                                try {


//                                    String errorJson = response.errorBody().string(); // ✅ this is the actual raw JSON
//                                    JsonObject jsonObject = JsonParser.parseString(errorJson).getAsJsonObject();
//
//                                    if (jsonObject.has("errors")) {
//                                        String errorMessage = jsonObject.get("errors").getAsString();
//                                        Toast.makeText(DoctorAppointmentActivity.this, errorMessage, Toast.LENGTH_LONG).show();
//                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(DoctorAppointmentActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                                }
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ApplyCouponResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            }

        }catch (Exception exception)
        {

        }

    }

}

