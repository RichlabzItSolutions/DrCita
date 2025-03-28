package com.drcita.user.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.DashBoardActivity;
import com.drcita.user.NotificationViewImage;
import com.drcita.user.databinding.NotificationWithoutImageBinding;
import com.drcita.user.databinding.NotificationlayoutBinding;
import com.drcita.user.models.notifications.DataItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private DashBoardActivity dashBoardActivity;
    private ArrayList<DataItem> dataItems;

    public NotificationAdapter(DashBoardActivity dashBoardActivity, ArrayList<com.drcita.user.models.notifications.DataItem> dataItems) {
        this.dashBoardActivity = dashBoardActivity;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == 1) {
            return new ViewHolderWithIV(NotificationlayoutBinding.inflate(LayoutInflater.from(parent.getContext())));
        } else {
            return new ViewHolderWithOutIV(NotificationWithoutImageBinding.inflate(LayoutInflater.from(parent.getContext())));
        }
    }
    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int itemViewType = getItemViewType(position);
        if (itemViewType == 1) {
            com.drcita.user.models.notifications.DataItem list = dataItems.get(position);

            ViewHolderWithIV viewHolderWithIV = (ViewHolderWithIV) holder;
            viewHolderWithIV.itemView.dateitem.setText("" + list.getNotficiationDate());
            viewHolderWithIV.itemView.headerTitle.setText(list.getTitle());
            viewHolderWithIV.itemView.description.setText(list.getDescription());
            viewHolderWithIV.itemView.name.setText(list.getName());
            if (!list.getImage().isEmpty()) {
                Picasso.with(this.dashBoardActivity).load(list.getImage())
                        .into(viewHolderWithIV.itemView.imagenotification);
            }


        } else {
            com.drcita.user.models.notifications.DataItem list = dataItems.get(position);
            ViewHolderWithOutIV viewHolderWithOutIV = (ViewHolderWithOutIV) holder;
            viewHolderWithOutIV.itemView.datenotification.setText(list.getNotficiationDate());
            viewHolderWithOutIV.itemView.titlenotifcation.setText(list.getTitle());
            viewHolderWithOutIV.itemView.descriptionnotification.setText(list.getDescription());

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (dataItems.get(position).getImage() != null && !dataItems.get(position).getImage().isEmpty()) {
            return 1;
        } else {
            return 2;
        }
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    class ViewHolderWithIV extends RecyclerView.ViewHolder {
        private NotificationlayoutBinding itemView;


        public ViewHolderWithIV(@NonNull NotificationlayoutBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;

            itemView.imagenotification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(dashBoardActivity,NotificationViewImage.class);
                    intent.putExtra("image",dataItems.get(getAdapterPosition()).getImage());
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    dashBoardActivity.startActivity(intent);
                }
            });
        }
    }

    class ViewHolderWithOutIV extends RecyclerView.ViewHolder {
        private NotificationWithoutImageBinding itemView;

        public ViewHolderWithOutIV(@NonNull NotificationWithoutImageBinding itemView) {
            super(itemView.getRoot());
            this.itemView = itemView;



        }
    }
}
