package com.drcita.user;

import androidx.databinding.DataBindingUtil;

import android.os.Bundle;

import com.drcita.user.databinding.ActivityNotificationViewImageBinding;
import com.drcita.user.databinding.ActivityNotificationsBinding;
import com.squareup.picasso.Picasso;

public class NotificationViewImage extends LanguageBaseActivity {

    private ActivityNotificationViewImageBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notification_view_image);
        Bundle extras = getIntent().getExtras();
        if(extras!=null){
            String image = extras.getString("image");
                Picasso.with(this).load(image)
                        .into(binding.viewImage);

        }
    }
}