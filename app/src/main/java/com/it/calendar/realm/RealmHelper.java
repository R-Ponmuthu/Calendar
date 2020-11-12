package com.it.calendar.realm;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.it.calendar.beans.MainTable;
import com.it.calendar.util.DateTimeHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class RealmHelper {

    public static Realm realm;

//    public static Realm getRealm(Context context) {
//
//        Realm.init(context);
//        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
//                .build();
//
//        try {
//            realm = Realm.getDefaultInstance();
//        } catch (Exception e) {
//            try {
//                realm = Realm.getInstance(realmConfiguration);
//            } catch (RealmMigrationNeededException r) {
//                Realm.deleteRealm(realmConfiguration);
//                realm = Realm.getInstance(realmConfiguration);
//            }
//        }
//
//        return realm;
//    }

    public static void copyToRealm(final Activity activity) {

//               realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                RealmResults<VirathaDay> rows = realm.where(VirathaDay.class).findAll();
//                rows.deleteAllFromRealm();
//            }
//        });

        realm = RealmController.with(activity).getRealm();

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm bgRealm) {
                String csvFile = "MainTable.csv";
                BufferedReader br = null;
                String line = "";
                String cvsSplitBy = ";";
                try {
                    br = new BufferedReader(new InputStreamReader(activity.getAssets().open(csvFile)));
                    MainTable mainTable = new MainTable();
                    while ((line = br.readLine()) != null) {
                        // use comma as separator
                        final String[] oneWord = line.split(cvsSplitBy);

                        mainTable.setDate(DateTimeHelper.getMillisFromDate(oneWord[0]));
                        mainTable.setDay(Long.valueOf(oneWord[1]));
                        mainTable.setMonth(oneWord[2]);
                        mainTable.setYear(Long.valueOf(oneWord[3]));
                        mainTable.setWeekday(oneWord[4]);
                        mainTable.setTam_month(oneWord[5]);
                        mainTable.setTam_day(Long.valueOf(oneWord[6]));
                        mainTable.setTam_year(oneWord[7]);
                        mainTable.setNallanerem_m(oneWord[8]);
                        mainTable.setNallanerem_e(oneWord[9]);
                        mainTable.setDay_type(oneWord[10]);
                        mainTable.setQuote(oneWord[11]);
                        mainTable.setThiti(oneWord[12]);
                        mainTable.setStar(oneWord[13]);
                        mainTable.setYokam(oneWord[14]);
                        mainTable.setChanthran(oneWord[15]);
                        mainTable.setImportantday(oneWord[16]);
                        mainTable.setHindu_fes(oneWord[17]);
                        mainTable.setMuslim_fes(oneWord[18]);
                        mainTable.setChirs_fes(oneWord[19]);
                        mainTable.setGov_holiday(oneWord[20]);
                        if (!oneWord[21].equals("-"))
                            mainTable.setLeave_flag(Long.valueOf(oneWord[21]));
                        else
                            mainTable.setLeave_flag(Long.valueOf("0"));
                        mainTable.setMesam(oneWord[22]);
                        mainTable.setRisibam(oneWord[23]);
                        mainTable.setMithunam(oneWord[24]);
                        mainTable.setKadakam(oneWord[25]);
                        mainTable.setSimmam(oneWord[26]);
                        mainTable.setKanni(oneWord[27]);
                        mainTable.setThulam(oneWord[28]);
                        mainTable.setViruchakam(oneWord[29]);
                        mainTable.setDhanusu(oneWord[30]);
                        mainTable.setMakaram(oneWord[31]);
                        mainTable.setKumbam(oneWord[32]);
                        mainTable.setMeenam(oneWord[33]);

                        bgRealm.insert(mainTable);
                    }
                } catch (Throwable e) {
                    e.printStackTrace();
                    try {
                        throw e;
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                } finally {
                    if (br != null) {
                        try {
                            br.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Log.e("TAGGED", "SAVED");
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("TAGGED", "FAILED");
            }
        });
    }

    public static void copyAssetFile(Context context, String assetFileName, RealmConfiguration configuration) {
        IOException exceptionWhenClose = null;
        File realmFile = new File(configuration.getRealmDirectory(), configuration.getRealmFileName());
        InputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = context.getAssets().open(assetFileName);
            if (inputStream == null) {
                throw new IOException("Could not open asset file: " + assetFileName);
            }
            outputStream = new FileOutputStream(realmFile);
            byte[] buf = new byte[4096];
            int bytesRead;
            while ((bytesRead = inputStream.read(buf)) > -1) {
                outputStream.write(buf, 0, bytesRead);
            }
        } catch (IOException e) {
            // Handle IO exceptions somehow
            throw new RuntimeException(e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    exceptionWhenClose = e;
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    // Ignores this one if there was an exception when close inputStream.
                    if (exceptionWhenClose == null) {
                        exceptionWhenClose = e;
                    }
                }
            }
        }

        // No other exception has been thrown, only the exception when close. So, throw it.
        if (exceptionWhenClose != null) {
            throw new RuntimeException(exceptionWhenClose);
        }
    }
}
