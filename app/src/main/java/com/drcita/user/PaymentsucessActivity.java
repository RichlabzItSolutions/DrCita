package com.drcita.user;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.databinding.ActivityPaymentsucessBinding;
import com.drcita.user.models.appointment.AppointmentData;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentsucessActivity extends LanguageBaseActivity {

    private ActivityPaymentsucessBinding binding;
    private AppointmentData doctorData;
//    private com.drcita.user.models.scans.DataItems doctorData1;
    private int slotNo;
    private String date;
    private int id;
    private int payment;
    private int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_paymentsucess);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                doctorData = Parcels.unwrap(extras.getParcelable("doctorData"));
                slotNo = extras.getInt("slotNo");
                position = extras.getInt("position");
                id = extras.getInt("id");
                 payment = extras.getInt("payment");

            }
        }
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy"); // or "dd-MM-yyyy"
        date = sdf.format(new Date());
        String strCurrentDate = "dd-mm-YYYY";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        binding.tvDetails.setText("Appointment ID #"+doctorData.getAppointmentNum()+"\nSlot time      : "+ doctorData.getSlotTime());

        format = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = format.format(newDate);
        if (position==2) {
            binding.viewreceiptBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), ViewreceiptActivity.class);
                intent.putExtra("id", doctorData.getAppointmentId());
                intent.putExtra("doctorData1", Parcels.wrap(doctorData));
                intent.putExtra("position",2);
                 intent.putExtra("payment",payment);
                intent.putExtra("from",1);
                startActivity(intent);
            });
        }else {
            binding.viewreceiptBtn.setOnClickListener(view -> {
                Intent intent = new Intent(getApplicationContext(), ViewreceiptActivity.class);
                intent.putExtra("doctorData", Parcels.wrap(doctorData));
                intent.putExtra("id", doctorData.getAppointmentId());
                intent.putExtra("position",1);
                 intent.putExtra("from",1);
                startActivity(intent);
            });
        }
       binding.btnBackHome.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
               intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
               startActivity(intent);
               finish();
           }
       });
    }

   @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}