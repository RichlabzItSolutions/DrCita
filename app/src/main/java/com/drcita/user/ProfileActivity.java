package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.common.PreferenceUtil;
import com.drcita.user.databinding.ActivityProfileBinding;
import com.drcita.user.models.GlobalRequest;
import com.drcita.user.models.profile.UpdateProfileRequest;
import com.drcita.user.models.profile.UpdateProfileResponse;
import com.drcita.user.models.region.DataItem;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends LanguageBaseActivity implements AdapterView.OnItemSelectedListener, View.OnClickListener{

    private ActivityProfileBinding activityProfileBinding;
    private int genderPosition,languagePosition=1,regionPosition;
        int  languageList,  regionListString;
    String[] gender = {"Select Gender", "Female", "Male"};
    String[] language = {"Select Language", "English", "Somali"};
    private ProgressDialog progress;
    private ArrayList<String> regionList;
    private ArrayList<Integer> regionIdInList;
    String userId,profilestatus;
    Boolean exit = false;
    final Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener date;
    List<DataItem> accademicyearModels;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityProfileBinding = DataBindingUtil.setContentView(this,R.layout.activity_profile);
        myCalendar.add(Calendar.YEAR,-18);
        //Log.d("mounika",""+getIntent().getExtras().getBundle("mounika").getParcelable("data"));

        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
       // profilestatus = sp.getString(Constants.PROFILESTATUS, profilestatus);
    /*    if (profilestatus.equals("1")){
            Intent intent = new Intent(getApplicationContext(),DashBoardActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(getApplicationContext(),ProfileActivity.class);
            startActivity(intent);
        }*/

        activityProfileBinding.calendardate.setOnClickListener(this);
        activityProfileBinding.appotimentdate.setOnClickListener(this);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, gender);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityProfileBinding.profilegender.setAdapter(adapter);
        activityProfileBinding.profilegender.setOnItemSelectedListener(this);

        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this, R.layout.spinner_item, language);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityProfileBinding.profilelanguage.setAdapter(adapter1);
        activityProfileBinding.profilelanguage.setSelection(2);
        activityProfileBinding.profilelanguage.setOnItemSelectedListener(this);

        activityProfileBinding.profilegender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                genderPosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activityProfileBinding.profilelanguage.setSelection(languagePosition);

        activityProfileBinding.profilelanguage.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                languagePosition = position;
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activityProfileBinding.updateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityProfileBinding.profilegender.getSelectedItemPosition() == 0) {
                    Toast.makeText(ProfileActivity.this, "Please Select Gender", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activityProfileBinding.profilelanguage.getSelectedItemPosition() == 0) {
                    Toast.makeText(ProfileActivity.this, "Please Select Language", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (activityProfileBinding.profileregion.getSelectedItemPosition() == 0) {
                    Toast.makeText(ProfileActivity.this, "Please Select Region", Toast.LENGTH_SHORT).show();
                    return;
                }
                updateProfile();
            }
        });
        getRegion();
    }


    private void updateProfile() {
        showLoadingDialog();

         if(activityProfileBinding.appotimentdate.getText().length()==0)
         {
             Toast.makeText(this,"Please select Date of Birth",Toast.LENGTH_LONG).show();
         }
         else {
             if (Constants.haveInternet(getApplicationContext())) {
                 UpdateProfileRequest request = new UpdateProfileRequest();
                 request.setUserId(userId);
                 request.setGender(String.valueOf(genderPosition));
                 request.setLanguage(languagePosition);
                 request.setRegion(regionListString);
                 request.setDob(activityProfileBinding.appotimentdate.getText().toString());
                 ApiClient.getRestAPI().userProfileUpdate(request).enqueue(new Callback<UpdateProfileResponse>() {
                     @RequiresApi(api = Build.VERSION_CODES.N)
                     @Override
                     public void onResponse(@NonNull Call<UpdateProfileResponse> call, @NonNull Response<UpdateProfileResponse> response) {
                         if (response.isSuccessful()) {
                             dismissLoadingDialog();
                             SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                             preferences.edit().putInt(Constants.REGION, regionListString).apply();
                             preferences.edit().putInt(Constants.LANGUAGE, languagePosition).apply();
                             if (LanguageEnum.getByValue(languagePosition) != null) {
                                 PreferenceUtil.getInstance().save(Constants.LANGUAGE1, getString(LanguageEnum.getByValue(languagePosition).getName()));
                             }
                             //   LoginResponse loginResponse = Parcels.unwrap(getIntent().getParcelableExtra("data"));

                             // LoginResponse loginResponse = Parcels.unwrap(getIntent().getExtras().getParcelable("data"));
//                        SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                             //  preferences.edit().putString(Constants.USER_ID, loginResponse.getData().getUserId()).apply();
                             //   preferences.edit().putString(Constants.NAME, loginResponse.getData().getName()).apply();
                             //   preferences.edit().putString(Constants.MOBILE, loginResponse.getData().getMobile()).apply();
                             // preferences.edit().putInt(Constants.PROFILESTATUS, 1).apply();
                             //  preferences.edit().putInt(Constants.REGION, (loginResponse.getData().getRegion())).apply();
                             //   preferences.edit().putString(Constants.LANGUAGE, String.valueOf(loginResponse.getData().getLanguage())).apply();
                             dismissLoadingDialog();
                             //Toast.makeText(ProfileActivity.this, Constants.PROFILESTATUS, Toast.LENGTH_LONG).show();

                             Toast.makeText(ProfileActivity.this, response.body().getMessage(), Toast.LENGTH_LONG).show();
                             Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
                             preferences.edit().putInt(Constants.PROFILESTATUS, 1).apply();
                             startActivity(intent);
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
                     public void onFailure(@NonNull Call<UpdateProfileResponse> call, @NonNull Throwable t) {
                         dismissLoadingDialog();
                         t.printStackTrace();
                         Toast.makeText(ProfileActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                     }
                 });
             } else {
                 Constants.InternetSettings(ProfileActivity.this);
             }
         }
    }




    private void getRegion() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            GlobalRequest globalRequest = new GlobalRequest();
            globalRequest.setUserId(userId);
            ApiClient.getRestAPI().cityMogadishu(globalRequest).enqueue(new Callback<com.drcita.user.models.region.Response>() {
                @Override
                public void onResponse(@NonNull Call<com.drcita.user.models.region.Response> call, @NonNull Response<com.drcita.user.models.region.Response> response) {
                    getcityResponse(Objects.requireNonNull(response.body()));
                }
                @Override
                public void onFailure(@NonNull Call<com.drcita.user.models.region.Response> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(ProfileActivity.this);
        }
    }

    private void getcityResponse(com.drcita.user.models.region.Response response) {
        try {
            dismissLoadingDialog();
            String description = response.getMessage();
            regionList = new ArrayList<String>();
            regionIdInList = new ArrayList<>();
            if (response.getStatus().equals("success")) {
                dismissLoadingDialog();
                accademicyearModels = response.getData();
                for (int i = 0; i < accademicyearModels.size(); i++) {
                    String region = accademicyearModels.get(i).getName();
                    regionList.add(region);
                    int regionId = accademicyearModels.get(i).getId();
                    regionIdInList.add(regionId);
                }
                setRegion();

            } else {
                setRegion();
                Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
            }
        }catch (Exception ex)

        {
            ex.getMessage();
        }

    }



    private void setRegion() {
        regionList.add(0, ("Select Region"));
        regionIdInList.add(0, 0);
        ArrayAdapter aa = new ArrayAdapter<>(this, R.layout.spinner_item, regionList);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        activityProfileBinding.profileregion.setAdapter(aa);

        activityProfileBinding.profileregion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                regionPosition = i;
                if (regionPosition == 0) {
                    //recyclerView.setVisibility(View.GONE);
                } else {
                    regionListString = regionIdInList.get(regionPosition);
                    SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, 0);
                    preferences.edit().putString(Constants.Currancy_Type, String.valueOf(accademicyearModels.get(regionPosition-1).getCurrencyType())).apply();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

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
                activityProfileBinding.appotimentdate.setText(dayOfMonth + "-" + "" + (monthOfYear + 1) + "-" + "" + year);

            }
        });
       // datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    @Override
    public void onBackPressed() {
        if (exit) {
            Intent homeIntent = new Intent(Intent.ACTION_MAIN);
            homeIntent.addCategory(Intent.CATEGORY_HOME);
            homeIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(homeIntent);
        } else {
            Toast.makeText(getApplicationContext(), "Press Again To Exit", Toast.LENGTH_LONG).show();
            exit = true;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}