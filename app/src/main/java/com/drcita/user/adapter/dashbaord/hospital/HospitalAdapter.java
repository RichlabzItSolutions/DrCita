package com.drcita.user.adapter.dashbaord.hospital;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.HospitalsListActivity;
import com.drcita.user.R;
import com.drcita.user.models.dashboard.hospital.Hospital;
import com.drcita.user.models.dashboard.specilization.Specialization;
import com.drcita.user.models.home.Providers;
import com.drcita.user.models.newProviderlist.NewProviderList;

import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.HospitalViewHolder> {

    private final List<Providers> hospitals;
    private int itemWidth;
    private Context context;
    public HospitalAdapter(Context context, List<Providers> hospitals) {
        this.hospitals = hospitals;
        this.context=context;

        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        itemWidth = screenWidth / 5;  // force 5 items fit

    }

    @NonNull
    @Override
    public HospitalViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hospital_logo, parent, false);
        view.getLayoutParams().width = itemWidth;
        return new HospitalViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalViewHolder holder, int position) {

        Providers providers = hospitals.get(position);
        holder.tv_providersname.setText(providers.getProviderName());
        Glide.with(context)
                .load(providers.getImage()) // URL from JSON
                .error(R.drawable.neurology)      // optional
                .into(holder.imgHospitalLogo);

         holder.ll_hospital.setOnClickListener(view -> {
         Providers dataItem = hospitals.get(position);
            Intent intent = new Intent(context, HospitalsListActivity.class);
            intent.putExtra("hospitalId", dataItem.getId());
            intent.putExtra("specialization",0);
            context.startActivity(intent);


        });

    }

    @Override
    public int getItemCount() {
        return hospitals.size();
    }

    static class HospitalViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgHospitalLogo;
        private TextView tv_providersname;
        LinearLayout ll_hospital;

        HospitalViewHolder(View itemView) {
            super(itemView);
            imgHospitalLogo = itemView.findViewById(R.id.imgHospitalLogo);
            tv_providersname=itemView.findViewById(R.id.tvSpecialization);
            ll_hospital=itemView.findViewById(R.id.ll_hospital);


        }

        void bind(Hospital hospital) {
            imgHospitalLogo.setImageResource(hospital.getLogoResId());
        }
    }
}
