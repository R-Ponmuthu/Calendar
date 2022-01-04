package com.it.calendar.ui.activity;

import static com.it.calendar.CalendarApp.setDefaultDataBase;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.it.calendar.CalendarApp;
import com.it.calendar.R;
import com.it.calendar.utils.SharedPreference;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        if (!new CalendarApp().checkDataBase())
            setDefaultDataBase(this);

        new Handler().postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, CalendarActivity.class));
            finish();
        }, 500);
    }
}
