package com.drcita.user.adapter.specilaization;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.drcita.user.R;

import java.util.List;

/**
 * RecyclerView adapter that lets the user pick ONE hospital name.
 */
public class SingleSelectHospitalAdapter
        extends RecyclerView.Adapter<SingleSelectHospitalAdapter.ViewHolder> {

    private final List<String> hospitalList;
    private int selectedPosition = -1;              // keeps currently‑selected row
    private final OnSingleHospitalSelected listener;

    public SingleSelectHospitalAdapter(List<String> hospitalList,
                                       OnSingleHospitalSelected listener) {
        this.hospitalList = hospitalList;
        this.listener     = listener;
        setHasStableIds(true);                      // smoother updates when rows refresh
    }

    // Expose currently‑selected name to caller
    public String getSelectedHospital() {
        return (selectedPosition != -1) ? hospitalList.get(selectedPosition) : null;
    }

    // ---------- VIEW‑HOLDER ----------
    static class ViewHolder extends RecyclerView.ViewHolder {
        final RadioButton rbHospital;

        ViewHolder(View itemView) {
            super(itemView);
            rbHospital = itemView.findViewById(R.id.rbHospital);
        }

        void bind(String hospitalName, boolean isChecked) {
            rbHospital.setText(hospitalName);
            rbHospital.setChecked(isChecked);
        }
    }

    // ---------- ADAPTER OVERRIDES ----------
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_single_select_hospital, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        boolean isChecked = (position == selectedPosition);
        holder.bind(hospitalList.get(position), isChecked);

        holder.rbHospital.setOnClickListener(v -> {
            int previous = selectedPosition;
            selectedPosition = holder.getAdapterPosition();
            // refresh only the two affected rows
            if (previous != -1) notifyItemChanged(previous);
            notifyItemChanged(selectedPosition);

            if (listener != null)
                listener.onHospitalSelected(hospitalList.get(selectedPosition));
        });
    }

    @Override
    public int getItemCount() { return hospitalList.size(); }

    // stable IDs improve checkbox/radiobutton behaviour on fast scroll
    @Override
    public long getItemId(int position) { return position; }

    // ---------- CALLBACK ----------
    public interface OnSingleHospitalSelected {
        void onHospitalSelected(String hospitalName);
    }
}
