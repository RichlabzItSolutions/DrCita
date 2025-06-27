package com.drcita.user.Activity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.drcita.user.HospitalsListActivity;
import com.drcita.user.LanguageBaseActivity;
import com.drcita.user.R;
import com.drcita.user.adapter.search.SearchAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivitySearchBinding;
import com.drcita.user.models.home.Doctor;
import com.drcita.user.models.home.Hospital;
import com.drcita.user.models.home.SearchRequest;
import com.drcita.user.models.home.SearchResponse;
import com.drcita.user.models.home.SectionItem;
import com.drcita.user.models.home.Specialist;
import com.drcita.user.retrofit.ApiClient;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SearchActivity extends LanguageBaseActivity {

    private ActivitySearchBinding searchbinding;
    SearchAdapter adapter;
    List<SectionItem> itemList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchbinding = DataBindingUtil.setContentView(this, R.layout.activity_search);
//        adapter = new SearchAdapter(itemList);
//        searchbinding.search.setLayoutManager(new LinearLayoutManager(this));
//        searchbinding.search.setAdapter(adapter);

        searchbinding.search.setLayoutManager(new LinearLayoutManager(this));
         searchbinding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String query = s.toString().trim();
                if (!query.isEmpty() &&query.length()>3) {
                    callSearchApi(query);
                } else if(query.isEmpty()) {
                    callSearchApi("");
                    itemList.clear();
                    adapter.notifyDataSetChanged();
                }
            }
        });
        callSearchApi("");
        itemList.clear();
        itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "Search Results"));
        itemList.add(new SectionItem(SectionItem.TYPE_ITEM, "No results found", true));

    }


    private void callSearchApi(String query) {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            SearchRequest request = new SearchRequest(query);
            ApiClient.getRestAPI().searchHomePage(request).enqueue(new Callback<SearchResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<SearchResponse> call, @NonNull Response<SearchResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        if (response.body() != null) {
                            searchResponse(response.body());
                        } else {
                            itemList.clear();
                            itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "Search Results"));
                            itemList.add(new SectionItem(SectionItem.TYPE_ITEM, "No results found", true));

                            adapter = new SearchAdapter(itemList);
                            searchbinding.search.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                        }
//                        contactRequestResponse(Objects.requireNonNull(response.body()));
                    } else {
                        try {
                            itemList.clear();
                            itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "Search Results"));
                            itemList.add(new SectionItem(SectionItem.TYPE_ITEM, "No results found", true));

                            adapter = new SearchAdapter(itemList);
                            searchbinding.search.setAdapter(adapter);
                            adapter.notifyDataSetChanged();
                           // Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SearchResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(SearchActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(SearchActivity.this);
        }

    }
        private void searchResponse(SearchResponse body) {
        try {
            itemList.clear();
            boolean hasHospital = body.getData().getHospitals() != null && !body.getData().getHospitals().isEmpty();
            boolean hasDoctor = body.getData().getDoctors() != null && !body.getData().getDoctors().isEmpty();
            boolean hasSpecialist = body.getData().getSpecialists() != null && !body.getData().getSpecialists().isEmpty();
            if (hasHospital) {
                itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "List of Hospitals"));
                for (Hospital h : body.getData().getHospitals()) {
                    itemList.add(new SectionItem(SectionItem.TYPE_ITEM, h.getName(), h.getId(), "hospital"));

                }
            }
                else {
                    itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "List of Hospitals"));
                itemList.add(new SectionItem(SectionItem.TYPE_ITEM, "No hospitals available", true));

            }

            if (hasDoctor) {
                itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "List of Doctors"));
                for (Doctor d : body.getData().getDoctors()) {

                    itemList.add(new SectionItem(SectionItem.TYPE_ITEM, d.getName(), d.getId(), "doctor"));
                }
            }
            else {
                itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "List of Doctors"));
                itemList.add(new SectionItem(SectionItem.TYPE_ITEM, "No Doctors available", true));
            }

            if (hasSpecialist) {
                itemList.add(new SectionItem(SectionItem.TYPE_HEADER, "List of Specialists"));
                for (Specialist s : body.getData().getSpecialists()) {
                    itemList.add(new SectionItem(SectionItem.TYPE_ITEM, s.getName(), s.getId(), "specialist"));
                }

            }
            else {
                itemList.add(new SectionItem(SectionItem.TYPE_HEADER,  "List of Specialists"));
                itemList.add(new SectionItem(SectionItem.TYPE_ITEM, "No Specialists available", true));
            }


            adapter = new SearchAdapter(itemList);
            searchbinding.search.setAdapter(adapter);
            adapter.notifyDataSetChanged();



            adapter.setOnItemClickListener(item -> {
                if (item.getType() == SectionItem.TYPE_ITEM && !item.isPlaceholder()) {
                    int id = item.getId();
                    String sectionType = item.getSectionType();
                    Intent intent;
                    switch (sectionType) {
                        case "hospital":
                            intent = new Intent(SearchActivity.this, HospitalsListActivity.class);
                            intent.putExtra("hospitalId", id);
                            intent.putExtra("specialization",0);
                            intent.putExtra("doctorId", 0);
                            startActivity(intent);
                            break;

                        case "doctor":
                            intent = new Intent(SearchActivity.this, HospitalsListActivity.class);
                            intent.putExtra("doctorId", id);
                            intent.putExtra("specialization",0);
                            intent.putExtra("hospitalId",0);
                            startActivity(intent);
                            break;

                        case "specialist":
                            intent = new Intent(SearchActivity.this, HospitalsListActivity.class);
                            intent.putExtra("hospitalId", 0);
                            intent.putExtra("specialization", id);
                            intent.putExtra("doctorId", 0);
                            startActivity(intent);
                            break;
                    }
                }
            });


        } catch (Exception ex) {
            ex.printStackTrace();
            Toast.makeText(this, "Something went wrong while parsing data.", Toast.LENGTH_SHORT).show();
        }
    }



}