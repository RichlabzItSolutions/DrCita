package com.drcita.user;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityViewreceiptBinding;
import com.drcita.user.models.systemcharges.SystemchargesResponse;
import com.drcita.user.models.viewreceipt.AppointmentDetails;
import com.drcita.user.models.viewreceipt.ViewreceiptRequest;
import com.drcita.user.models.viewreceipt.ViewreceiptResponse;
import com.drcita.user.retrofit.ApiClient;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ViewreceiptActivity extends LanguageBaseActivity {


    private ActivityViewreceiptBinding binding;


    private String userId;
    private int id;
    private ProgressDialog progress;
    private int slotNumber;
    private String dateRecipt;
    private String mobilenumber;
    boolean reciptFrom = false;
    private int position;
    private int payment;
    private String providerNumber;
    private int systemchargesindollar;
    private int systemchargesinslsh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_viewreceipt);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                id = extras.getInt("id");
                position = extras.getInt("position");
                payment = extras.getInt("payment",payment);
            }
        } else {
        }
        getUserAppointmentDetails(1);

        binding.homeLayout.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            startActivity(intent);
            finish();
        });
        binding.homeicon.setOnClickListener(view -> {
            Intent intent = new Intent(getApplicationContext(), DashBoardActivity.class);
            startActivity(intent);
            finish();
        });
        binding.supporticon.setOnClickListener(view -> {
            Intent callIntent = new Intent(Intent.ACTION_DIAL);
            callIntent.setData(Uri.parse("tel:" + providerNumber));
            startActivity(callIntent);
        });
        binding.bookmarkLayout.setOnClickListener(view -> takeScreenshot(1));
        binding.shareLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                takeScreenshot(2);
            }
        });

        Dexter.withContext(this)
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.MANAGE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport multiplePermissionsReport) {
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> list, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onResume() {
        super.onResume();
        systemcharges();

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
                    Toast.makeText(ViewreceiptActivity.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(ViewreceiptActivity.this);
        }
    }
    private void systemchargesResponse(SystemchargesResponse systemchargesResponse) {
        if(systemchargesResponse.getStatus().equals("success")){
            systemchargesindollar = systemchargesResponse.getDollar();
            systemchargesinslsh = systemchargesResponse.getSLSH();


        }
    }


    private void getUserAppointmentDetails(int position) {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            ViewreceiptRequest viewreceiptRequest = new ViewreceiptRequest();
            viewreceiptRequest.setUserId(Integer.parseInt(userId));
            viewreceiptRequest.setId(id);
            viewreceiptRequest.setAppointmentType(position);
            ApiClient.getRestAPI().viewReceipt(viewreceiptRequest).enqueue(new Callback<ViewreceiptResponse>() {
                @Override
                public void onResponse(@NonNull Call<ViewreceiptResponse> call, @NonNull retrofit2.Response<ViewreceiptResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        viewreceiptResponse(Objects.requireNonNull(response.body()),position);
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
                public void onFailure(@NonNull Call<ViewreceiptResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                }
            });
        } else {
            Constants.InternetSettings(ViewreceiptActivity.this);
        }
    }

    @SuppressLint("SetTextI18n")
    private void viewreceiptResponse(ViewreceiptResponse viewreceiptResponse, int position) {
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        mobilenumber = sp.getString(Constants.MOBILE, mobilenumber);
        dismissLoadingDialog();

        AppointmentDetails dataItems = viewreceiptResponse.getData();

         binding.patientname.setText(dataItems.getAppointment().getPatientName());
         binding.orderid.setText("Appointment # : "+dataItems.getAppointment().getAppointmentId() );
         binding.doctornamereceipt.setText(dataItems.getAppointment().getDoctorName());
         binding.receiptfee.setText(String.valueOf("₹"+dataItems.getAppointment().getConsultationFee()));

         if(dataItems.getAppointment().getCouponDiscount()>0){
             binding.llConsultation.setVisibility(VISIBLE);
            binding.coupon.setText(String.valueOf("-"+dataItems.getAppointment().getCouponDiscount()));
        }
         else {
             binding.llConsultation.setVisibility(GONE);

         }

         binding.tvAppointmentdate.setText("Appointment Date : " +dataItems.getAppointment().getSlotDate() +"\nAppointment Time : " +dataItems.getAppointment().getSlotTime());
            binding.slotdatereceipt.setText("Issued On: " + dataItems.getAppointment().getBookedOn());
            binding.receiptcharges.setText("₹"+dataItems.getAppointment().getTotalAmount());
            providerNumber = dataItems.getAppointment().getProviderMobile();

                binding.doctorhospital.setText(dataItems.getAppointment().getHospitalName());
                binding.receiptdoctorname.setText(dataItems.getAppointment().getDoctorName());
                binding.doctortitle.setText(getString(R.string.doctorname));
                binding.doctorinfotitle.setText(getString(R.string.doctorinfo));
                binding.receiptdoctorspecailazation.setText(dataItems.getAppointment().getSpecialisation());
                binding.doctorspecalization.setText(dataItems.getAppointment().getSpecialisation());
                binding.phonenumberreceipt.setText("Hospital No "+dataItems.getAppointment().getProviderMobile());
            binding.doctorlocation.setText(dataItems.getAppointment().getRegion());
            binding.patientslot.setText(dataItems.getAppointment().getPatientMobile());
            if(dataItems.getAppointment().getAppointmentMode().equals("1"))
            {
                binding.patientnumber.setText("Online Consultation");
            }
            else {
                binding.patientnumber.setText("In-Person Consultation");


            }

//            if(Integer.parseInt(dataItems.getAppointment().get())>0)
//            {
//                binding.llConsultation.setVisibility(VISIBLE);
//            }
//            else {
//                binding.llConsultation.setVisibility(GONE);
//            }


        }



    private void takeScreenshot(int i) {
        Uri externalUri = MediaStore.Files.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY);
        String relativeLocation = Environment.DIRECTORY_DOWNLOADS;

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Files.FileColumns.DISPLAY_NAME, dateRecipt+slotNumber+".jpg");
        contentValues.put(MediaStore.Files.FileColumns.MIME_TYPE, "image/jpeg");
        contentValues.put(MediaStore.Files.FileColumns.TITLE, "Test");
        contentValues.put(MediaStore.Files.FileColumns.DATE_ADDED, System.currentTimeMillis() / 1000);
        contentValues.put(MediaStore.Files.FileColumns.RELATIVE_PATH, relativeLocation);
        contentValues.put(MediaStore.Files.FileColumns.DATE_TAKEN, System.currentTimeMillis());
        Uri fileUri = getContentResolver().insert(externalUri, contentValues);
        try {
            binding.bottomBtnLayout.setVisibility(GONE);
            View v1 = binding.baseLayout;
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            v1.setDrawingCacheEnabled(false);
            OutputStream outputStream =  getContentResolver().openOutputStream(fileUri);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();
           // Toast.makeText(this, "Screenshot taken successfully", Toast.LENGTH_SHORT).show();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

//        Date now = new Date();
//        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//
//        try {
//            // image naming and path  to include sd card  appending name you choose for file
//            String dirpath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+"";
//            File file = new File(dirpath);
//            if (!file.exists()) {
//                boolean mkdir = file.mkdir();
//                Log.e("TAG", "takeScreenshot: " + mkdir);
//            }
//            CharSequence format = android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);
//            String mPath = dirpath + "/" + format + ".jpeg";
//
//            // create bitmap screen capture
//            View v1 = getWindow().getDecorView().getRootView();
//            v1.setDrawingCacheEnabled(true);
//            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
//            v1.setDrawingCacheEnabled(false);
//
//            File imageFile = new File(mPath);
//            Log.e("TAG", "takeScreenshot: " + imageFile.toString());
//
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            int quality = 100;
//            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
//            outputStream.flush();
//            outputStream.close();
//
        if(i==1){
            //nothing required
        } else {
            Intent share = new Intent(Intent.ACTION_SEND);
            share.setType("image/*");
            share.putExtra(Intent.EXTRA_STREAM, fileUri);
            startActivity(Intent.createChooser(share, "Share Image"));
            //TODO share screenshot
        }
        binding.bottomBtnLayout.setVisibility(VISIBLE);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or DOM
//            e.printStackTrace();
//        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}