package com.drcita.user.adapter.medical;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.DoctorsListActivity;
import com.drcita.user.HospitalReviewActivity;
import com.drcita.user.HospitalsListActivity;
import com.drcita.user.R;
import com.drcita.user.WriteReview;
import com.drcita.user.common.Constants;
import com.drcita.user.models.newProviderlist.NewProviderList;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder> {
    private List<File> images;
    private OnRemoveListener onRemoveListener;

    public interface OnRemoveListener {
        void onRemove(File file);
    }

    public ImageAdapter(List<File> images, OnRemoveListener onRemoveListener) {
        this.images = images;
        this.onRemoveListener = onRemoveListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPreview, btnRemove;
        public ViewHolder(View view) {
            super(view);
            imgPreview = view.findViewById(R.id.imgPreview);
            btnRemove = view.findViewById(R.id.btnRemove);
        }
    }

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(holder.itemView.getContext()).load(images.get(position)).into(holder.imgPreview);
        holder.btnRemove.setOnClickListener(v -> onRemoveListener.onRemove(images.get(position)));
    }

    @Override
    public int getItemCount() {
        return images.size();
    }
}
