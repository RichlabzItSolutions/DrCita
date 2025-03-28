package com.drcita.user.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.drcita.user.R;
import com.drcita.user.adapter.VideoConsultationHospitalAdapter;

import java.util.Arrays;
import java.util.List;

public class VideoConsultationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    ImageView back_VideoConsultation;
    private VideoConsultationHospitalAdapter hospitalAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_consultation);


        back_VideoConsultation = findViewById(R.id.back_VideoConsultation);

        back_VideoConsultation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List<String> hospitals = Arrays.asList(
                "Motherhood Hospital",
                "Medicover Hospital",
                "Rainbow Hospital",
                "Yashoda Hospital",
                "Continental Hospital",
                "Care Hospital"
        );

        hospitalAdapter = new VideoConsultationHospitalAdapter(this, hospitals);
        recyclerView.setAdapter(hospitalAdapter);
    }

    @Override
    public void onBackPressed() {
        String selectedHospital = hospitalAdapter.getSelectedHospital();
        if (selectedHospital != null) {
            Toast.makeText(this, "Selected: " + selectedHospital, Toast.LENGTH_SHORT).show();
        }
        super.onBackPressed();
    }

}