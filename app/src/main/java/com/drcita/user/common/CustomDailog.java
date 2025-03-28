package com.drcita.user.common;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.drcita.user.R;

public class CustomDailog extends Dialog {

    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_dailog);

      //  loading = findViewById(R.id.loading);
      //  Glide.with(getContext()).load(R.drawable.loding).into(loading);

    }

    public CustomDailog(@NonNull Context context) {
        super(context);
    }

    public CustomDailog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
    }

    protected CustomDailog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }
}
