package com.drcita.user.adapter;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.drcita.user.DoctorAppointmentActivity;
import com.drcita.user.R;
import com.drcita.user.ViewreceiptActivity;
import com.drcita.user.common.Constants;
import com.drcita.user.models.appointmentbookingsummary.AppointmentBookingData;
import com.drcita.user.models.appointmentbookingsummary.CancelReason;
import com.drcita.user.models.appointmentbookingsummary.CancelReasonResponse;
import com.drcita.user.models.appointmentbookingsummary.CancelRequest;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.retrofit.ApiClient;

import org.parceler.Parcels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class BookingAppointmentAdapter extends RecyclerView.Adapter<BookingAppointmentAdapter.ViewHolder> {

    private Context context;
    private List<AppointmentBookingData> appointments;
    public     ArrayList<String> reasons=new ArrayList<>();
    private String userId;

    public BookingAppointmentAdapter(Context context, List<AppointmentBookingData> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public BookingAppointmentAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_appointment_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookingAppointmentAdapter.ViewHolder holder, int position) {
        AppointmentBookingData item = appointments.get(position);

        holder.doctorName.setText(item.getDoctorName());
        holder.speciality.setText(TextUtils.join(", ", item.getSpecializationName()));
        holder.hospitalName.setText(item.getHospitalName()+"\n"+item.getArea());
        holder.multiSpeciality.setText(item.getHospitalType());
        holder.appointmentDate.setText(item.getSlotDate());
        holder.appointmentTime.setText(item.getSlotTime());
        holder.appointmentStatus.setText(item.getStatusString());
        holder.appointment.setText(String.valueOf(item.getAppointmentNum()));
        holder.tvamount.setText("Paid Amount : ₹"+item.getPaidAmount());

        holder.statusOnline.setText(item.isOnline() ? "Offline" : "Online");
        boolean showButtons = Constants.isMoreThanTwoHoursLeft(item.getSlotDate(), item.getSlotTime());
        boolean showReschdule = Constants.isRescheduleEnable(item.getSlotDate(), item.getSlotTime());
        holder.btnCancel.setVisibility(showButtons ? VISIBLE : GONE);
        holder.btnReschedule.setVisibility(showReschdule ? VISIBLE : GONE);
        // Set status color and visibility of buttons
        switch (item.getAppointmentStatus()) {
            case 3: // Completed
                holder.appointmentStatus.setTextColor(Color.parseColor("#4CAF50"));
                holder.btnCancel.setVisibility(GONE);
                holder.btnReschedule.setVisibility(GONE);
                holder.view_recipt.setVisibility(VISIBLE);
                break;
            case 4: // Cancelled
                holder.appointmentStatus.setTextColor(Color.RED);
                holder.btnCancel.setVisibility(GONE);
                holder.btnReschedule.setVisibility(GONE);
                holder.view_recipt.setVisibility(GONE);
                break;
            default: // New, Confirmed, etc.
                holder.appointmentStatus.setTextColor(Color.parseColor("#FF9900"));
                holder.viewreceiptBtn.setVisibility(VISIBLE);
                break;
        }

        Glide.with(context)
                .load(item.getPicture())
                .placeholder(R.drawable.docter_img)
                .circleCrop()
                .into(holder.doctorImage);

        // Cancel Button Action
        holder.btnCancel.setOnClickListener(v -> {
            int adapterPosition = holder.getAdapterPosition();
            if (adapterPosition != RecyclerView.NO_POSITION) {
                showCommentsDialog(context, adapterPosition,item.getAppointmentId());
            }
        });

        holder.btnReschedule.setOnClickListener(view -> {
            Intent intent = new Intent(context, DoctorAppointmentActivity.class);
            intent.putExtra("docterId", String.valueOf(item.getAppointmentId()));
            intent.putExtra("providerId","");
            intent.putExtra("paymentype",holder.statusOnline.getText().toString());
            intent.putExtra("amount", "0");
            intent.putExtra("slotdate",item.getSlotDate());
            intent.putExtra("isresechedule","1");
            context.startActivity(intent);
        });

        holder.viewreceiptBtn.setOnClickListener(view -> {
            Intent intent = new Intent(context, ViewreceiptActivity.class);
            intent.putExtra("doctorData", "");
            intent.putExtra("id", item.getAppointmentId());
            intent.putExtra("position",2);
            intent.putExtra("payment",holder.statusOnline.getText().toString());
             intent.putExtra("from",2);
           context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    // ✅ Update a single appointment in the list
    public void updateAppointment(int position, AppointmentBookingData updatedItem) {
        appointments.set(position, updatedItem);
        notifyItemChanged(position);
    }

    private void showCommentsDialog (Context context, int position, int appointmentId) {

        try {
            AppointmentBookingData item = appointments.get(position);
            SharedPreferences sp =context.getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            userId = sp.getString(Constants.USER_ID, userId);
            Dialog dialog = new Dialog(context, R.style.CustomAlertDialog);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.dialog_cancel_reason);
            dialog.setCancelable(true);
            Spinner spinner = dialog.findViewById(R.id.spinnerComments);
            EditText etOther = dialog.findViewById(R.id.etOtherComments);
            Button btnSubmit = dialog.findViewById(R.id.submitCancelBtn);
            ImageView ivClose = dialog.findViewById(R.id.btnClose);
            // Spinner options
            ArrayAdapter<String> adapter = new ArrayAdapter<>(context, R.layout.spinner_item, reasons);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            ivClose.setOnClickListener(v -> dialog.dismiss());

            // Spinner selection listener to toggle other comment field
            spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selected = parent.getItemAtPosition(position).toString();
                    if (selected.equalsIgnoreCase("other")) {
                        etOther.setVisibility(VISIBLE);
                    } else {
                        etOther.setVisibility(GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                }
            });

            btnSubmit.setOnClickListener(v -> {
                String selectedComment = spinner.getSelectedItem().toString().trim();
                String otherComment = etOther.getText().toString().trim();

                // If "others" is selected, use the EditText content as the selected comment
                if (selectedComment.equalsIgnoreCase("other")) {
                    if (otherComment.isEmpty()) {
                        etOther.setError("Please enter a reason");
                        return;
                    }
                    selectedComment = otherComment;
                }

                submitList(appointmentId, selectedComment,item,position);
                dialog.dismiss();
            });

            dialog.show();
        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void submitList(int appointmentId, String selectedComment, AppointmentBookingData item, int position) {


        try {
            if (Constants.haveInternet(context)) {

                CancelRequest cancelRequest = new CancelRequest(appointmentId, userId, selectedComment);

                ApiClient.getRestAPI().cancelAppointment(cancelRequest).enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RestResponse> call, @NonNull retrofit2.Response<RestResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                item.setAppointmentStatus(4); // Cancelled
                                updateAppointment(position, item);
                                Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            try {
                                Constants.displayError(response.errorBody().string(), context);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestResponse> call, @NonNull Throwable t) {
                        t.printStackTrace();

                    }
                });


            }

        }catch (Exception e)
        {
            e.getMessage();
        }
    }
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView doctorName, speciality, hospitalName, multiSpeciality, statusOnline;
        TextView appointmentDate, appointmentTime, appointmentStatus,appointment,tvamount;
        TextView btnReschedule;
        TextView btnCancel;
        ImageView doctorImage;
        LinearLayout viewreceiptBtn;
        ImageView view_recipt;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            doctorName = itemView.findViewById(R.id.doctorName);
            speciality = itemView.findViewById(R.id.doctorSpeciality);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            multiSpeciality = itemView.findViewById(R.id.multiSpeciality);
            statusOnline = itemView.findViewById(R.id.statusOnline);
            appointmentDate = itemView.findViewById(R.id.appointmentDate);
            appointmentTime = itemView.findViewById(R.id.appointmentTime);
            appointmentStatus = itemView.findViewById(R.id.appointmentStatus);
            btnReschedule = itemView.findViewById(R.id.btnReschedule);
            btnCancel = itemView.findViewById(R.id.btnCancel);
            doctorImage = itemView.findViewById(R.id.doctorImage);
            appointment=itemView.findViewById(R.id.appointment);
            viewreceiptBtn=itemView.findViewById(R.id.viewreceiptBtn);
            tvamount=itemView.findViewById(R.id.tv_paidamount);
            view_recipt=itemView.findViewById(R.id.view_recipt);
            callReasonApI();
        }

    private void callReasonApI() {
            try{

                try{

                    if (Constants.haveInternet(context)) {
                        ApiClient.getRestAPI().getCancelReason().enqueue(new Callback<CancelReasonResponse>() {
                            @Override
                            public void onResponse(@NonNull Call<CancelReasonResponse> call, @NonNull retrofit2.Response<CancelReasonResponse> response) {
                                if(response.isSuccessful()) {
                                    if(response.body()!=null)
                                    {
                                        cancelReasonResponse(response.body().getData());

                                    }

                                }
                                else {
                                    try {
                                        Constants.displayError(response.errorBody().string(), context);
                                    } catch (IOException e) {
                                        throw new RuntimeException(e);
                                    }

                                }
                            }
                            @Override
                            public void onFailure(@NonNull Call<CancelReasonResponse> call, @NonNull Throwable t) {
                                t.printStackTrace();

                            }
                        });
                    } else {
                        Constants.InternetSettings(context);
                    }

                }catch (Exception e)
            {
                e.getMessage();
            }
            }catch (Exception e)
            {
                e.getMessage();
            }


    }
    }

    private void cancelReasonResponse(List<CancelReason> data) {
            reasons=new ArrayList<>();
        for (CancelReason cancelReason : data) {
            reasons.add(cancelReason.getReason());
        }

    }
}