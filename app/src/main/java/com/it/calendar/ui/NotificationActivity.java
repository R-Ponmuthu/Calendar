package com.it.calendar.ui;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;

import com.it.calendar.R;
import com.it.calendar.model.Notification;
import com.it.calendar.model.NotificationModule;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

public class NotificationActivity extends AppCompatActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.notificationRecyclerView)
    RecyclerView notificationRecyclerView;
    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        ButterKnife.bind(this);
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("அறிவிப்புகள்");


        Realm.init(NotificationActivity.this);
        RealmConfiguration myConfig = new RealmConfiguration.Builder()
                .name("notification.realm")
                .modules(new NotificationModule())
                .build();

        realm = Realm.getInstance(myConfig);

        RealmResults<Notification> notifications = realm.where(Notification.class)
                .sort("date", Sort.DESCENDING)
                .findAll();
        
        notificationRecyclerView.setLayoutManager(new LinearLayoutManager(NotificationActivity.this));
        notificationRecyclerView.setAdapter(new NotificationAdapter(NotificationActivity.this, notifications));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
