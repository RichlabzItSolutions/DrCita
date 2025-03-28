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

import com.drcita.user.BookanAppotimentActivity;
import com.drcita.user.DoctorsListActivity;
import com.drcita.user.R;
import com.drcita.user.common.Constants;
import com.drcita.user.models.doctors.DataItem;
import com.google.android.material.button.MaterialButton;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

public class DoctorsListAdapter extends RecyclerView.Adapter<DoctorsListAdapter.Viewholder> {
    private DoctorsListActivity context;
    private List<DataItem> dataItems = new ArrayList<DataItem>();
    String location;
    private String type="0";
    private String currencytype;


    public DoctorsListAdapter(DoctorsListActivity doctorsListActivity, List<DataItem> doctorsListResponse, String location) {
        this.context = doctorsListActivity;
        this.dataItems = doctorsListResponse;
        this.location = location;

    }

    @NonNull
    @Override
    public DoctorsListAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.doctors_list, parent, false);
        return new DoctorsListAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DoctorsListAdapter.Viewholder holder, int position) {

        DataItem list = dataItems.get(position);
        String consulatationcharge;
        consulatationcharge = list.getConsultationCharges();
        holder.doctorName.setText(list.getFirstName() + " " + list.getLastName());
        holder.specialization.setText(""+list.getSpecialisationName());
        holder.doctorExperience.setText(list.getExperience() + " "+"Yrs of overall exp");

        // holder.location.setText(location);
         holder.location.setText(""+list.getRegion());
      //  holder.consultationfee.setText("SLSH" +" "+ list.getConsultationCharges());
        if (!list.getPicture().isEmpty()) {
            Picasso.with(context).load(list.getPicture())
                    .into(holder.doctorsprofilephoto);
        }
        if (list.getAvailableToday().equals("Yes")) {
            holder.availbility.setText(R.string.availabletoday);
        } else {
            holder.availbility.setText("Not Available");
        }
        if (consulatationcharge.equals("0")){
            holder.consultationchargefree.setVisibility(View.VISIBLE);
            holder.consultationfee.setVisibility(View.GONE);
            holder.consultationchargefree.setText(R.string.free);

        }else {
            holder.consultationchargefree.setVisibility(View.GONE);
            holder.consultationfee.setVisibility(View.VISIBLE);
            SharedPreferences sp =context.getSharedPreferences(Constants.PREFERENCE_NAME, 0);
            currencytype = sp.getString(Constants.Currancy_Type, currencytype);

            if(currencytype!=null) {
                if (currencytype.equals("1")) {
                    holder.consultationfee.setText("₹ " + list.getConsultationCharges());
                } else {
                    holder.consultationfee.setText("₹ " + list.getConsultationCharges());
                }
            }


        }
        // holder.hospitallogo.setImageDrawable();
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView doctorName, specialization, doctorExperience, availbility, location, consultationfee,consultationchargefree;
        ImageView doctorsprofilephoto;
        MaterialButton bookanappotimentBtn;
        LinearLayout consulationLL;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            doctorsprofilephoto = itemView.findViewById(R.id.doctorsprofilephoto);
            doctorName = itemView.findViewById(R.id.doctorName);
            specialization = itemView.findViewById(R.id.specialization);
            doctorExperience = itemView.findViewById(R.id.doctorExperience);
            availbility = itemView.findViewById(R.id.availbility);
            location = itemView.findViewById(R.id.location);
            consultationfee = itemView.findViewById(R.id.consultationfee);
            doctorsprofilephoto = itemView.findViewById(R.id.doctorsprofilephoto);
            bookanappotimentBtn = itemView.findViewById(R.id.bookanappotimentBtn);
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
