package com.drcita.user;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;

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

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UpdateProfile extends LanguageBaseActivity {

    private ActivityUpdateProfileBinding activityUpdateProfileBinding;
    Uri resultUri;
    private MultipartBody.Part multiPartBody;
    private String userId, name, mobile;
    private ProgressDialog progress;
    private Button updateprofileBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityUpdateProfileBinding = DataBindingUtil.setContentView(this, R.layout.activity_update_profile);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        name = sp.getString(Constants.NAME, name);
        mobile = sp.getString(Constants.MOBILE, mobile);

        setSupportActionBar(activityUpdateProfileBinding.toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setTitle("Update Profile");
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
                ImagePicker.with(UpdateProfile.this)
                        .crop()                    //Crop image(Optional), Check Customization for more option
                        .compress(1024)            //Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
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

    private void updateNameResponse(UpdateNameResponse updateNameResponse) {
        if (updateNameResponse.getStatus().equals("success")) {
            dismissLoadingDialog();
            Log.d("TAG", "signupResponse: " + updateNameResponse.getMessage());
            Toast.makeText(UpdateProfile.this, updateNameResponse.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void getUserProfile() {
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
       /* if (!getProfileResponse.getData().getPicture().isEmpty()) {
            Target target = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.e(TAG, "onBitmapLoaded: " );
                  //  activityUpdateProfileBinding.roundedimg.setImageTintList(null);
                    activityUpdateProfileBinding.roundedimg.setImageBitmap();
                    https://helps2others.com/public/uploads/profile_pics/595020220108073614.jpg
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
*/
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

    private void uploadPhoto(Uri fileUri) {
        File file = new File(fileUri.getPath());
        RequestBody requestFile = RequestBody.create(MediaType.parse("image"), file);
        multiPartBody = MultipartBody.Part.createFormData("photo", file.getName(), requestFile);
        uploadPhotoToServer();
    }

    private void uploadPhotoToServer() {
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


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }*/

}