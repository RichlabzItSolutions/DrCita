package com.drcita.user;
import static android.widget.Toast.LENGTH_SHORT;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;

import com.drcita.user.adapter.MemberAdapter;
import com.drcita.user.common.Constants;
import com.drcita.user.databinding.ActivityAddMemberBinding;
import com.drcita.user.models.userprofile.MemberResponse;
import com.drcita.user.models.userprofile.MemeberRequest;
import com.drcita.user.retrofit.ApiClient;
import com.marcinorlowski.fonty.Fonty;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MembersListActivity extends LanguageBaseActivity {
    ActivityAddMemberBinding addMemberBinding;
    MemberAdapter membersAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMemberBinding = DataBindingUtil.setContentView(this, R.layout.activity_add_member);
        Fonty.setFonts(this);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(this,1);
        addMemberBinding.rvMembers.setLayoutManager(gridLayoutManager);
          setupMembers();
          addMemberBinding.llBack.setOnClickListener(view -> finish());
    }

    private void setupMembers() {
        try{

            SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
            String userId = sp.getString(Constants.USER_ID, "");
            try {
                if (Constants.haveInternet(getApplicationContext())) {
                    showLoadingDialog();
                    MemeberRequest request = new MemeberRequest();
                    request.setUserId(Integer.parseInt(userId));


                    ApiClient.getRestAPI().getMembers(request).enqueue(new Callback<MemberResponse>() {
                        @Override
                        public void onResponse(@NonNull Call<MemberResponse> call, @NonNull Response<MemberResponse> response) {
                            dismissLoadingDialog();
                            if (response.isSuccessful()) {
                                if (response.isSuccessful() && response.body() != null) {

                                    getMembersResponse(response.body());
                                }

                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<MemberResponse> call, @NonNull Throwable t) {
                            t.printStackTrace();
                            Toast.makeText(MembersListActivity.this, t.getMessage(), LENGTH_SHORT).show();
                            dismissLoadingDialog();
                        }
                    });
                }

            }catch (Exception e)
            {
                e.getMessage();
            }

        }catch (Exception ex)
        {
            ex.getMessage();
        }
    }

    private void getMembersResponse(MemberResponse body) {

        try{

       membersAdapter=new MemberAdapter(MembersListActivity.this,body.getData());
       addMemberBinding.rvMembers.setAdapter(membersAdapter);

        }catch (Exception ex)
        {
            ex.getMessage();
        }

    }
}