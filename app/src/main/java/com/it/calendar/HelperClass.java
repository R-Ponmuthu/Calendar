package com.it.calendar;

import android.content.Context;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.exceptions.RealmMigrationNeededException;

public class HelperClass {

    public static Realm loadAssetFileOnMigration(Context context) {

        Realm.init(context);
        Realm realm;
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder()
                .name("data").build();
        try {
            realm = Realm.getInstance(realmConfiguration);
        } catch (Exception e) {
            try {
                realm = Realm.getInstance(realmConfiguration);
            } catch (RealmMigrationNeededException r) {
                Realm.deleteRealm(realmConfiguration);
                copyAssetFile(context, "data.realm", realmConfiguration);
                realm = Realm.getInstance(realmConfiguration);
            }
        }

        // TODO Don't re-create this every time
        /*RealmConfiguration config = new RealmConfiguration.Builder().build();
        Realm realm;
        try {
            realm = Realm.getInstance(config);
        } catch (RealmMigrationNeededException e) {
            // A migration was required. Delete the old file and load a
            // new from the asset folder.
            Realm.deleteRealm(config);
            copyAssetFile(context, "default.realm", config);
            realm = Realm.getInstance(config); // This should now succeed
        }*/
        return realm;
    }

    // Modified copy of https://github.com/realm/realm-java/blob/master/realm/realm-library/src/main/java/io/realm/RealmCache.java#L340
    private static void copyAssetFile(Context context, String assetFileName, RealmConfiguration configuration) {
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
            throw new RuntimeException();
        }
    }
}
