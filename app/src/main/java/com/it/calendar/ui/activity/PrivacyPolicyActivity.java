package com.it.calendar.ui.activity;

import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.it.calendar.R;
import com.it.calendar.util.Utils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PrivacyPolicyActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.webView)
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_privacy_policy);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("தனியுரிமை கொள்கை");

        if (new Utils().isOnline(PrivacyPolicyActivity.this))
            webView.loadUrl("https://pages.flycricket.io/tamil-calendar-0/privacy.html");
        else
            Toast.makeText(this, "Check internet connection", Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
