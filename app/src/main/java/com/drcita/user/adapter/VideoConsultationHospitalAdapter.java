package com.drcita.user.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.Activity.VideoConsultationDocterActivity;
import com.drcita.user.HospitalsListActivity;
import com.drcita.user.R;

import java.util.List;

public class VideoConsultationHospitalAdapter extends RecyclerView.Adapter<VideoConsultationHospitalAdapter.ViewHolder> {

    private List<String> hospitalList;
    private int selectedPosition = -1; // For single selection
    private Context context;

    public VideoConsultationHospitalAdapter(Context context, List<String> hospitalList) {
        this.context = context;
        this.hospitalList = hospitalList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_consultation_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        String hospital = hospitalList.get(position);
        holder.tvHospitalName.setText(hospital);
        holder.radioButton.setChecked(position == selectedPosition);

        holder.itemView.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
            Intent intent = new Intent(context, VideoConsultationDocterActivity.class);
            intent.putExtra("specailization", 0);
            context.startActivity(intent);
        });

        holder.radioButton.setOnClickListener(v -> {
            selectedPosition = position;
            notifyDataSetChanged();
        });
    }

    @Override
    public int getItemCount() {
        return hospitalList.size();
    }

    public String getSelectedHospital() {
        return selectedPosition != -1 ? hospitalList.get(selectedPosition) : null;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvHospitalName;
        RadioButton radioButton;

        public ViewHolder(View itemView) {
            super(itemView);
            tvHospitalName = itemView.findViewById(R.id.tvHospitalName);
            radioButton = itemView.findViewById(R.id.radioButton);
        }
    }
}