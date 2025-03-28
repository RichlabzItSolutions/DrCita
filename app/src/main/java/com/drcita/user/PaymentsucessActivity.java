package com.drcita.user;

import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.drcita.user.databinding.ActivityPaymentsucessBinding;

import org.parceler.Parcels;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PaymentsucessActivity extends LanguageBaseActivity {

    private ActivityPaymentsucessBinding binding;
    private com.drcita.user.models.doctors.DataItem doctorData;
    private com.drcita.user.models.scans.DataItems doctorData1;
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
                doctorData1 = Parcels.unwrap(extras.getParcelable("doctorData1"));
                slotNo = extras.getInt("slotNo");
                date = extras.getString("date");
                position = extras.getInt("position");
                id = extras.getInt("id");

                 payment = extras.getInt("payment");

            }
        } else {
        }
        String strCurrentDate = "dd-mm-YYYY";
        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = null;
        try {
            newDate = format.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        format = new SimpleDateFormat("dd-MM-yyyy");
        String date1 = format.format(newDate);
        if (position==2) {
            binding.paymentsucessname.setText(doctorData1.getScanName() + " "+ "on" +" "+ ""+date1);

            binding.paymentsucessslotno.setVisibility(View.GONE);
            binding.paymentsucessslotno.setText("Number: "+" "+slotNo);
            binding.viewreceiptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViewreceiptActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("doctorData1", Parcels.wrap(doctorData1));
                    intent.putExtra("position",2);
                     intent.putExtra("payment",payment);
                    startActivity(intent);
                }
            });
        }else {
            binding.paymentsucessname.setText(doctorData.firstName + doctorData.lastName+  " "+ "on" +" "+ ""+date1);
            binding.paymentsucessslotno.setText("Number: "+" "+slotNo);
            binding.viewreceiptBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(getApplicationContext(), ViewreceiptActivity.class);
                    intent.putExtra("doctorData", Parcels.wrap(doctorData));
                    intent.putExtra("id", id);
                    intent.putExtra("position",1);
                    // intent.putExtra("payment",payment);
                    startActivity(intent);
                }
            });
        }
    }

   @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}