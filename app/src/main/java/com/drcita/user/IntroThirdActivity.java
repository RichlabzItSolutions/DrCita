package com.drcita.user;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drcita.user.common.OnSwipeTouchListener;

public class IntroThirdActivity extends LanguageBaseActivity {
    CardView thirdintrocardView;
    Button thirdintrofinishBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_third);
        thirdintrocardView = findViewById(R.id.thirdintrocardView);
        thirdintrofinishBtn = findViewById(R.id.thirdintrofinishBtn);

        thirdintrocardView.setOnTouchListener(new OnSwipeTouchListener(IntroThirdActivity.this) {
            public void onSwipeTop() {
            }
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(),IntroSecondActivity.class);
                startActivity(intent);
                /*Intent intent = new Intent(getApplicationContext(),IntroSecondActivity.class);
                startActivity(intent);*/
            }
            public void onSwipeLeft() {

            }
            public void onSwipeBottom() {
            }

        });
        thirdintrofinishBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}