package com.drcita.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.Activity.VideoConsultationActivity;
import com.drcita.user.R;
import com.drcita.user.videomeeting.Common.Actvity.CreateOrJoinActivity;

import java.util.List;

public class VideoConsultationDocterAdapter extends RecyclerView.Adapter<VideoConsultationDocterAdapter.ViewHolder> {

    private List<String> doctersList;
    private Context context;

    public VideoConsultationDocterAdapter(Context context, List<String> doctersList) {
        this.context = context;
        this.doctersList = doctersList;
    }

    @NonNull
    @Override
    public VideoConsultationDocterAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_consultation_doctors_list_layout, parent, false);
        return new VideoConsultationDocterAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoConsultationDocterAdapter.ViewHolder holder, int position) {
        String docter = doctersList.get(position);
        holder.doctorName.setText(docter);

        holder.hospitalName.setText("Daad Orthopedic and Trauma Speciality Clinic");

        holder.location.setText("Hyderabad");
        holder.consultationfee.setText("₹ 14");
        holder.specialization.setText("General Physician");

        holder.bookOnlineappotimentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateOrJoinActivity.class);

                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return doctersList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, specialization, doctorExperience, availbility, consultationfee, location, bookOnlineappotimentBtn, hospitalName ;

        LinearLayout bookOfflineappotimentBtn;

        public ViewHolder(View itemView) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            doctorName = itemView.findViewById(R.id.doctorName);
            specialization = itemView.findViewById(R.id.specialization);
            doctorExperience = itemView.findViewById(R.id.doctorExperience);
            availbility = itemView.findViewById(R.id.availbility);
            consultationfee = itemView.findViewById(R.id.consultationfee);
            location = itemView.findViewById(R.id.location);
            bookOnlineappotimentBtn = itemView.findViewById(R.id.bookOnlineappotimentBtn);
            bookOfflineappotimentBtn = itemView.findViewById(R.id.bookOfflineappotimentBtn);
        }
    }
}
