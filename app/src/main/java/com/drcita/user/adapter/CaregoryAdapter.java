package com.drcita.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.CategoriesListActivity;
import com.drcita.user.DashBoardActivity;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.CategoryItemBinding;
import com.drcita.user.models.specalities.DataItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CaregoryAdapter extends RecyclerView.Adapter<CaregoryAdapter.ViewHolder> {
    private DashBoardActivity dashBoardActivity;
    private ArrayList<DataItem> dataItems;

    public CaregoryAdapter(DashBoardActivity dashBoardActivity, ArrayList<DataItem> dataItems) {
        this.dashBoardActivity = dashBoardActivity;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(CategoryItemBinding.inflate(LayoutInflater.from(parent.getContext())));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        com.drcita.user.models.specalities.DataItem list = dataItems.get(position);
        holder.itemView.categoryName.setText(dataItems.get(position).getName());
        if (dataItems.get(position).getPicture() != null) {
            Picasso.with(holder.itemView.getRoot().getContext()).load(dataItems.get(position).getPicture()).into(holder.itemView.catIV);
        }
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private com.drcita.user.databinding.CategoryItemBinding itemView;

        public ViewHolder(@NonNull CategoryItemBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;
            itemView.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    SharedPreferences sp = itemView.getRoot().getContext().getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
                    com.drcita.user.models.specalities.DataItem list = dataItems.get(getAdapterPosition());

                    Intent intent = new Intent(itemView.getRoot().getContext(), CategoriesListActivity.class);
                    intent.putExtra("id",list.getId());
                    itemView.getRoot().getContext().startActivity(intent);
                }
            });
        }
    }
}
