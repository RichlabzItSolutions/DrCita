package com.drcita.user.adapter;

import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.DiagnosticsActivity;
import com.drcita.user.HospitalReviewActivity;
import com.drcita.user.R;
import com.drcita.user.ScansListActivity;
import com.drcita.user.WriteReview;
import com.drcita.user.common.Constants;
import com.drcita.user.models.hospitals.DataItem;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DiagnosticsAdapter extends RecyclerView.Adapter<DiagnosticsAdapter.Viewholder> {
    private DiagnosticsActivity context;
    private List<DataItem> dataItems = new ArrayList<DataItem>();
    private int providerId;
    private String hospitalName,rating,time,location,ratingcount,ratingStar,type;


    public DiagnosticsAdapter(DiagnosticsActivity diagnosticsActivity, List<DataItem> hospitalsResponses, String t) {
        this.context = diagnosticsActivity;
        this.dataItems = hospitalsResponses;
        this.type=t;
    }

    @NonNull
    @Override
    public DiagnosticsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // to inflate the layout for each item of recycler view.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.diagnostics_list, parent, false);
        return new DiagnosticsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiagnosticsAdapter.Viewholder holder, int position) {
        DataItem list = dataItems.get(position);
        providerId = list.getProviderId();
        hospitalName = list.getHospitalName();
        rating = ""+list.getRating();
        time = ""+list.getOpeningHours();
        location = ""+list.getRegion();
        ratingcount =""+list.getRating();


        holder.hospitalname.setText(list.getHospitalName());
        holder.ratingTV.setText(""+list.getRating());
        holder.time.setText(list.getOpeningHours());
        holder.location.setText(""+list.getRegion());
        holder.ratingcount.setText(""+list.getRatedCount());
        rating = ""+list.getRating();
        if (!list.getPicture().isEmpty()) {
            Picasso.with(context).load(list.getPicture())
                    .into(holder.hospitallogo);
        }
        SharedPreferences preferences = context.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
        preferences.edit().putString(Constants.PROVIDER_ID, String.valueOf(providerId)).apply();

        // holder.hospitallogo.setImageDrawable();


    }
    @Override
    public int getItemCount() {
        return dataItems.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        ImageView hospitallogo;
        ImageView rating;
        LinearLayout ratingedit;
        TextView hospitalname,ratingTV,ratingcount,time,location;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            rating = itemView.findViewById(R.id.rating);
            hospitalname = itemView.findViewById(R.id.hospitalname);
            ratingTV = itemView.findViewById(R.id.ratingTV);
            ratingcount = itemView.findViewById(R.id.ratingcount);
            time = itemView.findViewById(R.id.time);
            location = itemView.findViewById(R.id.location);
            ratingedit = itemView.findViewById(R.id.ratingedit);
            hospitallogo = itemView.findViewById(R.id.hospitallogo);

            ratingedit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, WriteReview.class);
                    intent.putExtra("providerId",providerId);
                    context.startActivity(intent);
                }
            });
            ratingcount.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataItem dataItem = dataItems.get(getAdapterPosition());
                    Intent intent = new Intent(context, HospitalReviewActivity.class);
                    intent.putExtra("dataItem", Parcels.wrap(dataItem));
                    context.startActivity(intent);
                }
            } );
            hospitallogo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataItem dataItem = dataItems.get(getAdapterPosition());
                    int providerId = dataItem.getProviderId();
                    Intent intent = new Intent(context, ScansListActivity.class);
                    intent.putExtra("providerID",providerId);
                    intent.putExtra("type",type);
                    context.startActivity(intent);
                }
            });
            hospitalname.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataItem dataItem = dataItems.get(getAdapterPosition());
                    int providerId = dataItem.getProviderId();
                    Intent intent = new Intent(context, ScansListActivity.class);
                    intent.putExtra("providerID",providerId);
                    intent.putExtra("type",type);
                    context.startActivity(intent);
                }
            });

        }
    }
}
