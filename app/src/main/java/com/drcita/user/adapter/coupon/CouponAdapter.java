package com.drcita.user.adapter.coupon;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.Activity.CouponDetailsActivity;
import com.drcita.user.R;
import com.drcita.user.models.coupns.CouponModel;

import java.util.List;

public class CouponAdapter extends RecyclerView.Adapter<CouponAdapter.CouponViewHolder> {
    private Context context;
    private List<CouponModel> couponList;

    public CouponAdapter(Context context, List<CouponModel> couponList) {
        this.context = context;
        this.couponList = couponList;
    }

    @NonNull
    @Override
    public CouponViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.coupon_item, parent, false);
        return new CouponViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CouponViewHolder holder, int position) {
        CouponModel model = couponList.get(position);
        holder.tvCode.setText(model.getCouponcode());

        Glide.with(context)
                .load(model.getImage())
                .into(holder.ivCouponImage);
        holder.tvCode.setOnClickListener(v -> {
            ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("Coupon", model.getCouponcode());
            clipboard.setPrimaryClip(clip);
            Toast.makeText(context, "Copied: " + model.getCouponcode(), Toast.LENGTH_SHORT).show();
        });
        holder.codeContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CouponDetailsActivity.class);
                intent.putExtra("couponId", String.valueOf(model.getId()));
                intent.putExtra("couponCode", model.getCouponcode());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                intent.setFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS);
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.setFlags(Intent.FLAG_ACTIVITY_PREVIOUS_IS_TOP);
                context.startActivity(intent);

            }
        });
    }

    @Override
    public int getItemCount() {
        return couponList.size();
    }

    static class CouponViewHolder extends RecyclerView.ViewHolder {
        TextView tvCode;
        ImageView ivCopy, ivCouponImage;
        LinearLayout codeContainer;

        public CouponViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCouponCode);
            ivCopy = itemView.findViewById(R.id.ivCopy);
            ivCouponImage = itemView.findViewById(R.id.ivCouponBg);
            codeContainer=itemView.findViewById(R.id.codeContainer);
        }
    }
}

