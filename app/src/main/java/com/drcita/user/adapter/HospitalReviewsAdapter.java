package com.drcita.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.HospitalReviewActivity;
import com.drcita.user.R;
import com.drcita.user.models.hospitalreviews.DataItem;

import java.util.ArrayList;
import java.util.List;

public class HospitalReviewsAdapter extends RecyclerView.Adapter<HospitalReviewsAdapter.Viewholder> {
    private HospitalReviewActivity context;
    private List<DataItem> dataItems = new ArrayList<DataItem>();
    private int providerId;
    private String hospitalName;


    public HospitalReviewsAdapter(HospitalReviewActivity hospitalReviewActivity, List<DataItem> hospitalsResponses) {
        this.context = hospitalReviewActivity;
        this.dataItems = hospitalsResponses;
    }

    @NonNull
    @Override
    public HospitalReviewsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.hospital_reviews_list, parent, false);
        return new HospitalReviewsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HospitalReviewsAdapter.Viewholder holder, int position) {
        DataItem list = dataItems.get(position);
        holder.customerName.setText(list.getUserName());
        holder.reviewDescription.setText(list.getComments());
        holder.hospitaldate.setText(""+list.getReviewDate());
        holder.customerRating.setText(""+list.getRating());
    }
    @Override
    public int getItemCount() {
        return dataItems.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView customerName,customerRating,reviewDescription,hospitaldate;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            customerName = itemView.findViewById(R.id.customerName);
            customerRating = itemView.findViewById(R.id.customerRating);
            reviewDescription = itemView.findViewById(R.id.reviewDescription);
            hospitaldate = itemView.findViewById(R.id.hospitaldate);
        }
    }
}

