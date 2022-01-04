package com.it.core.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public interface IDatabaseHelper<X> {

    Object lock = new Object();

    int getCount(String selection, String[] selectionArgs, String orderBy, String limit);

    List<X> getList(String selection, String[] selectionArgs, String orderBy, String limit);

    void delete(long id);

    void delete(SQLiteDatabase param, String selection, String[] selectionArgs);

    ArrayList<X> getList(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy, String limit) throws Exception;

    ArrayList<X> getList(Cursor cursor) throws Exception;

    ArrayList<X> getDistinctList(SQLiteDatabase db, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws Exception;

    ArrayList<X> rawQuery(SQLiteDatabase db, String query, String[] selectionArgs);

    int getCount(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy, String limit) throws Exception;

    int insertOrUpdate(SQLiteDatabase param, X data);

    int insert(SQLiteDatabase param, X data);

    int update(SQLiteDatabase param, X data);

    X getItem(SQLiteDatabase param, String selection, String[] selectionArgs, String orderBy);

    X getItem(SQLiteDatabase param, int id);

    int exist(SQLiteDatabase param, String selection, String[] selectionArgs, String orderBy);

    boolean exist(SQLiteDatabase param, X data);

    void close();

    SQLiteDatabase getWritableDatabase();

    SQLiteDatabase getReadableDatabase();

    void beginTransaction(SQLiteDatabase db);

    void commitTransaction(SQLiteDatabase db);

    void endTransaction(SQLiteDatabase db);

    void recycle(X object);
}