package com.drcita.user.Activity;

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
import com.drcita.user.adapter.coupon.CouponAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityCouponsBinding;
import com.drcita.user.models.coupns.CouponModel;
import com.drcita.user.models.coupns.CouponRequest;
import com.drcita.user.models.coupns.CouponResponse;
import com.drcita.user.retrofit.ApiClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CoupnsActivity extends LanguageBaseActivity {

    private ActivityCouponsBinding searchbinding;
    private CouponAdapter couponAdapter;
    private LinearLayout categoryTabs;
    private List<CouponModel> couponList = new ArrayList<>();
    private int selectedFilterType = 3; // default to "All"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        searchbinding = DataBindingUtil.setContentView(this, R.layout.activity_coupons);
        searchbinding.backHospitalsList.setOnClickListener(view -> finish());
        categoryTabs = searchbinding.categoryTabs;
        setupStaticTabs();
        setupRecyclerView();
        fetchCouponsFromApi(selectedFilterType);

    }

    private void setupStaticTabs() {
        categoryTabs.removeAllViews();

        addStaticTab("All", 3);
        addStaticTab("Online", 1);
        addStaticTab("Offline", 2);

        if (categoryTabs.getChildCount() > 0) {
            ((TextView) categoryTabs.getChildAt(0)).setBackgroundResource(R.drawable.bg_tab_selected);
            ((TextView) categoryTabs.getChildAt(0)).setTextColor(Color.WHITE);
        }
    }

    private void addStaticTab(String label, int filterType) {
        TextView tab = new TextView(this);
        tab.setText(label);
        tab.setTextSize(14f);
        tab.setPadding(24, 12, 24, 12);
        tab.setTextColor(Color.BLACK);
        tab.setBackgroundResource(R.drawable.bg_tab_unselected);
        tab.setGravity(Gravity.CENTER);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.setMargins(0, 0, 24, 0);
        tab.setLayoutParams(params);

        tab.setOnClickListener(v -> {
            resetTabStyles();
            tab.setBackgroundResource(R.drawable.bg_tab_selected);
            tab.setTextColor(Color.WHITE);
            selectedFilterType = filterType;
            fetchCouponsFromApi(selectedFilterType);
        });

        categoryTabs.addView(tab);
    }

    private void resetTabStyles() {
        for (int i = 0; i < categoryTabs.getChildCount(); i++) {
            TextView tab = (TextView) categoryTabs.getChildAt(i);
            tab.setBackgroundResource(R.drawable.bg_tab_unselected);
            tab.setTextColor(Color.BLACK);
        }
    }

    private void setupRecyclerView() {
        searchbinding.search.setLayoutManager(new GridLayoutManager(this, 1));
        couponAdapter = new CouponAdapter(this, couponList);
        searchbinding.search.setAdapter(couponAdapter);
    }
    private void fetchCouponsFromApi(int mode) {
        try {
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                CouponRequest request = new CouponRequest();
                request.setAppointmentMode(mode);
                ApiClient.getRestAPI().getCoupons(request).enqueue(new Callback<CouponResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CouponResponse> call, @NonNull Response<CouponResponse> response) {
                        dismissLoadingDialog();
                        if (response.body() != null && response.body().getData() != null && response.body().isSuccess()) {
                            couponList.clear();
                            couponList.addAll(response.body().getData());
                            couponAdapter.notifyDataSetChanged();
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<CouponResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();
                        dismissLoadingDialog();
                    }
                });
            } else {
                Constants.InternetSettings(this);
            }
        } catch (Exception ignored) {
        }
    }
}
