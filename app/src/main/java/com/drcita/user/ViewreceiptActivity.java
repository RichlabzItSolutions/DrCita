package com.drcita.user;

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
import com.drcita.user.models.viewreceipt.ViewreceiptRequest;
import com.drcita.user.models.viewreceipt.ViewreceiptResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.snackbar.Snackbar;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.parceler.Parcels;

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
    private String CUSTOMER_CARE_MOBILE_NO = "+91-9491363419";
    private static final int REQUEST_PHONE_CALL = 100;
    private com.drcita.user.models.doctors.DataItem doctorData;
    private com.drcita.user.models.scans.DataItems doctorData1;
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
                doctorData = Parcels.unwrap(extras.getParcelable("doctorData"));
                doctorData1 = Parcels.unwrap(extras.getParcelable("doctorData1"));
                position = extras.getInt("position");
                payment = extras.getInt("payment",payment);
            }
        } else {
        }
        getUserAppointmentDetails(position);

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
                .withPermissions(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.MANAGE_EXTERNAL_STORAGE)
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
        String description = viewreceiptResponse.getMessage();
        if (viewreceiptResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            com.drcita.user.models.viewreceipt.Data dataItems = viewreceiptResponse.getData();
            binding.slotdatereceipt.setText("Slot Date:" + dataItems.getBookedOn());
            providerNumber = dataItems.getProviderMobile();
            if (position==2){
                binding.doctornamereceipt.setText(dataItems.getScanName());

                 if(dataItems.getPaymentMethod().matches("Free"))
                 {
                     binding.receiptcharges.setText("Free");
                     binding.receiptfee.setText("Free");
                 }
                 else {
                     if (dataItems.getRegion().matches("Mogadishu")) {
                         binding.receiptcharges.setText("$" + dataItems.getConsultationFee());
                         binding.receiptfee.setText("$" + dataItems.getConsultationFee());

                     }
                     else
                     {
                         binding.receiptcharges.setText("SLSH" + dataItems.getConsultationFee());
                         binding.receiptfee.setText("SLSH" + dataItems.getConsultationFee());
                     }

                 }


                binding.doctorhospital.setText(dataItems.getHospitalName());
                binding.receiptdoctorname.setText(dataItems.getScanName());
                binding.doctortitle.setText(getString(R.string.scanname));
                binding.doctorinfotitle.setText(getString(R.string.scaninfo));
                binding.phonenumberreceipt.setText("Provider Number "+dataItems.getProviderMobile());
                binding.specializationLayout.setVisibility(View.GONE);
                binding.doctorspecalization.setVisibility(View.INVISIBLE);
            }else {
                binding.doctornamereceipt.setText(dataItems.getDoctorName());


                if(dataItems.getPaymentMethod().matches("Free"))
                {
                    binding.receiptcharges.setText("Free");
                    binding.receiptfee.setText("Free");
                }
                else {
                    if (dataItems.getRegion().matches("Mogadishu")) {
                        binding.receiptcharges.setText("$" + dataItems.getConsultationFee());
                        binding.receiptfee.setText("$" + dataItems.getConsultationFee());

                    }
                    else
                    {
                        binding.receiptcharges.setText("SLSH" + dataItems.getConsultationFee());
                        binding.receiptfee.setText("SLSH" + dataItems.getConsultationFee());
                    }

                }
                binding.doctorhospital.setText(dataItems.getHospitalName());
                binding.receiptdoctorname.setText(dataItems.getDoctorName());
                binding.doctortitle.setText(getString(R.string.doctorname));
                binding.doctorinfotitle.setText(getString(R.string.doctorinfo));
                binding.receiptdoctorspecailazation.setText(dataItems.getSpecialisation());
                binding.doctorspecalization.setText(dataItems.getSpecialisation());
                binding.phonenumberreceipt.setText("Provider Number "+dataItems.getProviderMobile());

                /*binding.specializationLayout.setVisibility(View.VISIBLE);
                binding.doctorspecalization.setVisibility(View.VISIBLE);*/
            }
            binding.paymentService.setText(dataItems.getPaymentMethod());
            binding.doctorlocation.setText(dataItems.getRegion());
            binding.patientname.setText(dataItems.getPatientName());
            binding.patientslot.setText(getString(R.string.number)+": " + dataItems.getSlotNumber());
            binding.orderid.setText(getString(R.string.cardno) + dataItems.getSlotNumber());
           // binding.phonenumberreceipt.setText(dataItems.getProviderMobile());
            binding.slotdatereceipt.setText(dataItems.getSlotDate());
           // dateRecipt = dataItems.getSlotDate();
           // slotNumber = dataItems.getSlotNumber();
        } else {
            Snackbar.make(findViewById(android.R.id.content), description, Snackbar.LENGTH_SHORT).show();
        }
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
            binding.bottomBtnLayout.setVisibility(View.GONE);
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
        binding.bottomBtnLayout.setVisibility(View.VISIBLE);
//        } catch (Throwable e) {
//            // Several error may come out with file handling or DOM
//            e.printStackTrace();
//        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, DashBoardActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
    }
}