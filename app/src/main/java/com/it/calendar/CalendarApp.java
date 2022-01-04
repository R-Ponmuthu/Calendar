package com.it.calendar;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import androidx.multidex.MultiDex;
import androidx.multidex.MultiDexApplication;

import com.google.android.gms.ads.MobileAds;
import com.it.core.db.TableHelper;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;



public class CalendarApp extends MultiDexApplication {

    private String TAG = "CalendarApp";

    public static String DB_PATH = "/data/data/" + BuildConfig.APPLICATION_ID + "/databases/";
    public static String DB_NAME = "Calendar.db";

    @SuppressLint("MissingPermission")
    @Override
    public void onCreate() {
        super.onCreate();

        MultiDex.install(this);
        MobileAds.initialize(this, getString(R.string.admob_app_id));

//        Realm.init(this);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
//                .name("Calendar.realm")
////                .deleteRealmIfMigrationNeeded()
//                .modules(new CalendarModule())
//                .build();
//        Realm.setDefaultConfiguration(realmConfiguration);
//
//        RealmHelper.copyAssetFile(this, "Calendar.realm", realmConfiguration);
    }


    public static void setDefaultDataBase(Context context) {
        try {
            InputStream myInput = context.getAssets().open(DB_NAME);
            String outFileName = DB_PATH + DB_NAME;
            OutputStream myOutput = new FileOutputStream(outFileName);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = myInput.read(buffer)) > 0) {
                myOutput.write(buffer, 0, length);
            }
            myOutput.flush();
            myOutput.close();
            myInput.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("DB Exception",e.getMessage());
        }
    }

    public boolean checkDataBase() {
        Log.d(TAG, "checkDataBase: Enter");
        SQLiteDatabase checkDB = null;
        try {
            checkDB = SQLiteDatabase.openDatabase(DB_PATH + DB_NAME, null,
                    SQLiteDatabase.OPEN_READWRITE);
            checkDB.close();
            Log.d(TAG, "checkDataBase: loaded");
        } catch (SQLiteException e) {
            Log.d(TAG, "checkDataBase: SQLiteException---" + e);
            e.printStackTrace();
        } catch (Exception e) {
            Log.d(TAG, "checkDataBase: Exception " + e);
            e.printStackTrace();
        }
        return checkDB != null;
    }

    public static <T> TableHelper<T> getTable(Context context, Class<T> _class) {

        return CalendarDB.getHelper(context).getTableHelper(_class);
    }
}
