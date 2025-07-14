package com.drcita.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.Activity.SpecializationActivity;
import com.drcita.user.CategoriesListActivity;
import com.drcita.user.DashBoardActivity;
import com.drcita.user.HospitalsListActivity;
import com.drcita.user.R;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.CategoryItemBinding;
import com.drcita.user.models.dashboard.hospital.Hospital;
import com.drcita.user.models.dashboard.specilization.Specialization;
import com.drcita.user.models.home.Providers;
import com.drcita.user.models.specalities.DataItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class CaregoryAdapter extends RecyclerView.Adapter<CaregoryAdapter.ViewHolder> {
    private Context context;
    private List<Object> items;

    public CaregoryAdapter(Context context, List<Object> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Object item = items.get(position);

        String name = "";
        String imageUrl = "";

        if (item instanceof Specialization) {
            Specialization s = (Specialization) item;
            name = s.getSpecialityName();
            imageUrl = s.getImage();
            // Click to open HospitalsListActivity with specialization
            holder.binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(context, SpecializationActivity.class);
                intent.putExtra("hospitalId", 0);
                intent.putExtra("specialization", s.getId());

                intent.putExtra("position",position);
                context.startActivity(intent);
                context.startActivity(intent);
            });

        } else if (item instanceof Providers) {
            Providers p = (Providers) item;
            name = p.getProviderName();
            imageUrl = p.getImage();
            // Click to open HospitalsListActivity with hospital
            holder.binding.getRoot().setOnClickListener(view -> {
                Intent intent = new Intent(context, HospitalsListActivity.class);
                intent.putExtra("hospitalId", p.getId());
                intent.putExtra("specialization", 0);
                context.startActivity(intent);
            });
        }

        holder.binding.categoryName.setText(name);
        if (imageUrl != null && !imageUrl.isEmpty()) {
            Picasso.with(context)
                    .load(imageUrl)
                    .error(R.drawable.neurology)
                    .into(holder.binding.catIV);
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CategoryItemBinding binding;

        public ViewHolder(@NonNull CategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
