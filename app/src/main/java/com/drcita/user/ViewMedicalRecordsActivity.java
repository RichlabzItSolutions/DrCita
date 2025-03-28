package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.adapter.DocsAdapter;
import com.drcita.user.adapter.PhotosAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityViewMedicalRecordsBinding;
import com.drcita.user.models.viewmedicalrecords.Data;
import com.drcita.user.models.viewmedicalrecords.DocsItem;
import com.drcita.user.models.viewmedicalrecords.PhotosItem;
import com.drcita.user.models.viewmedicalrecords.ViewmedicalRequest;
import com.drcita.user.models.viewmedicalrecords.ViewmedicalResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;

public class ViewMedicalRecordsActivity extends LanguageBaseActivity {

    private ActivityViewMedicalRecordsBinding binding;
    private int id;
    private ProgressDialog progress;
    private List<Data> responses = new ArrayList<Data>();
    private List<PhotosItem> photosItems = new ArrayList<PhotosItem>();
    private List<DocsItem> docsItems = new ArrayList<com.drcita.user.models.viewmedicalrecords.DocsItem>();
    private PhotosAdapter photosAdapter;
    private DocsAdapter docsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_view_medical_records);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                id = 0;
            } else {
                id = extras.getInt("id");
            }
        } else {
        }
        binding.backviewmedicalrecords.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        binding.addmedicalRecordsRV.setLayoutManager(new GridLayoutManager(this, 3));
        photosAdapter = new PhotosAdapter(this, photosItems);
        binding.addmedicalRecordsRV.setAdapter(photosAdapter);
        binding.documentsRecordsRV.setLayoutManager(new GridLayoutManager(this,3));
        docsAdapter = new DocsAdapter(this, docsItems);
        binding.documentsRecordsRV.setAdapter(docsAdapter);
        getMedicalRecord();


    }

    private void getMedicalRecord() {
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                ViewmedicalRequest getMedicalRecordsRequest = new ViewmedicalRequest();
                getMedicalRecordsRequest.setAppointmentId(id);
                ApiClient.getRestAPI().getMedicalRecord(getMedicalRecordsRequest).enqueue(new Callback<ViewmedicalResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<ViewmedicalResponse> call, @NonNull retrofit2.Response<ViewmedicalResponse> response) {
                        if (response.isSuccessful()) {
                            dismissLoadingDialog();
                            getviewMedicalRecordsResponse(Objects.requireNonNull(response.body()));

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
                    public void onFailure(@NonNull Call<ViewmedicalResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(ViewMedicalRecordsActivity.this);
            }


    }

    private void getviewMedicalRecordsResponse(ViewmedicalResponse viewmedicalResponse) {
        dismissLoadingDialog();
        String description = viewmedicalResponse.getMessage();
        if (viewmedicalResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            Data dataItems = viewmedicalResponse.getData();
                binding.descriptionTV.setText(viewmedicalResponse.getData().getDescription());
                binding.reasonTV.setText(viewmedicalResponse.getData().getReason());
                responses.clear();
                photosItems.addAll(viewmedicalResponse.getData().getPhotos());
                photosAdapter.notifyDataSetChanged();
                docsItems.addAll(viewmedicalResponse.getData().getDocs());
                docsAdapter.notifyDataSetChanged();

            //setRegion();

        } else {
            //setRegion();
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
    }


}