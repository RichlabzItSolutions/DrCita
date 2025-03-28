package com.drcita.user;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import com.drcita.user.common.Constants;

public class SplashScreenActivity extends AppCompatActivity {

    String userId;
       int profilestatus;
    ImageView loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
       // loading = findViewById(R.id.loading);
       // Glide.with(this).load(R.drawable.loding).into(loading);
        SharedPreferences sp = getSharedPreferences(Constants.PREFERENCE_NAME, Context.MODE_PRIVATE);
        userId = sp.getString(Constants.USER_ID, userId);
        profilestatus = sp.getInt(Constants.PROFILESTATUS, profilestatus);
      //  Toast.makeText(getApplicationContext(), "profilestatus"+profilestatus, Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (userId == null || userId.equals("") ) {
                    Intent i=new Intent(SplashScreenActivity.this, SignupActivity.class);
                    startActivity(i);
                    finish();
                }  else if(!userId.equals("0") && profilestatus == 0){
                    Intent i=new Intent(SplashScreenActivity.this, ProfileActivity.class);
                    startActivity(i);
                } else {
                    Intent i=new Intent(SplashScreenActivity.this, DashBoardActivity.class);
                    startActivity(i);
                }

            }
        }, 3000);

    }
}