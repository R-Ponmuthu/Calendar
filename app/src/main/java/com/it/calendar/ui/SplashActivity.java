package com.it.calendar.ui;

import android.content.Intent;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.it.calendar.R;
import com.it.calendar.util.SharedPreference;

public class SplashActivity extends AppCompatActivity {

    private SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {

            if (sharedPreference.getInt(getApplicationContext(), "Theme") == 0)
                startActivity(new Intent(SplashActivity.this, CalendarActivity1.class));
            else
                startActivity(new Intent(SplashActivity.this, CalendarActivity2.class));
            finish();
        }, 1500);
    }
}
