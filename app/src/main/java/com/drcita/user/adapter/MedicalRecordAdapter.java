package com.drcita.user.adapter;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.R;
import com.drcita.user.models.medicalrecords.MedicalAssetFile;
import com.drcita.user.models.medicalrecords.MedicalRecordData;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

public class MedicalRecordAdapter extends RecyclerView.Adapter<MedicalRecordAdapter.RecordViewHolder> {

    private Context context;
    private List<MedicalRecordData> records;

    public MedicalRecordAdapter(Context context, List<MedicalRecordData> records) {
        this.context = context;
        this.records = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_medical_record, parent, false);
        return new RecordViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, int position) {
        MedicalRecordData record = records.get(position);

        holder.tvDate.setText(record.getAddedOn());
        holder.tvAddedBy.setText("Records added by " + record.getUserName());

        String desc = record.getDescription();
        if (desc == null || desc.isEmpty()) {
            desc = "No description provided.";
        }
        holder.tvDesc.setText(desc);

        // Load image URLs
        List<String> imageUrls = new ArrayList<>();
        if (record.getAssets() != null && record.getAssets().getImages() != null) {
            for (MedicalAssetFile img : record.getAssets().getImages()) {
                imageUrls.add(img.getFile());
            }
        }

        // Load document URLs
        List<String> documentUrls = new ArrayList<>();
        if (record.getAssets() != null && record.getAssets().getDocuments() != null) {
            for (MedicalAssetFile doc : record.getAssets().getDocuments()) {
                documentUrls.add(doc.getFile());
            }
        }

        // Set up Image RecyclerView
        DynamicImageAdapter imageAdapter = new DynamicImageAdapter(context, imageUrls);
        holder.rvImages.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvImages.setAdapter(imageAdapter);

        // Set up Document RecyclerView
        DocumentAdapter docAdapter = new DocumentAdapter(context, documentUrls);
        holder.rvDocuments.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        holder.rvDocuments.setAdapter(docAdapter);
    }

    @Override
    public int getItemCount() {
        return records.size();
    }

    public static class RecordViewHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvAddedBy, tvDesc;
        RecyclerView rvImages, rvDocuments;

        public RecordViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvRecordDate);
            tvAddedBy = itemView.findViewById(R.id.tvRecordUser);
            tvDesc = itemView.findViewById(R.id.tvRecordDesc);
            rvImages = itemView.findViewById(R.id.rvImages);
            rvDocuments = itemView.findViewById(R.id.rvDocuments);
        }
    }


}
