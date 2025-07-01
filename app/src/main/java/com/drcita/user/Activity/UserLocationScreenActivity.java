package com.drcita.user.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.drcita.user.DashBoardActivity;
import com.drcita.user.LanguageBaseActivity;
import com.drcita.user.R;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityLocationBinding;
import com.drcita.user.models.cities.CityRequestData;
import com.drcita.user.models.cities.CityResponse;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.models.states.StateRequest;
import com.drcita.user.models.states.StateResponse;
import com.drcita.user.models.userlocation.UpdateUserLocationRequestData;
import com.drcita.user.retrofit.ApiClient;
import com.marcinorlowski.fonty.Fonty;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLocationScreenActivity  extends LanguageBaseActivity {


    ActivityLocationBinding locationBinding;

    List<CityResponse.City> cityList = new ArrayList<>();
    List<StateResponse.State> stateList = new ArrayList<>();

    List<String> stateNames = new ArrayList<>();
    List<String> cityNames = new ArrayList<>();

    int selectedStateId = -1;
    int selectCityId=-1;
    String selectedStateName = "", selectedCityName = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationBinding = DataBindingUtil.setContentView(this, R.layout.activity_location);
        Fonty.setFonts(this);
        // Initial placeholder
        stateNames.add("Select State");
        cityNames.add("Select City");

        ArrayAdapter<String> stateAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, stateNames);

        ArrayAdapter<String> cityAdapter = new ArrayAdapter<>(this, R.layout.spinner_item, cityNames);

        locationBinding.spStates.setAdapter(stateAdapter);
        locationBinding.spCities.setAdapter(cityAdapter);

        fetchStates();
        locationBinding.spStates.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                if (pos == 0) {
                    selectedStateId = -1;
                    cityNames.clear();
                    cityNames.add("Select City");
                    locationBinding.spCities.setAdapter(new ArrayAdapter<>(UserLocationScreenActivity.this, R.layout.spinner_item, cityNames));
                    return;
                }

                selectedStateName = stateNames.get(pos);
                selectedStateId = stateList.get(pos - 1).getId(); // Subtract 1 because of "Select State"

                fetchCities(selectedStateId);
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

        locationBinding.spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {

                if(pos==0)
                {
                    selectCityId=-1;
                    selectedCityName="";
                    return;
                }
                selectedCityName = cityNames.get(pos);
                selectCityId = cityList.get(pos - 1).getId(); // Subtract 1 because of "Select State"
            }

            public void onNothingSelected(AdapterView<?> parent) {}
        });

       locationBinding.btnsubmit.setOnClickListener(v -> {
            if (!selectedStateName.equals("Select State") && !selectedCityName.equals("Select City")) {
                updateLocations();
            } else {
                Toast.makeText(this, "Please select both state and city", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateLocations() {
        // 1. Check for internet connectivity first
        if (!Constants.haveInternet(getApplicationContext())) {
            Constants.haveInternet(UserLocationScreenActivity.this);
            return;
        }

        showLoadingDialog();

        // 2. Read USER_ID from SharedPreferences
        SharedPreferences prefs = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
        String userIdString = prefs.getString(Constants.USER_ID, "");
        if (userIdString.isEmpty()) {
            Toast.makeText(this, "User ID not found", Toast.LENGTH_SHORT).show();
            dismissLoadingDialog();
            return;
        }

        // 3. Prepare the request body
        UpdateUserLocationRequestData locationRequestData = new UpdateUserLocationRequestData();
        locationRequestData.setUserId(userIdString);                  // assuming setUserId(String)
        locationRequestData.setStateId(selectedStateId);              // assuming setStateId(int)
        locationRequestData.setCityId(selectCityId);                  // assuming setCityId(int)

        // 4. Call your API. Replace UpdateLocationResponse with your actual response model
        ApiClient.getRestAPI()
                .updateUserLocation(locationRequestData)
                .enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RestResponse> call,
                                           @NonNull Response<RestResponse> response) {
                        // Always dismiss the loading dialog first
                        dismissLoadingDialog();

                        if (response.isSuccessful() && response.body() != null) {
                            // 5.a. Successful 2xx response: pass the parsed RestResponse to your handler
                            updateLocationResponse(response.body());
                        } else {
                            // 5.b. Non‐2xx status code. Attempt to extract and show server‐side errors
                            try {
                                if (response.errorBody() != null) {
                                    String errorJson = response.errorBody().string();
                                    Constants.displayError(errorJson, getBaseContext());
                                } else {
                                    Toast.makeText(UserLocationScreenActivity.this,
                                            "Unknown server error", Toast.LENGTH_SHORT).show();
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                                Toast.makeText(UserLocationScreenActivity.this,
                                        "Error parsing server response", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestResponse> call,
                                          @NonNull Throwable t) {
                        // 6. Network or unexpected error (e.g., timeout, no connectivity)
                        dismissLoadingDialog();
                        Toast.makeText(UserLocationScreenActivity.this,
                                "Network error: " + t.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateLocationResponse(RestResponse body) {

        try {
            if(body.isSuccess())
            {
                SharedPreferences preferences = getSharedPreferences(Constants.PREFERENCE_NAME, MODE_PRIVATE);
                // Save state and city IDs
                preferences.edit().putString(Constants.STATE_ID, String.valueOf(selectedStateId)).apply();
                preferences.edit().putString(Constants.CITY_ID, String.valueOf(selectCityId)).apply();
                Intent i =new Intent(this, DashBoardActivity.class);
                startActivity(i);
            }
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }




    private void fetchStates() {
        // Use your API manager or Retrofit call
        if (Constants.haveInternet(getApplicationContext())) {
            showLoadingDialog();
            StateRequest otpRequest = new StateRequest();
            otpRequest.setCountryId(76);

            ApiClient.getRestAPI().getStates(otpRequest).enqueue(new Callback<StateResponse>() {
                @Override
                public void onResponse(@NonNull Call<StateResponse> call, @NonNull Response<StateResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        if (response.body() != null)
                            if (response.isSuccessful() && response.body() != null) {
                                stateList = response.body().getData();
                                for (StateResponse.State state : stateList) {
                                    stateNames.add(state.getStateName());
                                }

                                ArrayAdapter<String> adapter = new ArrayAdapter<>(UserLocationScreenActivity.this, R.layout.spinner_item, stateNames);
                                adapter.setDropDownViewResource(R.layout.spinner_item);
                                locationBinding.spStates.setAdapter(adapter);
                            }
                    else {
                                try {
                                    Constants.displayError(response.errorBody().string(), getBaseContext());
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<StateResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(UserLocationScreenActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });

        } else {
            Constants.haveInternet(UserLocationScreenActivity.this);
        }
    }


    private void fetchCities(int stateId) {

        if (Constants.haveInternet(getApplicationContext())) {
            showLoadingDialog();
            CityRequestData otpRequest = new CityRequestData(stateId);

            ApiClient.getRestAPI().getCities(otpRequest).enqueue(new Callback<CityResponse>() {
                @Override
                public void onResponse(@NonNull Call<CityResponse> call, @NonNull Response<CityResponse> response) {
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        if (response.isSuccessful() && response.body() != null) {
                    cityList = response.body().getData();
                    cityNames.clear();
                    cityNames.add("Select City");

                    for (CityResponse.City city : cityList) {
                        cityNames.add(city.getCityName());
                    }

                    ArrayAdapter<String> adapter = new ArrayAdapter<>(UserLocationScreenActivity.this, R.layout.spinner_item, cityNames);
                            adapter.setDropDownViewResource(R.layout.spinner_item);
                    locationBinding.spCities.setAdapter(adapter);
                }

                    }
                }

                @Override
                public void onFailure(@NonNull Call<CityResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    Toast.makeText(UserLocationScreenActivity.this, t.getMessage(), Toast.LENGTH_SHORT).show();
                    dismissLoadingDialog();
                }
            });

        } else {
            Constants.haveInternet(UserLocationScreenActivity.this);
        }
    }

}
