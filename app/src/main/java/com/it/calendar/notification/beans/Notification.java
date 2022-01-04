package com.it.calendar.notification.beans;


import com.it.calendar.CalendarDB;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.Table;

@Table(dataSet = CalendarDB.class, name = "Notification", version = 1, autoIncrementID = true)
public class Notification {

    @FieldName("id")
    private int id;
    @FieldName("date")
    private String date;
    @FieldName("title")
    private String title;
    @FieldName("message")
    private String message;
    @FieldName("bigMessage")
    private String bigMessage;
    @FieldName("imageUrl")
    private String imageUrl;
    @FieldName("read")
    private String read;
    @FieldName("notiType")
    private String notiType;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getBigMessage() {
        return bigMessage;
    }

    public void setBigMessage(String bigMessage) {
        this.bigMessage = bigMessage;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getRead() {
        return read;
    }

    public void setRead(String read) {
        this.read = read;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNotiType() {
        return notiType;
    }

    public void setNotiType(String notiType) {
        this.notiType = notiType;
    }
}
