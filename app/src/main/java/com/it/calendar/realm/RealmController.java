package com.it.calendar.realm;

import android.app.Activity;
import android.app.Application;
import android.content.Context;


import io.realm.Realm;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    public RealmController() {
        realm = Realm.getDefaultInstance();
    }


    public static RealmController with(Context context) {

        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }


    public static RealmController getInstance() {

        return instance;
    }

    public Realm getRealm() {

        return realm;
    }

    //Refresh the realm istance
    public void refresh() {

        realm.refresh();
    }

    //clear all objects from Book.class
    public void clearAll() {

        realm.beginTransaction();
        //realm.clear(Book.class);
        realm.commitTransaction();
    }
}
