package com.drcita.user.adapter.specilaization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;
import com.drcita.user.models.specilization.ProviderModel;

import java.util.List;

public class SingleSelectHospitalAdapter extends RecyclerView.Adapter<SingleSelectHospitalAdapter.ViewHolder> {

    private final List<ProviderModel> providers;
    private int selectedPosition = -1;
    private final OnSingleProviderSelected listener;

    public interface OnSingleProviderSelected {
        void onProviderSelected(ProviderModel provider);
    }

    public SingleSelectHospitalAdapter(List<ProviderModel> providers, OnSingleProviderSelected listener) {
        this.providers = providers;
        this.listener = listener;
        setHasStableIds(true);

        if (!providers.isEmpty()) {
            selectedPosition = 0;
            listener.onProviderSelected(providers.get(0));
        }
    }

    public ProviderModel getSelectedProvider() {
        return selectedPosition != -1 ? providers.get(selectedPosition) : null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        RadioButton rbHospital;

        ViewHolder(View itemView) {
            super(itemView);
            rbHospital = itemView.findViewById(R.id.rbHospital);
        }

        void bind(String name, boolean isChecked) {
            rbHospital.setText(name);
            rbHospital.setChecked(isChecked);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_single_select_hospital, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ProviderModel provider = providers.get(position);
        boolean isChecked = position == selectedPosition;

        String name = provider.getHospitalName() + " (" + provider.getArea() + ")";
        holder.bind(name, isChecked);

        holder.rbHospital.setOnClickListener(v -> {
            int previous = selectedPosition;
            selectedPosition = holder.getAdapterPosition();

            if (previous != -1) notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);

            if (listener != null)
                listener.onProviderSelected(providers.get(selectedPosition));
        });
    }

    @Override
    public int getItemCount() {
        return providers.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
