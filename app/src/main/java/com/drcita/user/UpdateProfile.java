package com.drcita.user;

import static android.content.ContentValues.TAG;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.canhub.cropper.CropImageContract;
import com.canhub.cropper.CropImageContractOptions;
import com.canhub.cropper.CropImageOptions;
import com.canhub.cropper.CropImageView;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityUpdateProfileBinding;
import com.drcita.user.models.profile.GetProfileRequest;
import com.drcita.user.models.profile.GetProfileResponse;
import com.drcita.user.models.profile.UpdateNameRequest;
import com.drcita.user.models.profile.UpdateNameResponse;
import com.drcita.user.models.profile.UploadPhotoRequest;
import com.drcita.user.retrofit.ApiClient;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends LanguageBaseActivity {
    public ActivityUpdateProfileBinding activityUpdateProfileBinding;
    private ActivityResultLauncher<Intent> pickFileLauncher,cropLauncher;
    Uri resultUri;
     MultipartBody.Part multiPartBody;
    public String userId, name, mobile;
    ActivityResultLauncher<CropImageContractOptions> crop;
    public final String[] permissions = new String[]{android.Manifest.permission.CAMERA,
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};
    ImageView selectedImageView;
     File file;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpdateProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        name = sp.getString(Constants.NAME, name);
        mobile = sp.getString(Constants.MOBILE, mobile);

       /* setSupportActionBar(activityUpdateProfileBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Update Profile");*/
        activityUpdateProfileBinding.backUpdateprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        //  activityUpdateProfileBinding.nameUpdate.setText(name);
        //   activityUpdateProfileBinding.updateNumber.setText(mobile);
        activityUpdateProfileBinding.roundedimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                selectedImageView=activityUpdateProfileBinding.roundedimg;
                int result = ContextCompat.checkSelfPermission(UpdateProfile.this, Manifest.permission.CAMERA);
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(UpdateProfile.this, R.string.camera_permission_desc, Toast.LENGTH_SHORT).show();
                    final Handler handler = new Handler();
                    checkPermission();
                } else {
                    crop.launch(new CropImageContractOptions(null, new CropImageOptions()));
                }
            }
        });

        registerResult();

        activityUpdateProfileBinding.updateprofileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (activityUpdateProfileBinding.nameUpdate.getText().toString().equals("")) {
                    Constants.ToastShort(UpdateProfile.this, "Enter Name");
                    return;
                }
                if (activityUpdateProfileBinding.nameUpdate.getText().toString().length() < 3) {
                    Constants.ToastShort(UpdateProfile.this, "Name length should be 3");
                    return;
                }
                updateProfileName();
            }
        });

    }

    @Override
    protected void onResume() {
        getUserProfile();
        super.onResume();
    }

    private void updateProfileName() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            UpdateNameRequest request = new UpdateNameRequest();
            request.setUserId(Integer.parseInt(userId));
            request.setName(activityUpdateProfileBinding.nameUpdate.getText().toString());
            ApiClient.getRestAPI().updateName(request).enqueue(new Callback<UpdateNameResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<UpdateNameResponse> call, @NonNull Response<UpdateNameResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        updateNameResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<UpdateNameResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(UpdateProfile.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(UpdateProfile.this);
        }
    }

    public void updateNameResponse(UpdateNameResponse updateNameResponse) {
        if (updateNameResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            Log.d("TAG", "signupResponse: " + updateNameResponse.getMessage());
            Toast.makeText(UpdateProfile.this, updateNameResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void getUserProfile() {
        showLoadingDialog();
        if (Constants.haveInternet(getApplicationContext())) {
            GetProfileRequest request = new GetProfileRequest();
            request.setUserId(Integer.parseInt(userId));
            ApiClient.getRestAPI().getUserProfile(request).enqueue(new Callback<GetProfileResponse>() {
                @RequiresApi(api = Build.VERSION_CODES.N)
                @Override
                public void onResponse(@NonNull Call<GetProfileResponse> call, @NonNull Response<GetProfileResponse> response) {
                    if (response.isSuccessful()) {
                        dismissLoadingDialog();
                        getProfileResponse(Objects.requireNonNull(response.body()));
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
                public void onFailure(@NonNull Call<GetProfileResponse> call, @NonNull Throwable t) {
                    dismissLoadingDialog();
                    t.printStackTrace();
                    Toast.makeText(UpdateProfile.this, "Server internal error try again", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Constants.InternetSettings(UpdateProfile.this);
        }
    }

    private void getProfileResponse(GetProfileResponse getProfileResponse) {
        activityUpdateProfileBinding.updateNumber.setText(getProfileResponse.getData().getMobile());
        activityUpdateProfileBinding.nameUpdate.setText(getProfileResponse.getData().getName());
        if (getProfileResponse.getData().getPicture()!=null && !getProfileResponse.getData().getPicture().isEmpty()) {
            Picasso.with(this).load(getProfileResponse.getData().getPicture())
                    .into(activityUpdateProfileBinding.roundedimg);
            activityUpdateProfileBinding.roundedimg.setImageTintList(null);

        }else {
            activityUpdateProfileBinding.roundedimg.setImageDrawable(getDrawable(R.drawable.avtar));
            activityUpdateProfileBinding.roundedimg.setColorFilter(Color.argb(255,255,255,255));
        }
       /* Picasso.with(getApplicationContext()).load("https://helps2others.com/public/uploads/profile_pics/595020220108073614.jpg")
                .into(activityUpdateProfileBinding.roundedimg);*/
        if (!getProfileResponse.getData().getPicture().isEmpty()) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.e(TAG, "onBitmapLoaded: " );
                  //  activityUpdateProfileBinding.roundedimg.setImageTintList(null);
                    activityUpdateProfileBinding.roundedimg.setImageBitmap(bitmap);
                    //https://helps2others.com/public/uploads/profile_pics/595020220108073614.jpg
                }

                @Override
                public void onBitmapFailed(Drawable errorDrawable) {
                    Log.e(TAG, "onBitmapFailed: " );
                    activityUpdateProfileBinding.roundedimg.setImageDrawable(getDrawable(R.drawable.avtar));
                }

                @Override
                public void onPrepareLoad(Drawable placeHolderDrawable) {

                }
            };
            Picasso.with(this).load(getProfileResponse.getData().getPicture())
                    .placeholder(R.drawable.avtar)
                    .error(R.drawable.avtar)
                    .into(target);
        } else {

        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && data.getData() != null) {
            resultUri = data.getData();
            activityUpdateProfileBinding.roundedimg.setImageURI(resultUri);
            uploadPhoto(resultUri);
        }
    }

   /* public void uploadPhoto(Uri fileUri) {
         file = new File(fileUri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
        multiPartBody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        uploadPhotoToServer();
    }*/


    public void uploadPhoto(Uri fileUri) {
        try {
            File file = createTempFileFromUri(fileUri);
            if (file != null) {
                RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
                multiPartBody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
                uploadPhotoToServer();
            } else {
                Toast.makeText(this, "File creation failed", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to prepare image for upload", Toast.LENGTH_SHORT).show();
        }
    }



    public File createTempFileFromUri(Uri uri) throws IOException {
        InputStream inputStream = getContentResolver().openInputStream(uri);
        if (inputStream == null) return null;

        File tempFile = new File(getCacheDir(), "upload_" + System.currentTimeMillis() + ".jpg");
        FileOutputStream outputStream = new FileOutputStream(tempFile);

        byte[] buffer = new byte[1024];
        int length;
        while ((length = inputStream.read(buffer)) > 0) {
            outputStream.write(buffer, 0, length);
        }

        outputStream.close();
        inputStream.close();

        return tempFile;
    }


    public void uploadPhotoToServer() {
        UploadPhotoRequest request = new UploadPhotoRequest();
        request.setUserId(userId);
        String requestData = new Gson().toJson(request);
        RequestBody description = RequestBody.create(okhttp3.MultipartBody.FORM, requestData);

        if (Constants.haveInternet(UpdateProfile.this)) {
            showLoadingDialog();
            ApiClient.getRestAPI().uploadPic(description, multiPartBody).enqueue(new Callback<UpdateNameResponse>() {
                @Override
                public void onResponse(@NonNull Call<UpdateNameResponse> call, @NonNull Response<UpdateNameResponse> response) {
                    Log.d(TAG, "onResponse:  " + response);
                    dismissLoadingDialog();
                    if (response.isSuccessful()) {
                        uploadPhotosResponse(Objects.requireNonNull(response.body()));
                    } else {
                        if (response.code() == 400) {
                            try {
                                assert response.errorBody() != null;
                                JSONObject jsonResult = new JSONObject(response.errorBody().string());
                                String description = (String) jsonResult.get("description");
                                Toast.makeText(UpdateProfile.this, description, Toast.LENGTH_SHORT).show();
                            } catch (IOException | JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<UpdateNameResponse> call, @NonNull Throwable t) {
                    t.printStackTrace();
                    dismissLoadingDialog();
                    Toast.makeText(UpdateProfile.this, t.getMessage(), Toast.LENGTH_SHORT).show();

                }
            });
        } else {
            dismissLoadingDialog();
            Constants.InternetSettings(UpdateProfile.this);
        }
    }

    private void uploadPhotosResponse(UpdateNameResponse updateNameResponse) {
        if (updateNameResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            Log.d("TAG", "signupResponse: " + updateNameResponse.getMessage());
            Toast.makeText(UpdateProfile.this, updateNameResponse.getMessage(), Toast.LENGTH_LONG).show();
          //  finish();
        }
    }



    public void registerResult(){
        crop = registerForActivityResult(new CropImageContract(), new ActivityResultCallback<CropImageView.CropResult>() {
            @Override
            public void onActivityResult(CropImageView.CropResult result) {
                resultUri = result.getUriContent();

                uploadPhoto(resultUri);

            }
        });

    }


   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            crop.launch(new CropImageContractOptions(null, new CropImageOptions()));
        }
    }*/
    private void checkPermission() {
        int result;
        List<String> listPermissionsNeeded = new ArrayList<>();
        for (String p : permissions) {
            result = ContextCompat.checkSelfPermission(UpdateProfile.this, p);
            if (result != PackageManager.PERMISSION_GRANTED) {
                listPermissionsNeeded.add(p);
            }
        }
        if (!listPermissionsNeeded.isEmpty()) {
            int MULTIPLE_PERMISSIONS = 10;
            ActivityCompat.requestPermissions(UpdateProfile.this, listPermissionsNeeded.toArray(new String[0]), MULTIPLE_PERMISSIONS);
        }
    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

    /*private File uriToFile(Uri uri, Context context) {
        try {
            String fileName = getFileNameFromUri(uri, context);
            File file = new File(context.getCacheDir(), fileName);
            try (InputStream inputStream = context.getContentResolver().openInputStream(uri);
                 FileOutputStream outputStream = new FileOutputStream(file)) {

                byte[] buffer = new byte[1024];
                int bytesRead;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    outputStream.write(buffer, 0, bytesRead);
                }
            }

            return file;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }


    private String getFileNameFromUri(Uri uri, Context context) {
        String result = null;
        if (uri.getScheme().equals("content")) {
            try (Cursor cursor = context.getContentResolver().query(uri, null, null, null, null)) {
                if (cursor != null && cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
                    if (index != -1) {
                        result = cursor.getString(index);
                    }
                }
            }
        }
        if (result == null) {
            result = uri.getLastPathSegment();
        }
        return result;
    }*/

}