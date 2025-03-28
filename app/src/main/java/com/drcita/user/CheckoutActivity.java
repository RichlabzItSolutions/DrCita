package com.drcita.user;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityCheckoutBinding;
import com.drcita.user.models.GlobalResponse;
import com.drcita.user.models.payment.PaymentRequest;
import com.drcita.user.models.systemcharges.SystemchargesResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.card.MaterialCardView;
import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CheckoutActivity extends LanguageBaseActivity {

    private ActivityCheckoutBinding binding;
    private com.drcita.user.models.doctors.DataItem doctorData;
    private com.drcita.user.models.scans.DataItems doctorData1;
    private int slotNo,id,position;
    private String date;
    private ProgressDialog progress;
    private String userId;
    boolean isFromList = false;
    private String mobile;
    private int systemchargesindollar;
    private int systemchargesinslsh;
    private String type;
    private String currencytype;


    @SuppressLint({"SetTextI18n", "ResourceAsColor"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_checkout);
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            userId = sp.getString(Constants.USER_ID, userId);
            mobile = sp.getString(Constants.MOBILE, mobile);
            if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                isFromList = extras.getBoolean(Constants.list);
                doctorData = Parcels.unwrap(extras.getParcelable("doctorData"));
                doctorData1 = Parcels.unwrap(extras.getParcelable("doctorData1"));
                slotNo = extras.getInt("slotNo");
                id = extras.getInt("id");
                date = extras.getString("date");
                position = extras.getInt("position");
                type=extras.getString("type");
            }
        } else { }
            //systemcharges();
            binding.backCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
            if (position ==2) {
                binding.doctorname.setText(doctorData1.getScanName());
                if (!doctorData1.getPicture().isEmpty()) {
                    Picasso.with(this).load(doctorData1.getPicture())
                            .into(binding.doctorprofileimg);
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
                binding.datecheckout.setText(date1);

                binding.checkoutslotno.setVisibility(View.GONE);
                binding.checkoutslotno.setText("" + slotNo);
                binding.checkoutconsultationfee.setText(doctorData1.getConsultationCharges());
                currencytype = sp.getString(Constants.Currancy_Type, currencytype);
                if(currencytype!=null) {
                    if (currencytype.equals("1")) {
                        binding.checkoutcommission.setText("₹ " + " " + doctorData1.getCommission());
                        binding.llCurrencytype1.setVisibility(View.VISIBLE);
                        binding.llCurrencytype2.setVisibility(View.GONE);
                    } else {
                        binding.llCurrencytype1.setVisibility(View.GONE);
                        binding.llCurrencytype2.setVisibility(View.VISIBLE);
                        binding.checkoutcommission.setText("$ " +doctorData1.getCommission());
                    }
                }


                binding.specialzation.setText(doctorData1.getHospitalName());
                if (doctorData1.getConsultationCharges().equals("0")) {
                    binding.continueBtn.setVisibility(View.VISIBLE);
                    binding.layoutLL.setVisibility(View.GONE);
                    binding.noteDataTV.setVisibility(View.GONE);
                    binding.commssionlayout.setVisibility(View.GONE);

                } else {
                    binding.continueBtn.setVisibility(View.GONE);


                    binding.layoutLL.setVisibility(View.VISIBLE);
                    binding.commssionlayout.setVisibility(View.VISIBLE);

                }



                binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startPayment(3, mobile);

                    }
                });
/* addSound();
                    Intent intent = new Intent(getApplicationContext(),PaymentsucessActivity.class);
                    intent.putExtra("doctorData1", Parcels.wrap(doctorData1));
                    intent.putExtra("id",id);
                    intent.putExtra("slotNo",slotNo);
                    intent.putExtra("date",date);
                    intent.putExtra("position",2);
                    //intent.putExtra("payment",i);
                    startActivity(intent);*//*

                 */
