package com.drcita.user.adapter.specilaization;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.DoctorAppointmentActivity;
import com.drcita.user.R;
import com.drcita.user.models.specilization.DoctorModel;
import com.drcita.user.models.specilization.ProviderModel;

import java.util.ArrayList;
import java.util.List;

public class HospitalAdapter extends RecyclerView.Adapter<HospitalAdapter.DoctorViewHolder> implements Filterable {

    private Context context;
    private List<DoctorModel> doctorList;
    public List<DoctorModel> fullDoctorList;

    public HospitalAdapter(Context context, List<DoctorModel> doctorList) {
        this.context = context;
        this.doctorList = doctorList;
        this.fullDoctorList = new ArrayList<>(doctorList);
    }

    @NonNull
    @Override
    public DoctorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.docterlist, parent, false);
        return new DoctorViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DoctorViewHolder holder, int position) {
        DoctorModel doctor = doctorList.get(position);

        holder.tvName.setText(doctor.getDoctorName());
        holder.tvHospitalCount.setText(doctor.getProviderCount() + "+");
        holder.tvExperience.setText(doctor.getExperience() + " Years Exp.");
        holder.tvRating.setText("Rating: " + doctor.getRating());
        holder.tvSpecialization.setText(TextUtils.join(", ", doctor.getSpecializations()));
        holder.tvLanguages.setText(TextUtils.join(", ", doctor.getLanguages()));

        Glide.with(context)
                .load(doctor.getPicture())
                .placeholder(R.drawable.docter_img)
                .into(holder.imgDoctor);

        if (!doctor.getProviders().isEmpty()) {
            ProviderModel provider = doctor.getProviders().get(0);

            boolean isAvailable = provider.getMorningAvailable() == 1 ||
                    provider.getAfternoonAvailable() == 1 ||
                    provider.getEveningAvailable() == 1;

            if (isAvailable) {
                holder.tvAvailability.setText("Available Today");
                holder.tvAvailability.setBackgroundResource(R.drawable.bg_available);
                holder.tvAvailability.setTextColor(Color.BLACK);
                holder.ll_time.setVisibility(VISIBLE);
            } else {
                holder.tvAvailability.setText("Not Available");
                holder.tvAvailability.setBackgroundResource(R.drawable.bg_not_available);
                holder.tvAvailability.setTextColor(Color.parseColor("#D90B0D"));
                holder.ll_time.setVisibility(GONE);
            }

            holder.tvMorning.setTextColor(ContextCompat.getColor(context,
                    provider.getMorningAvailable() == 1 ? R.color.green : R.color.gray));
            holder.tvAfternoon.setTextColor(ContextCompat.getColor(context,
                    provider.getAfternoonAvailable() == 1 ? R.color.green : R.color.gray));
            holder.tvEvening.setTextColor(ContextCompat.getColor(context,
                    provider.getEveningAvailable() == 1 ? R.color.green : R.color.gray));

            holder.tvonlinefees.setText("₹" + provider.getOnlineFee());
            holder.tvofflinefees.setText("₹" + provider.getOfflineFee());

            // Handle consultation mode
            switch (provider.getConsultationMode()) {
                case 1: // Only Online
                    holder.btnOnline.setVisibility(VISIBLE);
                    holder.btnOffline.setVisibility(GONE);
                    holder.buttonContainer.setGravity(Gravity.END);
                    break;
                case 2: // Only Offline
                    holder.btnOnline.setVisibility(GONE);
                    holder.btnOffline.setVisibility(VISIBLE);
                    break;
                case 3: // Both
                    holder.btnOnline.setVisibility(VISIBLE);
                    holder.btnOffline.setVisibility(VISIBLE);
                    holder.buttonContainer.setGravity(Gravity.NO_GRAVITY);
                    break;
                default:
                    holder.btnOnline.setVisibility(GONE);
                    holder.btnOffline.setVisibility(GONE);
            }
        }
        // Button click inside onBindViewHolder
        holder.btnOnline.setOnClickListener(view -> {
            Intent intent = new Intent(context, DoctorAppointmentActivity.class);
            intent.putExtra("docterId", String.valueOf(doctor.doctorId));
            intent.putExtra("providerId",String.valueOf(doctor.getProviders().get(0).providerId));
            intent.putExtra("paymentype","Online");
            intent.putExtra("amount",String.valueOf(doctor.providers.get(0).onlineFee));
            intent.putExtra("slotdate","");
            intent.putExtra("isresechedule","0");
            context.startActivity(intent);
        });


        holder.btnOffline.setOnClickListener(view -> {
            Intent intent = new Intent(context, DoctorAppointmentActivity.class);
            intent.putExtra("docterId", String.valueOf(doctor.doctorId));
            intent.putExtra("providerId",String.valueOf(doctor.getProviders().get(0).providerId));
            intent.putExtra("paymentype","offline");
            intent.putExtra("amount",String.valueOf(doctor.providers.get(0).offlineFee));
            context.startActivity(intent);
        });
        holder.tvHospitalCount.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION && doctorList.get(pos) != null) {
                showSingleHospitalDialog(context, doctorList.get(pos).getProviders());
            }
        });
    }

    @Override
    public int getItemCount() {
        return doctorList.size();
    }

    @Override
    public Filter getFilter() {
        return doctorFilter;
    }

    private final Filter doctorFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            List<DoctorModel> filteredList = new ArrayList<>();
            if (TextUtils.isEmpty(constraint)) {
                filteredList.addAll(fullDoctorList);
            } else {
                String filterPattern = constraint.toString().toLowerCase().trim();
                for (DoctorModel doctor : fullDoctorList) {
                    boolean matchName = doctor.getDoctorName().toLowerCase().contains(filterPattern);
                    boolean matchSpecialization = doctor.getSpecializations().toString().toLowerCase().contains(filterPattern);

                    boolean matchHospital = false;
                    for (ProviderModel provider : doctor.getProviders()) {
                        if (provider.getHospitalName().toLowerCase().contains(filterPattern)) {
                            matchHospital = true;
                            break;
                        }
                    }

                    if (matchName || matchSpecialization || matchHospital) {
                        filteredList.add(doctor);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            doctorList.clear();
            doctorList.addAll((List<DoctorModel>) results.values);
            notifyDataSetChanged();
        }
    };

    public void clearAllData() {
        doctorList.clear();
    }

    static class DoctorViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvExperience, tvRating, tvSpecialization, tvLanguages,
                tvAvailability, tvMorning, tvAfternoon, tvEvening,
                tvonlinefees, tvofflinefees, tvHospitalCount;
        LinearLayout btnOnline, btnOffline, ll_time,buttonContainer;
        ImageView imgDoctor;

        public DoctorViewHolder(@NonNull View itemView) {
            super(itemView);
            imgDoctor = itemView.findViewById(R.id.imgDoctor);
            tvName = itemView.findViewById(R.id.tvDoctorName);
            tvExperience = itemView.findViewById(R.id.tvExperience);
            tvRating = itemView.findViewById(R.id.tvRating);
            tvSpecialization = itemView.findViewById(R.id.tvSpecialization);
            tvLanguages = itemView.findViewById(R.id.tvlanguage);
            tvAvailability = itemView.findViewById(R.id.tvAvailability);
            btnOnline = itemView.findViewById(R.id.ll_onlineconsult);
            buttonContainer=itemView.findViewById(R.id.buttonContainer);
            btnOffline = itemView.findViewById(R.id.ll_bookhospital);
            ll_time = itemView.findViewById(R.id.llTimings);
            tvMorning = itemView.findViewById(R.id.tvMorning);
            tvAfternoon = itemView.findViewById(R.id.tvAfternoon);
            tvEvening = itemView.findViewById(R.id.tvEvening);
            tvonlinefees = itemView.findViewById(R.id.tvonlinefees);
            tvofflinefees = itemView.findViewById(R.id.tvofflinefees);
            tvHospitalCount = itemView.findViewById(R.id.tvHospitalCount);


        }

    }
    private void showSingleHospitalDialog(Context context, List<ProviderModel> providers) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_single_select_hospital, null);
        RecyclerView recyclerView = view.findViewById(R.id.rvHospitalList);

        final ProviderModel[] selectedProvider = {null};

        // Adapter that works with ProviderModel directly
        SingleSelectHospitalAdapter adapter = new SingleSelectHospitalAdapter(providers, provider -> {
            selectedProvider[0] = provider;
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);

        builder.setView(view)
                .setPositiveButton("OK", (dialog, which) -> {
//                    if (selectedProvider[0] != null) {
//                        Toast.makeText(context, "Final selection: " + selectedProvider[0].getHospitalName(), Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "No hospital selected", Toast.LENGTH_SHORT).show();
//                    }
                })
                .setNegativeButton("Cancel", null)
                .show();
    }


