package com.drcita.user.adapter;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.BookanAppotimentActivity;
import com.drcita.user.CategoriesListActivity;
import com.drcita.user.R;
import com.drcita.user.models.doctors.DataItem;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class CategoriesListAdapter extends RecyclerView.Adapter<CategoriesListAdapter.Viewholder> {
    private CategoriesListActivity context;
    private List<com.drcita.user.models.doctors.DataItem> dataItems = new ArrayList<com.drcita.user.models.doctors.DataItem>();
    String location;
    private String type="0";


    public CategoriesListAdapter(CategoriesListActivity categoriesListActivity, List<com.drcita.user.models.doctors.DataItem> doctorsListResponse) {
        this.context = categoriesListActivity;
        this.dataItems = doctorsListResponse;
        this.location = location;

    }

    @NonNull
    @Override
    public CategoriesListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.categorieslist_irem, parent, false);
        return new CategoriesListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesListAdapter.Viewholder holder, int position) {

        com.drcita.user.models.doctors.DataItem list = dataItems.get(position);
        String consulatationcharge;
        consulatationcharge = list.getConsultationCharges();
        holder.doctorName.setText(list.getFirstName() + " " + list.getLastName());
        holder.specialization.setText(""+list.getSpecialisationName());
        holder.doctorExperience.setText(list.getExperience() + " "+" Yrs of exp");
        holder.ratingcount.setText("" + list.getRatedCount());

        holder.hospitalName.setText(list.getHospitalName());
        // holder.location.setText(location);
        holder.location.setText(""+list.getRegion());
        if (consulatationcharge.equals("0")){
            holder.consultationchargefree.setVisibility(View.VISIBLE);
            holder.consultationfee.setVisibility(View.GONE);
            holder.consultationchargefree.setText(R.string.free);

        }else {
            holder.consultationchargefree.setVisibility(View.GONE);
            holder.consultationfee.setVisibility(View.VISIBLE);
            holder.consultationfee.setText("₹ " + list.getConsultationCharges());
        }




        if (!list.getPicture().isEmpty()) {
            Picasso.with(context).load(list.getPicture())
                    .into(holder.doctorsprofilephoto);
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
        TextView doctorName, specialization, doctorSpecial, doctorExperience, ratingcount, availbility, location, consultationfee,hospitalName,consultationchargefree;
        ImageView doctorsprofilephoto;
        ImageView rating;
        LinearLayout consulationLL;

        MaterialButton bookanappotimentBtn;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            doctorsprofilephoto = itemView.findViewById(R.id.doctorsprofilephoto);
            doctorName = itemView.findViewById(R.id.doctorName);
            specialization = itemView.findViewById(R.id.specialization);
            doctorExperience = itemView.findViewById(R.id.doctorExperience);
            ratingcount = itemView.findViewById(R.id.ratingcount);
            availbility = itemView.findViewById(R.id.availbility);
            location = itemView.findViewById(R.id.location);
            consultationfee = itemView.findViewById(R.id.consultationfee);
            doctorsprofilephoto = itemView.findViewById(R.id.doctorsprofilephoto);
            bookanappotimentBtn = itemView.findViewById(R.id.bookanappotimentBtn);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            rating = itemView.findViewById(R.id.rating);
            consultationchargefree = itemView.findViewById(R.id.consultationchargefree);
            consulationLL = itemView.findViewById(R.id.consulationLL);
            bookanappotimentBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DataItem list = dataItems.get(getAdapterPosition());
                    Intent intent = new Intent(context, BookanAppotimentActivity.class);
                    intent.putExtra("doctorId", list.getDoctorId());
                    intent.putExtra("doctorData", Parcels.wrap(list));
                    intent.putExtra("type",type);
                    context.startActivity(intent);
                }
            });
        }
    }
}
