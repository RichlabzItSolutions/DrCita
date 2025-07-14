package com.drcita.user.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.ProfileUpdateActivity;
import com.drcita.user.R;
import com.drcita.user.models.profile.Disease;
import com.drcita.user.models.profile.DiseaseStatus;

import java.util.ArrayList;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    private final List<Disease> diseaseList;
    private final Context context;

    public DiseaseAdapter(ProfileUpdateActivity profileUpdateActivity, List<Disease> list) {
        this.context = profileUpdateActivity;
        this.diseaseList = list;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_disease, parent, false);
        return new DiseaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease model = diseaseList.get(position);

        // Set disease name
        String name = model.getDiseaseName() != null ? model.getDiseaseName() : model.getDisease();
        holder.tvDiseaseName.setText(name);

        // Update UI based on current selection
        updateButtonUI(holder, model.getSelection());

        // Handle YES click: Set 1
        holder.btnYes.setOnClickListener(v -> {
            model.setSelection(1); // Yes
            notifyItemChanged(position);
        });

        // Handle NO click: Set 0
        holder.btnNo.setOnClickListener(v -> {
            model.setSelection(0); // No
            notifyItemChanged(position);
        });
    }

    /**
     * Updates the button styles based on selection.
     */
    private void updateButtonUI(DiseaseViewHolder holder, int selection) {
        if (selection == 1) {
            // YES selected
            holder.btnYes.setBackgroundResource(R.drawable.bg_yes_selected);
            holder.btnYes.setTextColor(Color.WHITE);

            holder.btnNo.setBackgroundResource(R.drawable.bg_no_unselected);
            holder.btnNo.setTextColor(Color.parseColor("#F44336"));
        } else if (selection == 0) {
            // NO selected
            holder.btnNo.setBackgroundResource(R.drawable.bg_no_selected);
            holder.btnNo.setTextColor(Color.WHITE);

            holder.btnYes.setBackgroundResource(R.drawable.bg_yes_unselected);
            holder.btnYes.setTextColor(Color.parseColor("#4CAF50"));
        } else {
            // UNSELECTED (-1)
            holder.btnYes.setBackgroundResource(R.drawable.bg_yes_unselected);
            holder.btnYes.setTextColor(Color.parseColor("#4CAF50"));

            holder.btnNo.setBackgroundResource(R.drawable.bg_no_unselected);
            holder.btnNo.setTextColor(Color.parseColor("#F44336"));
        }
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiseaseName;
        AppCompatButton btnYes, btnNo;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiseaseName = itemView.findViewById(R.id.tvDiseaseName);
            btnYes = itemView.findViewById(R.id.btnYes);
            btnNo = itemView.findViewById(R.id.btnNo);
        }
    }

    /**
     * Returns selected diseases with status 0 or 1.
     */
    public List<DiseaseStatus> getSelectedDiseases() {
        List<DiseaseStatus> selectedYesList = new ArrayList<>();
        for (Disease disease : diseaseList) {
            if (disease.getSelection() == 1) { // Only "Yes"
                selectedYesList.add(new DiseaseStatus(disease.getId(), 1));
            }
        }
        return selectedYesList;
    }
}