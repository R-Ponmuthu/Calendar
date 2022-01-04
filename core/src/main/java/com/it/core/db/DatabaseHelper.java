package com.it.core.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import com.it.core.db.annotations.Database;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.FieldVersion;
import com.it.core.db.annotations.IsID;
import com.it.core.db.annotations.NonDatabaseField;
import com.it.core.util.ClassHelper;
import com.it.core.util.ObjectPool;
import com.it.core.util.Util;

/**
 * A SQLite Database which create a database using reflection with all the
 * fields which have the annotations {@link SerializedName} a class
 * <p>
 * A class should have the {@link Database} annotation with at least one field
 * which must be an int or long has a {@link IsID} annotation
 *
 * @param <T>
 * @author Vivek
 */
public class DatabaseHelper<T> extends SQLiteOpenHelper implements IDatabaseHelper<T> {

    private Class<T> c;
    private Database databaseAnnotation;

    public static final String TABLE_NAME = "Data";
    public static final String ID = BaseColumns._ID;
    private static final String LOG = "DataBaseHelper";
    private ArrayList<DatabaseField> Fields = new ArrayList<>();
    private DatabaseField IDField;
    private ObjectPool<T> objectPool = null;

    public DatabaseHelper(Context context, Class<T> c) {
        super(context, getDBName(c), null, getVersion(c));
        this.c = c;

        parseClass(c);

        objectPool = ObjectPool.getPool(c);
    }

    private void parseClass(Class<?> c) {
        for (Field field : c.getDeclaredFields()) {
            field.setAccessible(true);
            if (field.isAnnotationPresent(NonDatabaseField.class)) {
            } else if (field.isAnnotationPresent(IsID.class)) {
                IDField = new DatabaseField(field);
                IDField.name = ID;
            } else {
                Fields.add(new DatabaseField(field));
            }
        }
        if (!c.getSuperclass().equals(Object.class)) {
            parseClass(c.getSuperclass());
        }
    }


    @Override
    public void beginTransaction(SQLiteDatabase db) {
        db.beginTransaction();
    }

    @Override
    public void commitTransaction(SQLiteDatabase db) {
        db.setTransactionSuccessful();
    }

    @Override
    public void endTransaction(SQLiteDatabase db) {
        db.endTransaction();
    }

    @Override
    public void recycle(T object) {
        objectPool.recycle(object);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        boolean autoIncrement = getDatabaseAnnotation().autoIncrementID();

        StringBuilder builder = new StringBuilder();

        builder.append("create table " + TABLE_NAME + " (");

        builder.append(ID + " integer primary key " + (autoIncrement ? "autoincrement " : ""));

        for (DatabaseField field : Fields) {
            builder.append(", ");

            builder.append(field.getName());

            builder.append(" " + field.getSqliteField());
        }

        builder.append(")");

        db.execSQL(builder.toString());

        try {
            Method method = c.getMethod("onCreate", getClass(), SQLiteDatabase.class);
            method.invoke(null, this, db);
        } catch (NoSuchMethodException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }
    }

    public int getCount(String selection, String[] selectionArgs, String orderBy, String limit) {
        SQLiteDatabase db = null;

        try {
            db = getReadableDatabase();
            return getCount(db, selection, selectionArgs, orderBy, limit);
        } catch (Exception e) {
            String ErrorMessage = "Getting List";
            Log.d(LOG, ErrorMessage, e);
        } finally {
            if (db != null)
                db.close();
        }

        return 0;
    }

    public List<T> getList(String selection, String[] selectionArgs, String orderBy, String limit) {
        SQLiteDatabase db = null;

        try {
            db = getReadableDatabase();
            return getList(db, selection, selectionArgs, orderBy, limit);
        } catch (Exception e) {
            String ErrorMessage = "Getting List";
            Log.d(LOG, ErrorMessage, e);
            if (db != null)
                db.close();
        }

        return new ArrayList<>();
    }

