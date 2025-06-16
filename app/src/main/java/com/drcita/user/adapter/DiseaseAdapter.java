package com.drcita.user.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;
import com.drcita.user.models.profile.Disease;
import com.drcita.user.models.profile.DiseaseStatus;
import com.drcita.user.models.userprofile.DiseaseModel;

import java.util.ArrayList;
import java.util.List;

public class DiseaseAdapter extends RecyclerView.Adapter<DiseaseAdapter.DiseaseViewHolder> {

    private final List<Disease> diseaseList;

    public DiseaseAdapter(List<Disease> list) {
        this.diseaseList = list;
    }

    @NonNull
    @Override
    public DiseaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_disease, parent, false);
        return new DiseaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DiseaseViewHolder holder, int position) {
        Disease model = diseaseList.get(position);
        if(model.getDiseaseName()!=null) {
            holder.tvDiseaseName.setText(model.getDiseaseName());
        }
        else {
            holder.tvDiseaseName.setText(model.getDisease());
        }

//        model.setSelection(model.getStatus());

        // Update UI based on selection
        updateButtonUI(holder, model.getSelection());

        holder.btnYes.setOnClickListener(v -> {
            model.setSelection(1); // 1 = Yes
            notifyItemChanged(position);
        });

        holder.btnNo.setOnClickListener(v -> {
            model.setSelection(0); // 2 = No
            notifyItemChanged(position);
        });
    }

    private void updateButtonUI(DiseaseViewHolder holder, int selection) {
        holder.btnYes.setAlpha(selection == 1? 1f : 0.4f);
        holder.btnNo.setAlpha(selection == 0? 1f : 0.4f);
    }

    @Override
    public int getItemCount() {
        return diseaseList.size();
    }

    static class DiseaseViewHolder extends RecyclerView.ViewHolder {
        TextView tvDiseaseName;
        Button btnYes, btnNo;

        public DiseaseViewHolder(@NonNull View itemView) {
            super(itemView);
            tvDiseaseName = itemView.findViewById(R.id.tvDiseaseName);
            btnYes = itemView.findViewById(R.id.btnYes);
            btnNo = itemView.findViewById(R.id.btnNo);
        }
    }

    // ✅ Call this method to get the selected diseases
    public List<DiseaseStatus> getSelectedDiseases() {
        List<DiseaseStatus> selectedList = new ArrayList<>();
        for (Disease disease : diseaseList) {
            int selection = disease.getSelection();
            if (selection == 1 || selection == 2) {
                selectedList.add(new DiseaseStatus(disease.getId(), selection));
            }
        }
        return selectedList;
    }
}
