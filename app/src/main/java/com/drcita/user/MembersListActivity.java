package com.drcita.user;
import static android.widget.Toast.LENGTH_SHORT;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.drcita.user.adapter.MemberAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityAddMemberBinding;
import com.drcita.user.models.profile.AddNewMember;
import com.drcita.user.models.profile.RelationResponse;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.models.userprofile.MemberResponse;
import com.drcita.user.models.userprofile.MemeberRequest;
import com.drcita.user.models.userprofile.UserData;
import com.drcita.user.retrofit.ApiClient;
import com.marcinorlowski.fonty.Fonty;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MembersListActivity extends LanguageBaseActivity {
    ActivityAddMemberBinding addMemberBinding;
    MemberAdapter membersAdapter;
    List<RelationResponse.Relation> relationshipdata;
    private int relationId,genderId;
    private String userId;
    ArrayAdapter<String> relationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMemberBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_member);
        Fonty.setFonts(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        addMemberBinding.rvMembers.setLayoutManager(gridLayoutManager);
          setupMembers();
          addMemberBinding.llBack.setOnClickListener(view -> finish());
        getRelationships();

        addMemberBinding.btnAddMember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    showAddMemberDialog();
                }
            }
        });
    }

    private void setupMembers() {
        try{

            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String userId = sp.getString(Constants.USER_ID, "");
            try {
                if (Constants.haveInternet(getApplicationContext())) {
                    showLoadingDialog();
                    MemeberRequest request = new MemeberRequest();
                    request.setUserId(Integer.parseInt(userId));


                    ApiClient.getRestAPI().getMembers(request).enqueue(new Callback<MemberResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<MemberResponse> call, @NonNull Response<MemberResponse> response) {
                            dismissLoadingDialog();
                            if (response.isSuccessful()) {
                                if (response.isSuccessful() && response.body() != null) {

                                    getMembersResponse(response.body());
                                }

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MemberResponse> call, @NonNull Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(MembersListActivity.this, t.getMessage(), LENGTH_SHORT).show();
                            dismissLoadingDialog();
                        }
                    });
                }

            }catch (Exception e)
            {
                e.getMessage();
            }

        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void getMembersResponse(MemberResponse body) {

        try{

       membersAdapter=new MemberAdapter(MembersListActivity.this,body.getData());
       addMemberBinding.rvMembers.setAdapter(membersAdapter);

        }catch (Exception ex)
        {
            ex.getMessage();
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
                Constants.InternetSettings(MembersListActivity.this);
            }
        } catch (Exception ignored) {
        }

    }

    private void displayRelatiomshipsResponse(List<RelationResponse.Relation> data) {
        relationshipdata=data;

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
                    (view1, year, month, day) -> {
                        // Format with leading zeroes
                        String formattedDate = String.format("%02d-%02d-%04d", day, month + 1, year);
                        etDOB.setText(formattedDate);
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
            );

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
                        2,
                        diseaseList
                );

                ApiClient.getRestAPI().addMemberNew(request).enqueue(new Callback<RestResponse>() {
                    public void onResponse(@NonNull Call<RestResponse> call, @NonNull retrofit2.Response<RestResponse> response) {
                        dismissLoadingDialog();
                        if (response.isSuccessful()) {
                            Toast.makeText(MembersListActivity.this, response.body().getMessage(),Toast.LENGTH_SHORT).show();
                             setupMembers();
                            membersAdapter.notifyDataSetChanged();
                        }
                        else {
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
                Constants.InternetSettings(MembersListActivity.this);
            }
        } catch (Exception ignored) {
        }

    }
}