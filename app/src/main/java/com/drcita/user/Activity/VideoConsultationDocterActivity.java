package com.drcita.user.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.drcita.user.R;
import com.drcita.user.adapter.VideoConsultationDocterAdapter;
import com.drcita.user.adapter.VideoConsultationHospitalAdapter;

import java.util.Arrays;
import java.util.List;

public class VideoConsultationDocterActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    private VideoConsultationDocterAdapter docterAdapter;

    ImageView back_VideoConsultation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_consultation_docter);

        back_VideoConsultation = findViewById(R.id.back_VideoConsultation);

        recyclerView = findViewById(R.id.recyclerView);

        back_VideoConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView.setLayoutManager(new LinearLayoutManager(this));



        List<String> docters = Arrays.asList(
                "Dr. Ahmed Sayyed",
                "Dr. Ahmed Sayyed",
                "Dr. Ahmed Sayyed",
                "Dr. Ahmed Sayyed",
                "Dr. Ahmed Sayyed",
                "Dr. Ahmed Sayyed"
        );

        docterAdapter = new VideoConsultationDocterAdapter(VideoConsultationDocterActivity.this, docters);
        recyclerView.setAdapter(docterAdapter);
    }
}