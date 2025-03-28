package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityWriteReviewBinding;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.review.WriteReviewRequest;
import com.drcita.user.retrofit.ApiClient;

import java.io.IOException;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class WriteReview extends LanguageBaseActivity implements AdapterView.OnItemSelectedListener {

    private ActivityWriteReviewBinding activityWriteReviewBinding;
    String[] rating = {"Select Rating", "1","2","3","4","5"};
    private int ratingPosition;
    private String ratingList;
    private ProgressDialog progress;
    String userId;
    int providerId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityWriteReviewBinding = DataBindingUtil.setContentView(this, R.layout.activity_write_review);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                providerId = 0;
            } else {
                providerId = extras.getInt("providerId");
            }
        } else {
        }
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        setSupportActionBar(activityWriteReviewBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Write Your Review");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, rating);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        activityWriteReviewBinding.ratingSP.setAdapter(adapter);
        activityWriteReviewBinding.ratingSP.setOnItemSelectedListener(this);

        activityWriteReviewBinding.ratingSP.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                ratingPosition = position;
                ratingList = activityWriteReviewBinding.ratingSP.getSelectedItem().toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        activityWriteReviewBinding.backReview.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                finish();
            }
        });
        activityWriteReviewBinding.submitwritereview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validations();
            }
        });
    }

    private void validations() {
        if (activityWriteReviewBinding.descriptionReview.getText().toString().equals("")){
            Constants.ToastShort(WriteReview.this, "Enter Description");
            return;
        }
        if (activityWriteReviewBinding.ratingSP.getSelectedItemPosition() == 0) {
            Toast.makeText(WriteReview.this, "Please Select Rating", Toast.LENGTH_SHORT).show();
            return;
        }

        callApi();
    }

    private void callApi() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            WriteReviewRequest request = new WriteReviewRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setComments(activityWriteReviewBinding.descriptionReview.getText().toString());
            request.setRating(ratingPosition);
            request.setProviderId(providerId);
            ApiClient.getRestAPI().writeProviderReview(request).enqueue(new Callback<GlobalResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        writeReviewResponse(Objects.requireNonNull(response.body()));
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
                    Toast.makeText(WriteReview.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(WriteReview.this);
        }
    }

    private void writeReviewResponse(GlobalResponse globalResponse) {
        dismissLoadingDialog();
        Toast.makeText(WriteReview.this, globalResponse.getMessage(), Toast.LENGTH_LONG).show();
        activityWriteReviewBinding.descriptionReview.setText("");
        activityWriteReviewBinding.ratingSP.setSelection(0);
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}