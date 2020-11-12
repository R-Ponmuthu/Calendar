package com.it.calendar.notification;

import com.it.calendar.notification.beans.Notification;

import io.realm.annotations.RealmModule;

@RealmModule(library = true, classes = {Notification.class})
public class NotificationModule {
}