    public void delete(long id) {
        SQLiteDatabase db = null;

        try {
            db = getWritableDatabase();
            db.delete(TABLE_NAME, ID + " = ?", new String[]{id + ""});
        } catch (Exception e) {
            String ErrorMessage = "Delete post with ID : " + id;
            Log.d(LOG, ErrorMessage, e);
        } finally {
            if (db != null)
                db.close();
        }
    }

    public ArrayList<T> getList(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy, String limit) throws Exception {
        ArrayList<T> list = new ArrayList<>();

        Cursor cursor = null;
        try {

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, orderBy, limit);

            while (cursor.moveToNext()) {

                list.add(createObject(cursor));
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return list;
    }

    public int getCount(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy, String limit) throws Exception {
        Cursor cursor = null;
        try {

            cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, orderBy, limit);

            return cursor.getCount();

        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    protected T createObject(Cursor cursor) {
        try {
            T object = objectPool.get();

            if (object == null)
                object = (T) ClassHelper.getObject(c);

            IDField.setValue(object, cursor);

            for (DatabaseField field : Fields) {
                field.setValue(object, cursor);
            }

            return object;
        } catch (Exception e) {

        }
        return null;
    }

    @Override
    public int insertOrUpdate(SQLiteDatabase param, T data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = getWritableDatabase();

            Cursor cursor = null;

            boolean exist = false;

            try {

                String selection = DataSetHelper.ID + " = ?";

                String[] selectionArgs = new String[]{getIDAsString(data)};

                cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

                if (cursor.moveToNext()) {

                    if (getID(data) <= 0) {
                        int id = cursor.getInt(cursor.getColumnIndex(DataSetHelper.ID));
                        IDField.field.set(data, id);
                    }

                    exist = true;
                }

            } catch (Exception e) {
                throw e;

            } finally {
                if (cursor != null)
                    cursor.close();
            }

            if (exist) {
                return update(db, data);
            } else {
                return insert(db, data);
            }

        } catch (Exception e) {
            String ErrorMessage = "Error while Inserting or Updating";
            Log.d(LOG, ErrorMessage, e);
        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    @Override
    public int update(SQLiteDatabase param, T data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = getWritableDatabase();

            int id = getID(data);

            String selection = DataSetHelper.ID + " = ?";
            String[] selectionArgs = new String[]{getIDAsString(data)};

            if (id <= 0) {
                // find the id
                Cursor cursor = null;
                try {
                    cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

                    if (cursor.moveToNext()) {

                        if (getID(data) <= 0) {
                            id = cursor.getInt(cursor.getColumnIndex(DataSetHelper.ID));
                            IDField.field.set(data, id);
                            selectionArgs[0] = getIDAsString(data);
                        }
                    }

                } catch (Exception e) {
                    throw e;

                } finally {
                    if (cursor != null)
                        cursor.close();
                }
            }

            db.update(TABLE_NAME, getContentValues(data, false), selection, selectionArgs);

            return id;

        } catch (Exception e) {
            String ErrorMessage = "Error while updating";
            Log.d(LOG, ErrorMessage, e);
        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    private String getIDAsString(T data) {
        if (IDField.field.getType().equals(long.class) || IDField.field.getType().equals(Long.class)) {
            return Long.toString(getLongID(data));
        } else
            return Integer.toString(getID(data));
    }

    @Override
    public int insert(SQLiteDatabase param, T data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = getWritableDatabase();

            int id = (int) db.insert(TABLE_NAME, null, getContentValues(data, !getDatabaseAnnotation().autoIncrementID()));

            IDField.field.set(data, id);

            return id;

        } catch (Exception e) {
            String ErrorMessage = "Error while inserting";
            Log.d(LOG, ErrorMessage, e);
        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    private ContentValues getContentValues(T data, boolean withID) {
        ContentValues values = new ContentValues();

        if (withID)
            IDField.addValue(values, data);

        for (DatabaseField field : Fields) {
            field.addValue(values, data);
        }

        return values;
    }

    private int getID(T data) {
        try {
            Object id = IDField.getField().get(data);
            if (id.getClass() == Long.class || id.getClass() == long.class)
                return Long.valueOf((Long) id).intValue();
            return Integer.valueOf((Integer) id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long getLongID(T data) {
        try {
            return IDField.getField().getLong(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        for (; oldVersion < newVersion; oldVersion++) {
            for (DatabaseField field : Fields) {
                if (field.getVersion() != null && field.getVersion().version() - 1 == oldVersion) {
                    Util.alterAddColumn(db, TABLE_NAME, field.getName() + " " + field.getSqliteField());
                }
            }
        }
    }

    private Database getDatabaseAnnotation() {
        if (databaseAnnotation == null) {
            databaseAnnotation = getDatabaseAnnotation(c);
        }

        return databaseAnnotation;
    }

    private static Database getDatabaseAnnotation(Class<?> c) {
        while (!c.equals(Object.class)) {
            if (c.isAnnotationPresent(Database.class)) {
                try {
                    Database db = c.getAnnotation(Database.class);
                    return db;
                } catch (Exception e) {

                }
            }

            c = c.getSuperclass();

            if (c == null)
                break;
        }

        throw new IllegalArgumentException("Database name not supplied");
    }

    private static String getDBName(Class<?> c) {
        return getDatabaseAnnotation(c).name();
    }

    private static int getVersion(Class<?> c) {
        return getDatabaseAnnotation(c).version();
    }

    public class DatabaseField {
        private Field field;
        private String name;
        private FieldVersion version;

        public DatabaseField(Field field) {
            FieldName fieldName = field.getAnnotation(FieldName.class);
            SerializedName name = field.getAnnotation(SerializedName.class);
            version = field.getAnnotation(FieldVersion.class);
            this.field = field;
            if (fieldName != null)
                this.name = fieldName.value();
            else if (name != null)
                this.name = name.value();
            else
                this.name = field.getName();
        }

        public FieldVersion getVersion() {
            return version;
        }

        public void setValue(T object, Cursor cursor) {
            try {
                Class<?> type = field.getType();
                int columnIndex = cursor.getColumnIndex(name);
                if (cursor.isNull(columnIndex))
                    return;
                if (type.equals(UUID.class))
                    field.set(object, UUID.fromString(cursor.getString(columnIndex)));
                else if (type.equals(Date.class))
                    field.set(object, new Date(cursor.getLong(columnIndex)));
                else if (type.equals(int.class) || type.equals(Integer.class))
                    field.set(object, cursor.getInt(columnIndex));
                else if (type.equals(long.class) || type.equals(Long.class))
                    field.set(object, cursor.getLong(columnIndex));
                else if (type.equals(short.class) || type.equals(Short.class))
                    field.set(object, cursor.getShort(columnIndex));
                else if (type.equals(double.class) || type.equals(Double.class))
                    field.set(object, cursor.getDouble(columnIndex));
                else if (type.equals(float.class) || type.equals(Float.class))
                    field.set(object, cursor.getFloat(columnIndex));
                else if (type.equals(boolean.class) || type.equals(Boolean.class))
                    field.set(object, cursor.getInt(columnIndex) == 1);
                else
                    field.set(object, cursor.getString(columnIndex));
            } catch (Exception e) {

            }
        }

        public Field getField() {
            return field;
        }

        public String getName() {
            return name;
        }

        public String getSqliteField() {
            Class<?> type = field.getType();
            if (type.equals(Date.class))
                return "integer";
            else if (type.equals(int.class) || type.equals(Integer.class) || type.equals(long.class) || type.equals(Long.class) || type.equals(short.class) || type.equals(Short.class) || type.equals(boolean.class) || type.equals(Boolean.class))
                return "integer";
            else if (type.equals(double.class) || type.equals(Double.class) || type.equals(float.class) || type.equals(Float.class))
                return "real";
            else
                return "text";
        }

        public <X> void addValue(ContentValues values, X data) {
            try {
                Class<?> type = field.getType();
                if (type.equals(Date.class))
                    values.put(name, ((Date) field.get(data)).getTime());
                else if (type.equals(int.class) || type.equals(Integer.class))
                    values.put(name, field.getInt(data));
                else if (type.equals(long.class) || type.equals(Long.class))
                    values.put(name, field.getLong(data));
                else if (type.equals(short.class) || type.equals(Short.class))
                    values.put(name, field.getShort(data));
                else if (type.equals(double.class) || type.equals(Double.class))
                    values.put(name, field.getDouble(data));
                else if (type.equals(float.class) || type.equals(Float.class))
                    values.put(name, field.getFloat(data));
                else if (type.equals(boolean.class) || type.equals(Boolean.class))
                    values.put(name, field.getBoolean(data));
                else
                    values.put(name, field.get(data).toString());
            } catch (Exception e) {

            }
        }
    }

    @Override
    public ArrayList<T> getList(Cursor cursor) throws Exception {
        throw new UnsupportedOperationException();
    }

    @Override
    public ArrayList<T> getDistinctList(SQLiteDatabase db, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws Exception {
        ArrayList<T> list = new ArrayList<>();

        Cursor cursor = null;
        try {

            cursor = db.query(true, TABLE_NAME, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

            while (cursor.moveToNext()) {
                list.add(createObject(cursor));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return list;
    }

    @Override
    public ArrayList<T> rawQuery(SQLiteDatabase db, String query, String[] selectionArgs) {
        ArrayList<T> list = new ArrayList<>();

        Cursor cursor = null;
        try {

            cursor = db.rawQuery(query, selectionArgs);

            while (cursor.moveToNext()) {
                list.add(createObject(cursor));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null)
                cursor.close();
        }

        return list;
    }


    @Override
    public T getItem(SQLiteDatabase param, String selection, String[] selectionArgs, String orderBy) {
        List<T> list;

        if (param != null) {
            try {
                list = getList(param, selection, selectionArgs, orderBy, "1");
            } catch (Exception e) {
                return null;
            }
        } else
            list = getList(selection, selectionArgs, orderBy, "1");

        if (list.size() > 0)
            return list.get(0);

        return null;
    }

    @Override
    public int exist(SQLiteDatabase param, String selection, String[] selectionArgs, String orderBy) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = getWritableDatabase();

            Cursor cursor = null;
            try {
                cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

                if (cursor.moveToNext()) {
                    return cursor.getInt(cursor.getColumnIndex(ID));
                }

            } catch (Exception e) {
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } catch (Exception e) {
            String ErrorMessage = "Error while exist";
            Log.d(LOG, ErrorMessage, e);
        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    @Override
    public boolean exist(SQLiteDatabase param, T data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = getWritableDatabase();

            int id = getID(data);

            String selection = DataSetHelper.ID + " = ?";
            String[] selectionArgs = new String[]{getIDAsString(data)};

            Cursor cursor = null;
            try {
                cursor = db.query(TABLE_NAME, null, selection, selectionArgs, null, null, null);

                if (cursor.moveToNext()) {

                    if (getID(data) <= 0) {
                        id = cursor.getInt(cursor.getColumnIndex(ID));
                        IDField.field.set(data, id);
                        selectionArgs[0] = getIDAsString(data);
                    }

                    return true;
                }

            } catch (Exception e) {
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
            }

            return true;

        } catch (Exception e) {
            String ErrorMessage = "Error while exist";
            Log.d(LOG, ErrorMessage, e);
        } finally {
            if (param == null && db != null)
                db.close();
        }

        return false;
    }

    @Override
    public T getItem(SQLiteDatabase param, int id) {
        return getItem(param, ID + " = ?", new String[]{Integer.toString(id)}, null);
    }

    @Override
    public void delete(SQLiteDatabase paramDB, String selection, String[] selectionArgs) {
        SQLiteDatabase db = paramDB;

        try {
            if (db == null)
                db = getWritableDatabase();

            db.delete(TABLE_NAME, selection, selectionArgs);
        } catch (Exception e) {
            Log .d(LOG, "", e);
        } finally {
            if (db != null && paramDB == null)
                db.close();
        }
    }

    public boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName) {
        boolean isExist = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

            if (cursor != null && cursor.moveToFirst()) {

                cursor.moveToFirst();
                do {
                    String currentColumn = cursor.getString(1);
                    if (currentColumn.equals(fieldName)) {
                        isExist = true;
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return isExist;
    }
}
