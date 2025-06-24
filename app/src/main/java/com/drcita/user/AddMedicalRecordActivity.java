package com.drcita.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.drcita.user.adapter.medical.ImageAdapter;
import com.drcita.user.adapter.medical.PdfAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityAddNewrecordBinding;
import com.drcita.user.models.restresponse.RestResponse;
import com.drcita.user.retrofit.ApiClient;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.yalantis.ucrop.UCrop;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AddMedicalRecordActivity extends LanguageBaseActivity {
    ActivityAddNewrecordBinding binding;
    private List<File> imageFiles = new ArrayList<>();
    private List<File> pdfFiles = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private static final int PICK_IMAGE = 101;
    private static final int PICK_PDF = 102;

    private ImageAdapter imageAdapter;
    private PdfAdapter pdfAdapter;
    private String Id;
    private String subuserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_newrecord);

        binding.backHospitalsList.setOnClickListener(view -> {
            if (!imageFiles.isEmpty() || !pdfFiles.isEmpty()) {
                showExitBottomSheet();
            } else {
               navigateToMedicalRecords();
               finish();
            }
        });

        binding.btnAddPhoto.setOnClickListener(v -> pickImage());
        binding.btnAddPdf.setOnClickListener(v -> pickPdf());
        binding.btnSubmit.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(binding.etURL.getText())) {
                urls.add(binding.etURL.getText().toString());
            }
            uploadData();
        });
         if(getIntent()!=null)
         {
             subuserId = getIntent().getStringExtra("subuserId");
         }

        setupAdapters();
    }

    private void setupAdapters() {
        imageAdapter = new ImageAdapter(imageFiles, file -> {
            imageFiles.remove(file);
            imageAdapter.notifyDataSetChanged();
        });
        binding.rvImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.rvImage.setAdapter(imageAdapter);

        pdfAdapter = new PdfAdapter(pdfFiles, file -> {
            pdfFiles.remove(file);
            pdfAdapter.notifyDataSetChanged();
        });
        binding.rvPdf.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPdf.setAdapter(pdfAdapter);
    }

    private void pickImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select Images"), PICK_IMAGE);
    }

    private void pickPdf() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        startActivityForResult(Intent.createChooser(intent, "Select PDFs"), PICK_PDF);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data != null) {
            if (requestCode == PICK_IMAGE) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri sourceUri = data.getClipData().getItemAt(i).getUri();
                        startCrop(sourceUri);
                    }
                } else if (data.getData() != null) {
                    Uri sourceUri = data.getData();
                    startCrop(sourceUri);
                }
            } else if (requestCode == UCrop.REQUEST_CROP) {
                Uri resultUri = UCrop.getOutput(data);
                if (resultUri != null) {
                    File file = new File(resultUri.getPath());
                    imageFiles.add(file);
                    imageAdapter.notifyDataSetChanged();
                }
            } else if (requestCode == PICK_PDF) {
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri pdfUri = data.getClipData().getItemAt(i).getUri();
                        File copiedFile = copyUriToTempFile(pdfUri, "pdf_" + System.currentTimeMillis() + ".pdf");
                        if (copiedFile != null && isFileSizeValid(copiedFile)) {
                            pdfFiles.add(copiedFile);
                        } else {
                            Toast.makeText(this, "Each PDF must be less than 5 MB", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else if (data.getData() != null) {
                    Uri pdfUri = data.getData();
                    File copiedFile = copyUriToTempFile(pdfUri, "pdf_" + System.currentTimeMillis() + ".pdf");
                    if (copiedFile != null && isFileSizeValid(copiedFile)) {
                        pdfFiles.add(copiedFile);
                    } else {
                        Toast.makeText(this, "PDF file must be less than 5 MB", Toast.LENGTH_SHORT).show();
                    }
                }
                pdfAdapter.notifyDataSetChanged();
            }
        }
    }


        private boolean isFileSizeValid(File file) {
            long maxSizeInBytes = 5 * 1024 * 1024; // 5 MB
            return file.length() <= maxSizeInBytes;
        }


    private void startCrop(Uri sourceUri) {
        Uri destUri = Uri.fromFile(new File(getCacheDir(), System.currentTimeMillis() + "_cropped.jpg"));
        UCrop.of(sourceUri, destUri)
                .withAspectRatio(1, 1)
                .start(this);
    }

    private File copyUriToTempFile(Uri uri, String fileName) {
        try {
            File tempFile = new File(getCacheDir(), fileName);
            InputStream inputStream = getContentResolver().openInputStream(uri);
            if (inputStream == null) return null;

            FileOutputStream outputStream = new FileOutputStream(tempFile);

            byte[] buffer = new byte[1024];
            int length;
            while ((length = inputStream.read(buffer)) > 0) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
            return tempFile;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private void uploadData() {
        try {
            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String userIdPref = sp.getString(Constants.USER_ID, ""); // Use default value if null

            // Step 1: Create JSON data object
            JSONObject jsonData = new JSONObject();
            try {
                jsonData.put("userId", userIdPref);
                jsonData.put("subUserId", subuserId != null ? subuserId : "");
                jsonData.put("appointmentId", 1); // Replace with your actual appointmentId if needed
                jsonData.put("title", binding.etFullname.getText().toString());
                jsonData.put("reason", binding.etAddress.getText().toString());

                JSONArray urlsArray = new JSONArray();
                for (String url : urls) {
                    urlsArray.put(url);
                }
                jsonData.put("urls", urlsArray);

            } catch (JSONException e) {
                e.printStackTrace();
                return;
            }

            // Step 2: Convert JSON to RequestBody & wrap as MultipartBody.Part
            RequestBody jsonRequestBody = RequestBody.create(
                    MediaType.parse("application/json"), jsonData.toString());
            MultipartBody.Part dataPart = MultipartBody.Part.createFormData("data", null, jsonRequestBody);

            // Step 3: Prepare image parts
            List<MultipartBody.Part> imageParts = new ArrayList<>();
            for (File image : imageFiles) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), image);
                imageParts.add(MultipartBody.Part.createFormData("images[]", image.getName(), fileBody));
            }

            // Step 4: Prepare PDF parts
            List<MultipartBody.Part> pdfParts = new ArrayList<>();
            for (File pdf : pdfFiles) {
                RequestBody fileBody = RequestBody.create(MediaType.parse("application/pdf"), pdf);
                pdfParts.add(MultipartBody.Part.createFormData("pdfs[]", pdf.getName(), fileBody));
            }

            // Step 5: Show loading and check internet
            showLoadingDialog();
            if (Constants.haveInternet(getApplicationContext())) {
                ApiClient.getRestAPI().uploadMedicalRecord(
                        dataPart,
                        imageParts,
                        pdfParts
                ).enqueue(new Callback<RestResponse>() {
                    @Override
                    public void onResponse(@NonNull Call<RestResponse> call, @NonNull retrofit2.Response<RestResponse> response) {
                        dismissLoadingDialog();
                        if (response.isSuccessful() && response.body() != null) {
                            Toast.makeText(AddMedicalRecordActivity.this, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            imageFiles.clear();
                             pdfFiles.clear();
                        } else {
                            Constants.displayError(response.errorBody().toString(), AddMedicalRecordActivity.this);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<RestResponse> call, @NonNull Throwable t) {
                        dismissLoadingDialog();
                        t.printStackTrace();
                    }
                });
            } else {
                Constants.InternetSettings(AddMedicalRecordActivity.this);
            }

        } catch (Exception e) {
            e.getMessage();
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (!imageFiles.isEmpty() || !pdfFiles.isEmpty()) {
            showExitBottomSheet(); // Ask confirmation if files are selected
        } else {
            navigateToMedicalRecords();
        }
    }
    private void showExitBottomSheet() {
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        View view = LayoutInflater.from(this).inflate(R.layout.bottomsheet_exit_confirm, null);
        bottomSheetDialog.setContentView(view);

        Button btnConfirm = view.findViewById(R.id.btnConfirmExit);
        Button btnCancel = view.findViewById(R.id.btnCancelExit);

        btnConfirm.setOnClickListener(v -> {
            bottomSheetDialog.dismiss();

            // Optionally clear lists
            imageFiles.clear();
            pdfFiles.clear();

            navigateToMedicalRecords();
        });

        btnCancel.setOnClickListener(v -> bottomSheetDialog.dismiss());

        bottomSheetDialog.show();
    }

    private void navigateToMedicalRecords() {
        Intent intent = new Intent(this, MedicalRecordsActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}