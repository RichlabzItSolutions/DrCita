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

import com.bumptech.glide.Glide;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.DocterBookingBinding;
import com.drcita.user.models.appointment.AppointmentResponse;
import com.drcita.user.models.appointment.BookAppointmentRequest;
import com.drcita.user.models.appointment.DocterDetailResoponse;
import com.drcita.user.models.appointment.DocterDetailsRequest;
import com.drcita.user.models.appointment.DoctorData;
import com.drcita.user.models.appointment.TimeSlot;
import com.drcita.user.models.profile.AddNewMember;
import com.drcita.user.models.profile.RelationResponse;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.models.userprofile.UserData;
import com.drcita.user.models.userprofile.UserRequestData;
import com.drcita.user.models.userprofile.UsersResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.flexbox.FlexboxLayout;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;

public class DoctorAppointmentActivity extends LanguageBaseActivity {

    private String providerId, docterId, amount, paymenttype, subuserId, stotTime = "";
    private DocterBookingBinding docterBookingBinding;
    private String userId;
    int appointmentMode, totalamount;
    List<RelationResponse.Relation> relationshipdata;
    private int relationId,genderId;
    ArrayAdapter<String> relationAdapter;
    private boolean isSummary=false;
    String selectedDate = null;

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
                paymenttype = getIntent().getStringExtra("paymentype");
                docterBookingBinding.tvMode.setText(paymenttype);
                docterBookingBinding.tvamount.setText("₹" + amount);
                if (paymenttype.equals("Online")) {
                    appointmentMode = 2;
                } else {
                    appointmentMode = 1;
                }

            }
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            selectedDate = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
        }

        getRelationships();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            docterBookingBinding.llAddmemeber.setOnClickListener(v ->
                    showAddMemberDialog()
            );
        }
        docterBookingBinding.llBack.setOnClickListener(view1 -> {
            if (isSummary) {
                docterBookingBinding.llContinuebooking.setVisibility(VISIBLE);
                docterBookingBinding.btnContinue.setVisibility(VISIBLE);
                docterBookingBinding.llSlot.setVisibility(GONE);
                isSummary=false;
            }
            else {

                finish();
            }


        });
        docterBookingBinding.dateTextView.setOnClickListener(v -> showDatePicker());
        getUserProfile();
        getDocterDetails();

        docterBookingBinding.btnContinue.setOnClickListener(view -> {
            if (stotTime.isEmpty()) {
                Toast.makeText(DoctorAppointmentActivity.this,
                        "Please select slot", Toast.LENGTH_SHORT).show();
            } else {
                 isSummary=true;
                docterBookingBinding.llContinuebooking.setVisibility(GONE);
                docterBookingBinding.btnContinue.setVisibility(GONE);
                docterBookingBinding.llSlot.setVisibility(VISIBLE);
                docterBookingBinding.tvAppoinmentddate.setText(docterBookingBinding.dateTextView.getText().toString());
                docterBookingBinding.tvConsultationfees.setText(amount);
                docterBookingBinding.tvSlotime.setText(stotTime);
            }
        });
        docterBookingBinding.btnPayNow.setOnClickListener(view -> bookAppointmentApI());
        Calendar today = Calendar.getInstance();
        int year = today.get(Calendar.YEAR);
        int month = today.get(Calendar.MONTH) + 1; // Calendar.MONTH is zero‐based
        int day = today.get(Calendar.DAY_OF_MONTH);
        // 3. Format & set it
        selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", day, month, year);
        docterBookingBinding.dateTextView.setText(selectedDate);
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
        relationshipdata=data;

    }

    private void bookAppointmentApI() {
        try {
            showLoadingDialog();
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            userId = sp.getString(Constants.USER_ID, "");
            if (Constants.haveInternet(getApplicationContext())) {
                BookAppointmentRequest bookAppointmentRequest = new BookAppointmentRequest(
                        Integer.parseInt(docterId),
                        Integer.parseInt(providerId),
                        docterBookingBinding.dateTextView.getText().toString(),
                        stotTime,
                        Integer.parseInt(userId),
                        Integer.parseInt(subuserId),
                        appointmentMode,
                        "1",// 1-> docter ,2->Scan
                        Integer.parseInt(amount),
                        0,
                        0,

                        Integer.parseInt(docterBookingBinding.tvSystemcharges.getText().toString()),
                        totalamount,
                        "dggdgdg2222"


                );


                ApiClient.getRestAPI().bookAppointment(bookAppointmentRequest).enqueue(new Callback<AppointmentResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<AppointmentResponse> call, @NonNull retrofit2.Response<AppointmentResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            Toast.makeText(DoctorAppointmentActivity.this,
                                    response.message(), Toast.LENGTH_SHORT).show();
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
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                (view, year1, month1, dayOfMonth) -> {
                    // Month is 0-based in callback, so add +1 for display
                     selectedDate = String.format(Locale.getDefault(), "%02d-%02d-%04d", dayOfMonth, month1 + 1, year1);
                    docterBookingBinding.dateTextView.setText(selectedDate);

                    // Call after setting date
                    getDocterDetails();
                },
                year, month, day
        );

        datePickerDialog.show();
    }


    private void getDocterDetails() {
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


        docterBookingBinding.tvDoctorName.setText(data.getDoctorName());
        docterBookingBinding.tvHospitalName.setText(data.getHospitalName());
        docterBookingBinding.tvdesegnation.setText(TextUtils.join(", ", data.getQualifications()));
        docterBookingBinding.tvSpecialization.setText(TextUtils.join(", ", data.getSpecializations()));
        docterBookingBinding.tvlanguage.setText(TextUtils.join(", ", data.getLanguages()));
        docterBookingBinding.tvTotalslot.setText("Total Slots \n" + data.getSlots().getTotalSlots());
        docterBookingBinding.tvAvailableslots.setText("Available Slots \n" + data.getSlots().getAvailableSlots());

        docterBookingBinding.tvSystemcharges.setText(String.valueOf(data.getSystemCharges()));
        docterBookingBinding.tvCoupon.setText(String.valueOf(data.getCoupon()));

        totalamount = data.getSystemCharges() + Integer.parseInt(amount);

        Glide.with(this)
                .load(data.getPicture())
                .placeholder(R.drawable.docter_img)
                .into(docterBookingBinding.imgDoctor);

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

            FlexboxLayout.LayoutParams textParams = new FlexboxLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
            );
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
            FlexboxLayout.LayoutParams params = new FlexboxLayout.LayoutParams(
                    widthInPx,
                    heightInPx
            );
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
        ArrayAdapter<String> genderAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                genderList);
        spinnerGender.setAdapter(genderAdapter);

        // Relation Spinner
        ArrayAdapter<String> relationAdapter = new ArrayAdapter<>(this, R.layout.spinner_item,
                relation);
        spinnerRelation.setAdapter(relationAdapter);
        spinnerRelation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {


                 relationId= relationshipdata.get(pos).getId();
            }

            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                switch (pos) {
                    case 0: // Female
                        genderValue[0] = 1;
                        genderId=1;
                        break;
                    case 1: // Male
                        genderValue[0] = 2;
                        genderId=2;
                        break;
                    case 2: // Other
                        genderValue[0] = 3;
                        genderId=3;
                        break;
                }

            }

            public void onNothingSelected(AdapterView<?> parent) {
            }


        });
        // Date Picker
        etDOB.setOnClickListener(v -> {
            Calendar calendar = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(this,
                    (view1, year, month, day) -> etDOB.setText(day + "-" + (month + 1) + "-" + year),
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH));
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
                String stateId ="" ; // from spinner or dropdown
                String cityId = "14";   // same as above
                String address = "";

                String pastSurgeries = ""; // optional
                String bloodGroupId = ""; // dropdown or input

// 4. Prepare diseases list (can be empty)
                List<AddNewMember.Disease> diseaseList = new ArrayList<>();

                AddNewMember request = new AddNewMember(
                      Integer.parseInt(userId),
                        fullName,
                        genderId,
                        age,
                        dob,
                        maritalStatus,
                        mobile,
                        email,
                        stateId,
                        cityId,
                        address,
                        pastSurgeries,
                        bloodGroupId,
                        relationId,
                        diseaseList
                );

                ApiClient.getRestAPI().addMemberNew(request).enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RestResponse> call, @NonNull retrofit2.Response<RestResponse> response) {
                        dismissLoadingDialog();

                           Toast.makeText(DoctorAppointmentActivity.this,"Member added successfully !",Toast.LENGTH_SHORT).show();
                            getUserProfile();
                            relationAdapter.notifyDataSetChanged();

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


    private static class Slot {
        public String time;
        public boolean isAvailable;

        public Slot(String time, boolean isAvailable) {
            this.time = time;
            this.isAvailable = isAvailable;
        }
    }
}

