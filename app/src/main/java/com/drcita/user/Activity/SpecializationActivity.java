package com.drcita.user.Activity;
import static android.view.View.VISIBLE;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import com.drcita.user.LanguageBaseActivity;
import com.drcita.user.R;
import com.drcita.user.adapter.CategoriWiseSpecializationAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivitySpecilizationBinding;
import com.drcita.user.models.dashboard.specilization.Specialization;
import com.drcita.user.models.departments.Department;
import com.drcita.user.models.departments.DepartmentResponse;
import com.drcita.user.models.departments.SpecializationRequest;
import com.drcita.user.models.departments.SpecializationResponse;
import com.drcita.user.retrofit.ApiClient;
import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
public class SpecializationActivity extends LanguageBaseActivity {

    private ActivitySpecilizationBinding searchbinding;
    CategoriWiseSpecializationAdapter adapter;
    private LinearLayout categoryTabs;
    private List<Specialization> specializationList = new ArrayList<>();
    int position = 2;
    private int departmentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchbinding = DataBindingUtil.setContentView(this, R.layout.activity_specilization);
        setupCategoryTabs();
        setupRecyclerView();
        // Load default category
        if (getIntent() != null) {
            position = getIntent().getIntExtra("position", 0);
            departmentId = getIntent().getIntExtra("specialization", 0);
            loadSpecializations(departmentId);
        }
    }

    private void setupCategoryTabs() {

        try {

            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {

                ApiClient.getRestAPI().getDepartments().enqueue(new Callback<DepartmentResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<DepartmentResponse> call, @NonNull retrofit2.Response<DepartmentResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null) {
                            getCategoryResponse(response.body().getData());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DepartmentResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });

            }
        } catch (Exception ex) {
            ex.getMessage();
        }
    }
    private void resetTabStyles() {
        for (int i = 0; i < categoryTabs.getChildCount(); i++) {
            TextView tab = (TextView) categoryTabs.getChildAt(i);
            tab.setBackgroundResource(R.drawable.bg_tab_unselected);
            tab.setTextColor(Color.BLACK); // reset color
        }
    }

    private void setupRecyclerView() {
        searchbinding.search.setLayoutManager(new GridLayoutManager(this, 5));
        adapter = new CategoriWiseSpecializationAdapter(this, specializationList);
        searchbinding.search.setAdapter(adapter);
    }

    private void loadSpecializations(Integer departmentId) {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            SpecializationRequest specializationRequest = new SpecializationRequest(String.valueOf(departmentId));


            ApiClient.getRestAPI().getSpecoilizationByDepartment(specializationRequest).enqueue(new Callback<SpecializationResponse>() {
                @Override
                public void onResponse(@NonNull Call<SpecializationResponse> call, @NonNull retrofit2.Response<SpecializationResponse> response) {
                    dismissLoadingDialog();
                    if (response.body() != null && response.body().getData() != null) {
                        searchbinding.nodataimage.setVisibility(View.GONE);
                        searchbinding.search.setVisibility(VISIBLE);
                        getSpecilaizationResponse(response.body().getData());

                    } else {
                        dismissLoadingDialog();
                        searchbinding.nodataimage.setVisibility(VISIBLE);
                        searchbinding.search.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SpecializationResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
            adapter.notifyDataSetChanged();

        }
    }

    private void getSpecilaizationResponse(List<Specialization> data) {
        try {
            specializationList.clear();
            specializationList.addAll(data);
            adapter.notifyDataSetChanged();
        } catch (Exception ex) {
        }
    }

    private void getCategoryResponse(List<Department> data) {
        try {
            categoryTabs = searchbinding.categoryTabs; // make sure this is initialized
            categoryTabs.removeAllViews(); // clear any existing views
            for (Department dept : data) {
                String category = dept.getDepartmentName();

                TextView tab = new TextView(this);
                tab.setText(category);
                tab.setTextSize(14f);
                tab.setPadding(24, 12, 24, 12);
                tab.setTextColor(Color.BLACK);
                tab.setBackgroundResource(R.drawable.bg_tab_unselected);
                tab.setGravity(Gravity.CENTER);

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                );
                params.setMargins(0, 0, 24, 0); // Add 24px space between tabs
                tab.setLayoutParams(params);

                tab.setOnClickListener(v -> {
                    resetTabStyles();
                    tab.setBackgroundResource(R.drawable.bg_tab_selected);
                    tab.setTextColor(Color.WHITE); // selected tab text color
                    loadSpecializations(dept.getId());
                });

                categoryTabs.addView(tab);
            }

            // Highlight the first tab if any
            if (categoryTabs.getChildCount() > 0) {
                ((TextView) categoryTabs.getChildAt(position)).setBackgroundResource(R.drawable.bg_tab_selected);
            }
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }
}



