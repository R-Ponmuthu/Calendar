package com.it.calendar;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.it.calendar.beans.MainTable;
import com.it.calendar.realm.RealmController;
import com.it.calendar.realm.RealmHelper;

import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RealmHelper.copyToRealm(this);

        realm = RealmController.with(this).getRealm();
    }
}