/*Intent intent = new Intent(getApplicationContext(), ViewreceiptActivity.class);
                    intent.putExtra("id", id);
                    intent.putExtra("doctorData1", Parcels.wrap(doctorData1));
                    intent.putExtra("position",2);
                    // intent.putExtra("payment",payment);
                    startActivity(intent);*//*

                }
            });
*/
            }else {
                binding.doctorname.setText(doctorData.firstName +" "+ doctorData.getLastName());
            if (!doctorData.getPicture().isEmpty()) {
            Picasso.with(this).load(doctorData.getPicture())
                    .into(binding.doctorprofileimg);
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
            binding.datecheckout.setText(date1);
            binding.checkoutslotno.setText("" + slotNo);

                currencytype = sp.getString(Constants.Currancy_Type, currencytype);
                if(currencytype!=null) {
                    if (currencytype.equals("1")) {
                        binding.checkoutconsultationfee.setText("₹ " + " " + doctorData.consultationCharges);
                        binding.llCurrencytype1.setVisibility(View.VISIBLE);
                        binding.llCurrencytype2.setVisibility(View.GONE);
                    } else {
                        binding.llCurrencytype1.setVisibility(View.GONE);
                        binding.llCurrencytype2.setVisibility(View.VISIBLE);
                        binding.checkoutconsultationfee.setText("$ " + doctorData.consultationCharges);

                    }
                }



                binding.noteDataTV.setVisibility(View.GONE);
          //  binding.checkoutcommission.setVisibility(View.GONE);
            binding.commssionlayout.setVisibility(View.GONE);
            binding.specialzation.setText(doctorData.getSpecialisationName());
           /* Log.d("consulationcharges", ""+doctorData.getConsultationCharges());
                Log.d("consulationcharges1", ""+doctorData1.getConsultationCharges());
                Toast.makeText(getApplicationContext(), "consulationcharges1"+doctorData1.getConsultationCharges(), Toast.LENGTH_SHORT).show();

                Toast.makeText(getApplicationContext(), "consulationcharges"+doctorData.getConsultationCharges(), Toast.LENGTH_SHORT).show();
            */    if (doctorData.consultationCharges.equals("0") ){
                    binding.continueBtn.setVisibility(View.VISIBLE);
                    binding.noteDataTV.setVisibility(View.GONE);
                    binding.layoutLL.setVisibility(View.GONE);
                }
                else {
                    binding.noteDataTV.setVisibility(View.VISIBLE);
                    binding.continueBtn.setVisibility(View.GONE);
                    binding.layoutLL.setVisibility(View.VISIBLE);
                }
                binding.continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                       // addSound();
                        startPayment(3,mobile);

                       /* Intent intent = new Intent(getApplicationContext(),PaymentsucessActivity.class);
                        intent.putExtra("doctorData", Parcels.wrap(doctorData));
                        intent.putExtra("doctorData1", Parcels.wrap(doctorData1));
                        intent.putExtra("id",id);
                        intent.putExtra("slotNo",slotNo);
                        intent.putExtra("date",date);
                        intent.putExtra("position",1);
                        //intent.putExtra("payment",i);
                        startActivity(intent);*/
                        /*Intent intent = new Intent(getApplicationContext(), ViewreceiptActivity.class);
                        intent.putExtra("doctorData", Parcels.wrap(doctorData));
                        intent.putExtra("id", id);
                        intent.putExtra("position",1);
                        // intent.putExtra("payment",payment);
                        startActivity(intent);*/
                    }
                });
        }
     /*   if (mobile.startsWith("63")){
            binding.zaadpaymentbtn.setEnabled(true);
         //   binding.edahabpayment.setEnabled(false);
         //   binding.edahabpaymentbtn.setEnabled(false);
         //   binding.edahabpayment.setBackgroundColor(R.color.browser_actions_bg_grey);
        }else if(mobile.startsWith("65")) {
            binding.edahabpaymentbtn.setEnabled(true);
            binding.zaadpaymentbtn.setEnabled(false);
            binding.zaadpayment.setEnabled(false);
            binding.zaadpaymentbtn.setBackgroundColor(R.color.browser_actions_bg_grey);
        }*/
        binding.zaadpaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.startsWith("63")){
                    shoPopup(1);
                }else if (mobile.startsWith("65")){
                    showPopupedaahab(1);
                }
            }
        });

        binding.evcpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.startsWith("61") || mobile.startsWith("77")){
                    shoPopup(4);
                }else {
                    showPopupedaahab(4);
                }
            }
        });
        binding.edahabpaymentbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.startsWith("65")){
                    shoPopup(2);
                   // startPayment(2);
                }else if (mobile.startsWith("63")){
                    showPopupedaahab(2);
                }
            }
        });

        binding.edahabpaymentbtn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.startsWith("62")){
                    shoPopup(5);
                    // startPayment(2);
                }
            }
        });

        binding.premierwallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    shoPopup(6);
                    // startPayment(2);
                }

        });
        binding.edahabpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.startsWith("65")){
                    shoPopup(2);
                    // startPayment(2);
                }
                else if (mobile.startsWith("63")){
                    showPopupedaahab(2);
                }
            }
        });
        binding.zaadpayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mobile.startsWith("63")){
                    shoPopup(1);
                }else if (mobile.startsWith("65")){
                    showPopupedaahab(1);
                }
            }
        });

        if(type.matches("2"))
        {
            binding.noteDataTV.setVisibility(View.VISIBLE);

        }
        else
        {

            binding.noteDataTV.setVisibility(View.GONE);


        }
        systemcharges();

      //  binding.checkoutsystemcharges.setText(doctorData.);
    }


    private void systemcharges() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ApiClient.getRestAPI().systemCharges().enqueue(new Callback<SystemchargesResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<SystemchargesResponse> call, @NonNull Response<SystemchargesResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        systemchargesResponse(Objects.requireNonNull(response.body()));
                    } else {
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<SystemchargesResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(CheckoutActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(CheckoutActivity.this);
        }
    }

    private void systemchargesResponse(SystemchargesResponse systemchargesResponse) {
        if(systemchargesResponse.getStatus().equals("success")){
            systemchargesindollar = systemchargesResponse.getDollar();
            systemchargesinslsh = systemchargesResponse.getSLSH();
        }
    }

    private void showPopupedaahab(int i) {
        Dialog dialog = new Dialog(this,R.style.caafisomdailogtheme);
        dialog.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.payment_change_popup, null);
        dialog.setContentView(view);
        TextView numberTV,changeTV,paymentext,cancelTV,noteTV;
        EditText patientnumberET;
        Button continueBtn;
        MaterialCardView numberCard;
        numberTV = view.findViewById(R.id.numberTV);
        continueBtn = view.findViewById(R.id.continueBtn);
        numberCard = view.findViewById(R.id.numberCard);
        changeTV = view.findViewById(R.id.changeTV);
        cancelTV = view.findViewById(R.id.cancelTV);
        noteTV = view.findViewById(R.id.noteTV);
        patientnumberET = view.findViewById(R.id.patientnumberET);
        paymentext = view.findViewById(R.id.paymentext);
        paymentext.setText(R.string.proceedpayment);
        numberTV.setVisibility(View.GONE);
        numberCard.setVisibility(View.VISIBLE);
        changeTV.setVisibility(View.GONE);
        cancelTV.setVisibility(View.GONE);
       // noteTV.setVisibility(View.GONE);
        if (i==1) {
            noteTV.setText(R.string.notezaad);
        }else  if(i==2){
            noteTV.setText(R.string.noteeddahab);
        }
        else if(i==4)
        {
            noteTV.setText(R.string.evc);

        }
        else if(i==5)
        {
            noteTV.setText(R.string.evczeddahab);

        }
        else if(i==6)
        {
            noteTV.setText(R.string.ezaad);
        }


        continueBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (patientnumberET.getText().toString().equals("")) {
                            Constants.ToastShort(CheckoutActivity.this, "Enter Phone Number");
                            return;
                        }
                        if (i==2){
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternedaahab))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;
                            }
                        }else if (i==1) {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePattern))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;
                            }
                        }
                        else if(i==4)
                        {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc)) &&(!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc1)))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;
                            }
                        }

                        else if(i==5) {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevcezad))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;

                            }
                        }
                        else if(i==6) {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc)) &&(!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc1)) &&(!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevcezad))))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;

                            }
                        }

                        startPayment(i,patientnumberET.getText().toString());
                    }
                });
       // numberTV.setText(mobile);
        dialog.show();
    }
    private void shoPopup(int i) {
        Dialog dialog = new Dialog(this,R.style.caafisomdailogtheme);
        dialog.setCancelable(true);
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.payment_change_popup, null);
        dialog.setContentView(view);
        TextView numberTV,changeTV,paymentext,cancelTV,noteTV;
        EditText patientnumberET;
        Button continueBtn;
        MaterialCardView numberCard;
        numberTV = view.findViewById(R.id.numberTV);
        continueBtn = view.findViewById(R.id.continueBtn);
        numberCard = view.findViewById(R.id.numberCard);
        changeTV = view.findViewById(R.id.changeTV);
        cancelTV = view.findViewById(R.id.cancelTV);
        patientnumberET = view.findViewById(R.id.patientnumberET);
        noteTV = view.findViewById(R.id.noteTV);
        paymentext = view.findViewById(R.id.paymentext);
        paymentext.setText(R.string.proceedpayment);
        cancelTV.setVisibility(View.GONE);
        noteTV.setVisibility(View.GONE);
        if (i==2){
            int maxLength = 9;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            patientnumberET.setFilters(FilterArray);
        }else {
            int maxLength = 9;
            InputFilter[] FilterArray = new InputFilter[1];
            FilterArray[0] = new InputFilter.LengthFilter(maxLength);
            patientnumberET.setFilters(FilterArray);
        }
        cancelTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                numberTV.setVisibility(View.VISIBLE);
                numberCard.setVisibility(View.GONE);
                noteTV.setVisibility(View.GONE);
                changeTV.setVisibility(View.VISIBLE);
                cancelTV.setVisibility(View.GONE);
            }
        });
        continueBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!patientnumberET.isShown()) {
                        startPayment(i,numberTV.getText().toString());
                    }else {
                        if (patientnumberET.getText().toString().equals("")) {
                            Constants.ToastShort(CheckoutActivity.this, "Enter Phone Number");
                            return;
                        }
                        if (i == 2) {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternedaahab))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;
                            }
                        } else if (i == 1) {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaad))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;
                            }

                        }

                        else if(i==4)
                        {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc)) &&(!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc1)))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;
                            }
                        }

                        else if(i==5) {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevcezad))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;

                            }
                        }

                        else if(i==6) {
                            if (!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc)) &&(!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevc1)) &&(!(patientnumberET.getText().toString().matches(Constants.MobilePatternzaadevcezad))))) {
                                Constants.ToastShort(CheckoutActivity.this, Constants.enter_valid_mobile_no);
                                return;

                            }
                        }

                            startPayment(i, patientnumberET.getText().toString());

                }
            }
        });
        changeTV.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    numberTV.setVisibility(View.GONE);
                    numberCard.setVisibility(View.VISIBLE);
                    cancelTV.setVisibility(View.VISIBLE);
                    noteTV.setVisibility(View.VISIBLE);
                    changeTV.setVisibility(View.GONE);
                    if (i==1) {
                        //noteTV.setVisibility(View.GONE);
                         noteTV.setText(R.string.notezaad);
                    }else if(i==2) {
                        noteTV.setText(R.string.noteeddahab);
                    }

                    else if(i==4)
                    {
                        noteTV.setText(R.string.evc);

                    }
                    else if(i==5)
                    {
                        noteTV.setText(R.string.evczeddahab);

                    }
                    else if(i==6)
                    {
                        noteTV.setText(R.string.ezaad);
                    }
                }


            });
        numberTV.setText(mobile);
        dialog.show();
    }
    private void startPayment(int i, String mobile) {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            PaymentRequest request = new PaymentRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setId(id);
            request.setAppointmentType(position);
            request.setPaymentGatewayType(i);
            request.setMobile(mobile);
            ApiClient.getRestAPI().payNow(request).enqueue(new Callback<GlobalResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GlobalResponse> call, @NonNull Response<GlobalResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        payNowResponse(i,Objects.requireNonNull(response.body()));
                    } else {
                        try {
                            Constants.displayError(response.errorBody().string(), getBaseContext());
                            Intent intent = new Intent(getApplicationContext(), PaymentFailureActivity.class);
                            startActivity(intent);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        dismissLoadingDialog();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<GlobalResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(CheckoutActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(CheckoutActivity.this);
        }
    }
    private void payNowResponse(int i, GlobalResponse globalResponse) {
        dismissLoadingDialog();
        Toast.makeText(CheckoutActivity.this, globalResponse.getMessage(), Toast.LENGTH_LONG).show();
        addSound();
        Intent intent = new Intent(getApplicationContext(),PaymentsucessActivity.class);
        intent.putExtra("doctorData", Parcels.wrap(doctorData));
        intent.putExtra("doctorData1", Parcels.wrap(doctorData1));
        intent.putExtra("id",id);
        intent.putExtra("slotNo",slotNo);
        intent.putExtra("date",date);
        intent.putExtra("position",position);
        intent.putExtra("payment",i);
        startActivity(intent);
    }
    private void addSound() {
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}