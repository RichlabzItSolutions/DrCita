package com.drcita.user;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drcita.user.common.OnSwipeTouchListener;

public class IntroSecondActivity extends LanguageBaseActivity{

    CardView secondintrocardView;
    Button secondintroskipBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_second);
        secondintrocardView = findViewById(R.id.secondintrocardView);
        secondintroskipBtn = findViewById(R.id.secondintroskipBtn);

        secondintroskipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });

        secondintrocardView.setOnTouchListener(new OnSwipeTouchListener(IntroSecondActivity.this) {
            public void onSwipeTop() {

            }
            public void onSwipeRight() {
                Intent intent = new Intent(getApplicationContext(),IntroFirstActivity.class);
                startActivity(intent);
            }
            public void onSwipeLeft() {
                Intent intent = new Intent(getApplicationContext(),IntroThirdActivity.class);
                startActivity(intent);

            }
            public void onSwipeBottom() {
            }

        });
    }
}