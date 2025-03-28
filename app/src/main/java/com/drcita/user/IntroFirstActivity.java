package com.drcita.user;

import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.drcita.user.common.OnSwipeTouchListener;

public class IntroFirstActivity extends LanguageBaseActivity {

    CardView firstintrocardView;
    Button firstintroskipBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro_first);
        firstintrocardView = findViewById(R.id.firstintrocardView);
        firstintroskipBtn = findViewById(R.id.firstintroskipBtn);

        firstintrocardView.setOnTouchListener(new OnSwipeTouchListener(IntroFirstActivity.this) {
            public void onSwipeTop() {

            }
            public void onSwipeRight() {

            }
            public void onSwipeLeft() {
                Intent intent = new Intent(getApplicationContext(),IntroSecondActivity.class);
                startActivity(intent);
            }
            public void onSwipeBottom() {
            }

        });
        firstintroskipBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),SignupActivity.class);
                startActivity(intent);
            }
        });
    }
}