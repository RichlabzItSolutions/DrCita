package com.drcita.user.adapter.search;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;
import com.drcita.user.models.fliter.FilterCategoryModel;


import java.util.List;

public class CategoryTabAdapter extends RecyclerView.Adapter<CategoryTabAdapter.CategoryViewHolder> {

    private final List<FilterCategoryModel> categoryList;
    private int selectedPosition = 0;
    private Context context;
    private final OnCategorySelectedListener listener;

    public interface OnCategorySelectedListener {
        void onCategorySelected(FilterCategoryModel selectedCategory);
    }

    public CategoryTabAdapter(Context context, List<FilterCategoryModel> categoryList, OnCategorySelectedListener listener) {
        this.categoryList = categoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter_category_tab, parent, false);
        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, @SuppressLint("RecyclerView") int position) {
        FilterCategoryModel category = categoryList.get(position);
        holder.txtCategory.setText(category.getCategoryName());

        boolean isSelected = selectedPosition == position;
//        holder.cardView.setBackgroundColor(
//                isSelected ? Color.parseColor("#F2F8F2") : Color.parseColor("#FFFFFF")
//        );
        holder.txtCategory.setTextColor(
                isSelected ? Color.parseColor("#62AA48") : Color.parseColor("#333333")
        );



        // Show selected count if any
        int count = category.getSelectedCount();
        if (count > 0) {
            holder.txtBadge.setVisibility(View.VISIBLE);
            holder.txtBadge.setText(String.valueOf(count));
        } else {
            holder.txtBadge.setVisibility(View.GONE);
        }

        holder.itemView.setOnClickListener(v -> {
            if (selectedPosition != position) {
                int previous = selectedPosition;
                selectedPosition = position;
                notifyItemChanged(previous);
                notifyItemChanged(position);
                listener.onCategorySelected(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    static class CategoryViewHolder extends RecyclerView.ViewHolder {
        TextView txtCategory, txtBadge;
        RelativeLayout cardView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategoryName);
            txtBadge = itemView.findViewById(R.id.txtCountBadge);
            cardView = itemView.findViewById(R.id.cardCategoryTab);
        }
    }
}
