package com.drcita.user.adapter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.MedicalRecordsActivity;
import com.drcita.user.R;
import com.drcita.user.ViewMedicalRecordsActivity;
import com.drcita.user.models.medicalrecords.DataItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordsAdapter extends RecyclerView.Adapter<MedicalRecordsAdapter.Viewholder> {
    private MedicalRecordsActivity context;
    private List<com.drcita.user.models.medicalrecords.DataItem> responses = new ArrayList<com.drcita.user.models.medicalrecords.DataItem>();
    private ProgressDialog progress;
    private String[] month = {"JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};


    public MedicalRecordsAdapter(MedicalRecordsActivity medicalRecordsActivity, List<DataItem> dataItems) {
        this.context = medicalRecordsActivity;
        this.responses = dataItems;
    }

    @NonNull
    @Override
    public MedicalRecordsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.medicalrecords_list, parent, false);
        return new MedicalRecordsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MedicalRecordsAdapter.Viewholder holder, int position) {
        com.drcita.user.models.medicalrecords.DataItem list = responses.get(position);
        /*if (responses.get(position).equals(0)){
            holder.newTV.setVisibility(View.VISIBLE);
        }
        else {
            holder.newTV.setVisibility(View.GONE);

        }*/
        holder.hospitalname.setText("Records Added by" + " " + list.getHospitalName());
        holder.hospitaltitle.setText(list.getHospitalName());
        holder.doctorNameTV.setText(list.getDoctorName());

        String insertDate = list.getAddedOn();
        Log.d("date", String.valueOf(insertDate));

        String[] items1 = insertDate.split("-");
        String d1 = items1[0];
        String m1 = items1[1];
        String y1 = items1[2];
        int d = Integer.parseInt(d1);
        int m = Integer.parseInt(m1);
        int y = Integer.parseInt(y1);
        Log.d("d", String.valueOf(d));
        Log.d("m", String.valueOf(m));
        Log.d("y", String.valueOf(y));
        holder.date.setText("" + d);
        holder.month.setText(month[m - 1]);


    }

    @Override
    public int getItemCount() {
        return responses.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView newTV, hospitalname, hospitaltitle, date, month,doctorNameTV;
        MaterialCardView medicalrecordsLayout;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            //  newTV = itemView.findViewById(R.id.newTV);
            hospitalname = itemView.findViewById(R.id.hospitalname);
            date = itemView.findViewById(R.id.date);
            month = itemView.findViewById(R.id.month);
            hospitaltitle = itemView.findViewById(R.id.hospitaltitle);
            medicalrecordsLayout = itemView.findViewById(R.id.medicalrecordsLayout);
            doctorNameTV = itemView.findViewById(R.id.doctorNameTV);

            medicalrecordsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.drcita.user.models.medicalrecords.DataItem list = responses.get(getAdapterPosition());
                    int id = list.getAppointmentId();
                    Intent intent = new Intent(context, ViewMedicalRecordsActivity.class);
                    intent.putExtra("id", id);
                    context.startActivity(intent);
                }
            });

        }

    }
}