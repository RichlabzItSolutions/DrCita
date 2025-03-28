package com.drcita.user.adapter;

import android.app.ProgressDialog;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.BookanAppotimentActivity;
import com.drcita.user.R;
import com.drcita.user.ScansListActivity;
import com.drcita.user.models.scans.DataItems;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;


public class ScansListAdapter extends RecyclerView.Adapter<ScansListAdapter.Viewholder> {
    private ScansListActivity context;
    private List<DataItems> dataItems = new ArrayList<DataItems>();
    private String userId;
    private ProgressDialog progress;
    private String type;

    public ScansListAdapter(ScansListActivity scansListActivity, List<DataItems> responses, String t) {
        this.context = scansListActivity;
        this.dataItems = responses;
        this.type=t;
    }

    @NonNull
    @Override
    public ScansListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.sacns_list_item, parent, false);
        return new ScansListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScansListAdapter.Viewholder holder, int position) {

        DataItems list = dataItems.get(position);
        holder.doctorName.setText(list.getScanName());
        holder.specialization.setText(""+list.getHospitalName());

        // holder.location.setText(location);
        holder.location.setText(""+list.getRegion());
        if (!list.getPicture().isEmpty()) {
            Picasso.with(context).load(list.getPicture())
                    .into(holder.doctorsprofilephoto);
        }
        if (list.getConsultationCharges().equals("0")){
            holder.consultationfee.setText("Free");
        }
        else {
            holder.consultationfee.setText("₹ " +  list.getConsultationCharges());
        }
        if (list.getAvailableToday().equals("Yes")) {
            holder.availbility.setText(R.string.availabletoday);
        } else {
            holder.availbility.setText("Not Available");
        }
        // holder.hospitallogo.setImageDrawable();
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView doctorName, specialization,ratingcount, availbility, location, consultationfee,scanstatus,scanstatusTV,deletescan;
        ImageView doctorsprofilephoto;
        RatingBar rating;
        MaterialButton bookanappotimentBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            doctorsprofilephoto = itemView.findViewById(R.id.doctorsprofilephoto);
            doctorName = itemView.findViewById(R.id.doctorName);
            specialization = itemView.findViewById(R.id.specialization);
            ratingcount = itemView.findViewById(R.id.ratingcount);
            availbility = itemView.findViewById(R.id.availbility);
            location = itemView.findViewById(R.id.location);
            consultationfee = itemView.findViewById(R.id.consultationfee);
            doctorsprofilephoto = itemView.findViewById(R.id.doctorsprofilephoto);
            rating = itemView.findViewById(R.id.rating);
            bookanappotimentBtn = itemView.findViewById(R.id.bookanappotimentBtn);

            bookanappotimentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataItems list = dataItems.get(getAdapterPosition());
                    Intent intent = new Intent(context, BookanAppotimentActivity.class);
                    intent.putExtra("doctorData1", Parcels.wrap(list));
                    intent.putExtra("listFrom",true);
                    intent.putExtra("doctorId", list.getScanId());
                    intent.putExtra("type",type);
                    context.startActivity(intent);
                }
            });
        }

    }


    private void showLoadingDialog() {
        if (progress == null) {
            progress = new ProgressDialog(context);
            progress.setTitle(context.getString(R.string.loading_title));
            progress.setMessage(context.getString(R.string.loading_message));
        }
        progress.show();
        progress.setCancelable(false);
    }

    private void dismissLoadingDialog() {
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }
    }
}
