package com.drcita.user.filter;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.drcita.user.R;
import com.drcita.user.adapter.search.CategoryTabAdapter;
import com.drcita.user.adapter.search.FilterOptionAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.models.dashboard.specilization.Specialization;
import com.drcita.user.models.departments.SpecializationRequest;
import com.drcita.user.models.departments.SpecializationResponse;
import com.drcita.user.models.fliter.FilterCategoryModel;
import com.drcita.user.models.fliter.FilterOption;
import com.drcita.user.retrofit.ApiClient;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DoctorFilterBottomSheet extends AppCompatActivity {

    private RecyclerView rvCategoryTabs, rvFilterOptions;
    private TextView txtTitle;
    private ImageView ivClose;
    private EditText edtSearch;
    private CategoryTabAdapter categoryTabAdapter;
    private FilterOptionAdapter optionAdapter;

    private final List<FilterCategoryModel> categoryList = new ArrayList<>();
    private final Map<String, List<FilterOption>> optionMap = new HashMap<>();
    private List<Specialization> specializationList = new ArrayList<>();
    private FilterCategoryModel currentCategory;

    private SharedPreferences sharedPreferences;
    AppCompatButton btnClear,btnApply;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_filter_bottom_sheet);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.BOTTOM);

        rvCategoryTabs = findViewById(R.id.rvCategoryTabs);
        rvFilterOptions = findViewById(R.id.rvOptions);
        txtTitle = findViewById(R.id.tvFilterTitle);
        ivClose = findViewById(R.id.ivClose);
        edtSearch = findViewById(R.id.etSearch);
        btnClear = findViewById(R.id.btnClear);
        btnApply=findViewById(R.id.btnApply);
        btnApply.setOnClickListener(view -> applyFilters());


        sharedPreferences = getSharedPreferences("filter_prefs", Context.MODE_PRIVATE);
       // clearAllSelections(); // ✅ Clear selections before loading filters
        ivClose.setOnClickListener(v -> finish());

        setupRecyclerViews();
        loadFilterCategories();

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (optionAdapter != null) {
                    optionAdapter.filter(s.toString());
                }
            }
        });
        btnClear.setOnClickListener(v -> {
            clearAllSelections(); // clear from SharedPreferences and memory
            loadFilterOptions(currentCategory); // reload current category
            categoryTabAdapter.notifyDataSetChanged(); // refresh tab counts
        });

    }

    private void setupRecyclerViews() {
        categoryTabAdapter = new CategoryTabAdapter(this, categoryList, selectedCategory -> {
            currentCategory = selectedCategory;
            txtTitle.setText("Filter By : " + selectedCategory.getCategoryName());
            loadFilterOptions(selectedCategory);
        });

        rvCategoryTabs.setLayoutManager(new LinearLayoutManager(this));
        rvCategoryTabs.setAdapter(categoryTabAdapter);

        DividerItemDecoration divider = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(this, R.drawable.divider_line));
        rvCategoryTabs.addItemDecoration(divider);

        rvFilterOptions.setLayoutManager(new LinearLayoutManager(this));
    }

    private void loadFilterCategories() {
        categoryList.clear();
        categoryList.add(new FilterCategoryModel("Specialization", "specialization", true));
        categoryList.add(new FilterCategoryModel("Consultation", "consultation", false));
        categoryList.add(new FilterCategoryModel("Availability", "availability", false));
        categoryList.add(new FilterCategoryModel("Gender", "gender", false));
        categoryList.add(new FilterCategoryModel("Language", "language", false));
        categoryList.add(new FilterCategoryModel("Experience", "experience", false));
        categoryList.add(new FilterCategoryModel("State", "state", false));
        categoryList.add(new FilterCategoryModel("City", "city", false));
        categoryList.add(new FilterCategoryModel("Location", "location", false));
        categoryTabAdapter.notifyDataSetChanged();
        txtTitle.setText("Filter By : " + categoryList.get(0).getCategoryName());
        currentCategory = categoryList.get(0);
        loadFilterOptions(currentCategory);
    }

    private void loadFilterOptions(FilterCategoryModel category) {
        String key = category.getCategoryId();

        List<FilterOption> optionList = optionMap.containsKey(key) ? optionMap.get(key) : new ArrayList<>();

        if (optionList.isEmpty()) {
            optionList = new ArrayList<>();

            switch (key) {
                case "gender":
                    optionList.add(new FilterOption("1", "Male", false));
                    optionList.add(new FilterOption("2", "Female", false));
                    optionList.add(new FilterOption("3", "Other", false));
                    break;

                case "consultation":
                    optionList.add(new FilterOption("1", "Online", false));
                    optionList.add(new FilterOption("2", "In-Person", false));
                    optionList.add(new FilterOption("3", "Both", false));
                    break;

                case "availability":
                    optionList.add(new FilterOption("1", "Morning", true));
                    optionList.add(new FilterOption("2", "Afternoon", false));
                    optionList.add(new FilterOption("3", "Evening", false));
                    break;

                case "language":
                    optionList.add(new FilterOption("1", "English", true));
                    optionList.add(new FilterOption("2", "Telugu", false));
                    optionList.add(new FilterOption("3", "Hindi", false));
                    break;
                case "experience":
                    if (optionList.isEmpty()) {
                        optionList.add(new FilterOption("experience_range", "Experience Range", false));
                    }

                    break;
                case "specialization":
                    loadOptionsFromApi(category);
                    return;
                case "state":
                case "city":
                    return;
            }

            if (key.equals("consultation") || key.equals("availability") || key.equals("language")) {
                category.setSelectedCount(1);
                categoryTabAdapter.notifyDataSetChanged();
            }

            optionMap.put(key, optionList);
        }

        updateSelectedCount(category, optionList);

        optionAdapter = new FilterOptionAdapter(this, optionList, sharedPreferences,isSingleChoice(key), count -> {
            category.setSelectedCount(count);
            categoryTabAdapter.notifyDataSetChanged();
        }, key );

        rvFilterOptions.setAdapter(optionAdapter);
    }

    private void loadOptionsFromApi(FilterCategoryModel category) {
        String key = category.getCategoryId();
        if (!Constants.haveInternet(this)) return;

        SpecializationRequest request = new SpecializationRequest("");
        ApiClient.getRestAPI().getSpecoilizationByDepartment(request).enqueue(new Callback<SpecializationResponse>() {
            @Override
            public void onResponse(@NonNull Call<SpecializationResponse> call, @NonNull Response<SpecializationResponse> response) {
                if (response.body() != null && response.body().getData() != null) {
                    specializationList.clear();
                    specializationList.addAll(response.body().getData());

                    List<FilterOption> specializationOptions = new ArrayList<>();
                    for (Specialization spec : specializationList) {
                        specializationOptions.add(new FilterOption(
                                String.valueOf(spec.getId()),
                                spec.getSpecialityName(),
                                false
                        ));
                    }

                    optionMap.put(key, specializationOptions);
                    updateSelectedCount(category, specializationOptions);

                    optionAdapter = new FilterOptionAdapter(DoctorFilterBottomSheet.this, specializationOptions, sharedPreferences, isSingleChoice(key), count ->  {
                        category.setSelectedCount(count);
                        categoryTabAdapter.notifyDataSetChanged();
                    }, key );

                    rvFilterOptions.setAdapter(optionAdapter);
                    optionAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<SpecializationResponse> call, @NonNull Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateSelectedCount(FilterCategoryModel category, List<FilterOption> options) {
        int count = 0;
        for (FilterOption option : options) {
            if (option.isSelected()) count++;
        }
        category.setSelectedCount(count);
        categoryTabAdapter.notifyDataSetChanged();
    }

    private boolean isSingleChoice(String key) {
        return key.equals("gender") || key.equals("consultation") ;
    }

    private void showExperienceInputs() {
        // Handle experience input UI here


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.slide_out_bottom);
    }

    private void clearAllSelections() {
        // Clear from shared preferences
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();

        // Clear selection status in memory
        for (List<FilterOption> options : optionMap.values()) {
            for (FilterOption option : options) {
                option.setSelected(false);
                option.setMin("");
                option.setMax("");
            }
        }

        // Reset count in each category
        for (FilterCategoryModel category : categoryList) {
            category.setSelectedCount(0);
        }

        if (optionAdapter != null) {
            optionAdapter.notifyDataSetChanged();
        }
    }
//
//    private void applyFilters() {
//        Map<String, List<String>> selectedFilters = new HashMap<>();
//
//        for (Map.Entry<String, List<FilterOption>> entry : optionMap.entrySet()) {
//            String categoryId = entry.getKey();
//            List<String> selectedValues = new ArrayList<>();
//
//            for (FilterOption option : entry.getValue()) {
//                if (option.isSelected()) {
//                    selectedValues.add(option.getId()); // or option.getLabel() if you want labels
//                }
//            }
//
//            if (!selectedValues.isEmpty()) {
//                selectedFilters.put(categoryId, selectedValues);
//            }
//
//        }
//
//        // Log selected filters (for debugging)
//        for (Map.Entry<String, List<String>> entry : selectedFilters.entrySet()) {
//            Log.d("FILTER", entry.getKey() + ": " + entry.getValue());
//        }
//
//        // Send result back to calling activity
//        Intent resultIntent = new Intent();
//        resultIntent.putExtra("filters", new Gson().toJson(selectedFilters));
//        setResult(RESULT_OK, resultIntent); // Notify parent activity of success
//
//        finish(); // Close the filter screen
//    }

    private void applyFilters() {
        Map<String, List<String>> selectedFilters = new HashMap<>();

        for (Map.Entry<String, List<FilterOption>> entry : optionMap.entrySet()) {
            String categoryId = entry.getKey();
            List<String> selectedValues = new ArrayList<>();

            // Handle experience range separately
            if (categoryId.equals("experience")) {
                List<FilterOption> experienceOptions = entry.getValue();
                for (FilterOption option : experienceOptions) {
                    if ("experience_range".equalsIgnoreCase(option.getId())) {
                        String min = option.getMin();
                        String max = option.getMax();
                        if (!min.isEmpty() || !max.isEmpty()) {
                            selectedValues.add(min + "-" + max);
                        }
                    }
                }
            } else {
                // Normal selected checkboxes/radio buttons
                for (FilterOption option : entry.getValue()) {
                    if (option.isSelected()) {
                        selectedValues.add(option.getId()); // or option.getLabel()
                    }
                }
            }

            if (!selectedValues.isEmpty()) {
                selectedFilters.put(categoryId, selectedValues);
            }
        }

        // Debug log
        for (Map.Entry<String, List<String>> entry : selectedFilters.entrySet()) {
            Log.d("FILTER", entry.getKey() + ": " + entry.getValue());
        }

        // Pass back to calling activity
        Intent resultIntent = new Intent();
        resultIntent.putExtra("filters", new Gson().toJson(selectedFilters));
        setResult(RESULT_OK, resultIntent);
        finish();
    }

}
