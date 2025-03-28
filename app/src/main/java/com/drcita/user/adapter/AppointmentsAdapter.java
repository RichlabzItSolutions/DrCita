package com.drcita.user.adapter;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.AppointmentsActivity;
import com.drcita.user.R;
import com.drcita.user.ViewreceiptActivity;
import com.drcita.user.models.appointment.DataItem;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AppointmentsAdapter extends RecyclerView.Adapter<AppointmentsAdapter.Viewholder> {
    private AppointmentsActivity context;
    private List<DataItem> dataItems = new ArrayList<DataItem>();
    private String userId;
    private ProgressDialog progress;


    public AppointmentsAdapter(AppointmentsActivity appointmentsActivity, List<DataItem> dataItems) {
        this.context = appointmentsActivity;
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public AppointmentsAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.appointments_list, parent, false);
        return new AppointmentsAdapter.Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentsAdapter.Viewholder holder, int position) {
        com.drcita.user.models.appointment.DataItem list = dataItems.get(position);
        holder.slotno.setText("Card No:"+" "+list.getSlotNumber());
        holder.date.setText(list.getSlotDate());
        holder.hospitalnameTV.setText(list.getHospitalName());
      //  holder.appointmentId.setText("Appointment Id:"+""+list.getAppointmentId());
        if (!list.getPicture().isEmpty()) {
            Picasso.with(context).load(list.getPicture())
                    .into(holder.patientImage);
        }else {
            Picasso.with(context).load(R.drawable.avtar)
                    .into(holder.patientImage);
        }
        if (list.getAppointmentType()==1){
            holder.patientname.setText(list.getFirstName()+" "+ list.getLastName());
            holder.specailzation.setText(list.getSpecializationName());
        }else {
            holder.patientname.setText(list.getScanName());
            holder.specailzation.setText(list.getHospitalName());
        }
        if (!(list.getAppointmentStatus() == 3)){
            holder.cancel.setText("Cancel");
            holder.cancel.setBackgroundColor(context.getResources().getColor(R.color.red));
            holder.cancel.setTextColor(context.getResources().getColor(R.color.white));
        }else
        if (list.getAppointmentStatus() == 3){
            holder.cancel.setText("Cancelled");
            holder.cancel.setClickable(false);
            holder.cancel.setBackgroundColor(Color.TRANSPARENT);
            holder.cancel.setTextColor(context.getResources().getColor(R.color.red));
        }
    }
    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView patientname,slotno,date,cancel,specailzation,hospitalnameTV;
        ImageView patientImage;
        CardView appointmentsLayout;

        public Viewholder(@NonNull View itemView) {
            super(itemView);
            patientname = itemView.findViewById(R.id.patientname);
            slotno = itemView.findViewById(R.id.slotno);
            date = itemView.findViewById(R.id.date);
            patientImage = itemView.findViewById(R.id.patientImage);
            appointmentsLayout = itemView.findViewById(R.id.appointmentsLayout);
            cancel = itemView.findViewById(R.id.cancel);
            specailzation = itemView.findViewById(R.id.specailzation);
            hospitalnameTV = itemView.findViewById(R.id.hospitalnameTV);
            appointmentsLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.drcita.user.models.appointment.DataItem list = dataItems.get(getAdapterPosition());
                    Intent intent = new Intent(context, ViewreceiptActivity.class);
                    intent.putExtra("id", list.getAppointmentId());
                    intent.putExtra("position",list.getAppointmentType());
                    context.startActivity(intent);

                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.drcita.user.models.appointment.DataItem list = dataItems.get(getAdapterPosition());
                    int id = list.getAppointmentId();
                    showpopup(id,getAdapterPosition());
                }
            });

        }

        private void showpopup(int id, int adapterPosition) {
            Dialog dialog = new Dialog(context);
            dialog.setCancelable(true);

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.cancel_popup, null);
            dialog.setContentView(view);
            CardView submit_popup;
            TextView okButton,cancelButton;
            okButton = view.findViewById(R.id.okButton);
            cancelButton = view.findViewById(R.id.cancelButton);
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.callApi(id,adapterPosition);
                    dialog.dismiss();

                }
            });
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();

                }
            });
            dialog.show();
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
}