//    private void showSingleHospitalDialog(Context context, List<ProviderModel> providers) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context);
//        View view = LayoutInflater.from(context).inflate(R.layout.dialog_single_select_hospital, null);
//        RecyclerView recyclerView = view.findViewById(R.id.rvHospitalList);
//
//        List<String> hospitalNames = new ArrayList<>();
//        for (ProviderModel provider : providers) {
//            hospitalNames.add(provider.getHospitalName());
//        }
//
//        // Create adapter with click listener
//        SingleSelectHospitalAdapter adapter = new SingleSelectHospitalAdapter(hospitalNames, hospitalName -> {
//            // Handle the selected hospital (this is triggered on radio click)
//            Toast.makeText(context, "Selected: " + hospitalName, Toast.LENGTH_SHORT).show();
//        });
//
//        recyclerView.setLayoutManager(new LinearLayoutManager(context));
//        recyclerView.setAdapter(adapter);
//
//        builder.setView(view)
//                .setPositiveButton("OK", (dialog, which) -> {
//                    String selectedHospital = adapter.getSelectedProvider().hospitalName;
//                    if (selectedHospital != null) {
//                        // Do something with the selected hospital
//                        Toast.makeText(context, "Final selection: " + selectedHospital, Toast.LENGTH_SHORT).show();
//                    } else {
//                        Toast.makeText(context, "No hospital selected", Toast.LENGTH_SHORT).show();
//                    }
//                })
//                .setNegativeButton("Cancel", null)
//                .show();
//    }
}

