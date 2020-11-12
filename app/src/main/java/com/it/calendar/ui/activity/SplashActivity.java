package com.it.calendar.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.it.calendar.MainActivity;
import com.it.calendar.R;
import com.it.calendar.utils.SharedPreference;

public class SplashActivity extends AppCompatActivity {

    private SharedPreference sharedPreference = new SharedPreference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(() -> {

            startActivity(new Intent(SplashActivity.this, CalendarActivity.class));
//            startActivity(new Intent(SplashActivity.this, MainActivity.class));
            finish();
        }, 500);
    }
}
