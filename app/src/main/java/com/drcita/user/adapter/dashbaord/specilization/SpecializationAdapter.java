package com.drcita.user.adapter.dashbaord.specilization;


import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.R;
import com.drcita.user.models.dashboard.specilization.Specialization;

import java.util.List;

public class SpecializationAdapter extends RecyclerView.Adapter<SpecializationAdapter.SpecializationViewHolder> {

    private List<Specialization> specializationList;

    private int itemWidth;
    private Context context;

    public SpecializationAdapter(Context context, List<Specialization> list) {
        this.specializationList = list;
       this. context=context;
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int screenWidth = displayMetrics.widthPixels;
        itemWidth = screenWidth / 5;  // force 5 items fit

    }

    @NonNull
    @Override
    public SpecializationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_specialization, parent, false);
        view.getLayoutParams().width = itemWidth;
        return new SpecializationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SpecializationViewHolder holder, int position) {
        Specialization specialization = specializationList.get(position);
        holder.tvSpecialization.setText(specialization.getSpecialityName());
        Glide.with(context)
                .load(specialization.getImage()) // URL from JSON
                .error(R.drawable.neurology)      // optional
                .into( holder.imgSpecialization);


    }

    @Override
    public int getItemCount() {
        return specializationList.size();
    }

    public static class SpecializationViewHolder extends RecyclerView.ViewHolder {
        ImageView imgSpecialization;
        TextView tvSpecialization;

        public SpecializationViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSpecialization = itemView.findViewById(R.id.imgSpecialization);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
        }
    }
}
