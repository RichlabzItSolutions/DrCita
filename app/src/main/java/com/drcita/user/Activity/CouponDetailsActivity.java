package com.drcita.user.Activity;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import com.bumptech.glide.Glide;
import com.drcita.user.LanguageBaseActivity;
import com.drcita.user.R;
import com.drcita.user.common.Constants;
import com.drcita.user.models.coupns.CouponDetailRequest;
import com.drcita.user.models.coupns.CouponDetailsResponse;
import com.drcita.user.models.coupns.CouponModel;
import com.drcita.user.retrofit.ApiClient;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CouponDetailsActivity extends LanguageBaseActivity {

    private TextView tvCouponCode, tvCouponTitle,
            tvCouponDescription,tv_Title,tvTermsTitle;
    private ImageView ivCouponImage,back_icon;
    private LinearLayout llTerms;
    private int couponId;
    private String couponCode;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupon_details);

        // Initialize views
        tvCouponCode = findViewById(R.id.tvCouponCode);
        tvCouponTitle = findViewById(R.id.tvCouponTitle);
        tvCouponDescription = findViewById(R.id.tvCouponSubtitle);
        ivCouponImage = findViewById(R.id.ivCouponImage);
        tv_Title = findViewById(R.id.tv_Title);
        tvTermsTitle= findViewById(R.id.tvTermsTitle);
        llTerms = findViewById(R.id.layoutTerms);
        back_icon=findViewById(R.id.back_hospitalsList);
        back_icon.setOnClickListener(view -> finish());
        tvTermsTitle.setPaintFlags(tvTermsTitle.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        if(getIntent().getStringExtra("couponId")!=null) {
            // Replace this with dynamic couponId from intent
         couponCode=getIntent().getStringExtra("couponCode");
         tv_Title.setText(couponCode);
            fetchCouponDetails(Integer.parseInt(getIntent().getStringExtra("couponId")));

        }


    }

    private void fetchCouponDetails(int couponId) {
        try{
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                CouponDetailRequest request = new CouponDetailRequest(String.valueOf(couponId));


                ApiClient.getRestAPI().getCouponDetails(request).enqueue(new Callback<CouponDetailsResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<CouponDetailsResponse> call, @NonNull Response<CouponDetailsResponse> response) {
                        if (response.isSuccessful() && response.body() != null && response.body().isSuccess()) {
                            CouponModel coupon = response.body().getData().getCoupon();
                            List<String> terms = response.body().getData().getTermsAndConditions();

                            tvCouponCode.setText(coupon.getCouponcode());
                           tvCouponTitle.setText("GET ₹" + coupon.getCouponWorth() + " OFF");
                            tvCouponDescription.setText(coupon.getDescription());

                            Glide.with(CouponDetailsActivity.this)
                                    .load(coupon.getImage())
                                    .into(ivCouponImage);

                            llTerms.removeAllViews();
                            for (String term : terms) {
                                TextView tv = new TextView(CouponDetailsActivity.this);
                                tv.setText("• " + term);
                                tv.setTextSize(14f);
                                tv.setTextColor(Color.DKGRAY);
                                tv.setPadding(0, 8, 0, 0);
                                llTerms.addView(tv);
                            }
                            dismissLoadingDialog();

                        } else {
                            dismissLoadingDialog();
                            Toast.makeText(CouponDetailsActivity.this, "Coupon not found.", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<CouponDetailsResponse> call, @NonNull Throwable t) {
                        Toast.makeText(CouponDetailsActivity.this, "Failed to load coupon details", Toast.LENGTH_SHORT).show();
                    }
                });
            }


        }catch (Exception ex)
        {
            ex.getMessage();
        }


    }
}
