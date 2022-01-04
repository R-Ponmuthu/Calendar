package com.it.core.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.it.core.db.annotations.Table;
import com.it.core.util.ClassHelper;
import com.it.core.util.ObjectPool;
import com.it.core.util.Util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class TableHelper<X> implements IDatabaseHelper<X> {

    private String tableName;
    int version = 1;

    private Class<X> kclass;
    private Table database;

    @SuppressWarnings("rawtypes")
    private DataSetHelper dataSet;

    private ArrayList<DatabaseField<X>> Fields;
    private DatabaseField<X> IDField;
    private DatabaseField<X> SubIDField;
    private TableClass<X> tableClass;
    private ObjectPool<X> objectPool;

    @SuppressWarnings("rawtypes")
    public TableHelper(DataSetHelper dataSet, Class<X> kclass) {
        super();
        this.kclass = kclass;

        this.dataSet = dataSet;
        database = DataSetHelper.getDatabaseAnnotation(kclass);
        tableName = database.name();
        version = database.version();

        tableClass = TableClass.getClass(kclass);
        Fields = tableClass.Fields;
        SubIDField = tableClass.SubIDField;
        IDField = tableClass.IDField;

        objectPool = ObjectPool.getPool(kclass);
    }

    void onCreate(SQLiteDatabase db) {

        boolean autoIncrement = database.autoIncrementID();

        StringBuilder builder = new StringBuilder();

        builder.append("create table if not exists " + tableName + " (");

        builder.append(DataSetHelper.ID + " integer primary key " + (autoIncrement ? "autoincrement " : ""));

        for (int i = 0; i < Fields.size(); i++) {

            DatabaseField<X> field = Fields.get(i);

            if (i == Fields.size() - 1)
                builder.append("");
            else
                builder.append(", ");

            builder.append(field.getName());
        }

//        for (DatabaseField<X> field : Fields) {
//            builder.append(", ");
//
//            builder.append(field.getName());
//
//            builder.append(" " + field.getSqliteField());
//        }

        builder.append(")");

        db.execSQL(builder.toString());

        if (SubIDField != null) {
            builder = new StringBuilder();

            builder.append("CREATE INDEX 'index-");
            builder.append(tableName);
            builder.append("-");
            builder.append(SubIDField.name);
            builder.append("' ON ");
            builder.append(tableName);
            builder.append("(");
            builder.append(SubIDField.name);
            builder.append(")");

            db.execSQL(builder.toString());
        }

        try {
            Method method = kclass.getMethod("onCreate", getClass(), SQLiteDatabase.class);
            method.invoke(null, this, db);
        } catch (NoSuchMethodException e) {

        } catch (IllegalArgumentException e) {

        } catch (IllegalAccessException e) {

        } catch (InvocationTargetException e) {

        }
    }

    @SuppressWarnings("unchecked")
    public <T> DataSetHelper<T> getDataSet() {
        return dataSet;
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int old = oldVersion + 1;
        for (; old <= newVersion; old++) {
            for (DatabaseField<X> field : Fields) {
                if (field.getVersion() != null && field.getVersion().version() == old) {
                    if (!isFieldExist(db, tableName, field.getName(), field.getSqliteField()))
                        Util.alterAddColumn(db, tableName, field.getName() + " " + field.getSqliteField());
                }
            }
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
    public void recycle(X object) {
        objectPool.recycle(object);
    }

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#getCount(java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    @Override
    public int getCount(String selection, String[] selectionArgs, String orderBy, String limit) {
        SQLiteDatabase db = null;

        try {
            db = dataSet.getReadableDatabase();
            return getCount(db, selection, selectionArgs, orderBy, limit);
        } catch (Exception e) {
            String ErrorMessage = "Getting List";
            Log.v(DataSetHelper.LOG, ErrorMessage, e);
        } finally {
            if (db != null)
                db.close();
        }

        return 0;
    }

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#getList(java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    @Override
    public List<X> getList(String selection, String[] selectionArgs, String orderBy, String limit) {
        SQLiteDatabase db = null;

        try {
            db = dataSet.getReadableDatabase();
            return getList(db, selection, selectionArgs, orderBy, limit);
        } catch (Exception e) {
            String ErrorMessage = "Getting List";
            Log.v(DataSetHelper.LOG, ErrorMessage, e);
        } finally {
            if (db != null)
                db.close();
        }

        return new ArrayList<>();
    }

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#delete(long)
     */

    @Override
    public void delete(long id) {
        delete(null, id);
    }

    public void delete(SQLiteDatabase paramDB, long id) {
        SQLiteDatabase db = paramDB;

        try {
            if (db == null)
                db = dataSet.getWritableDatabase();
            db.delete(tableName, DataSetHelper.ID + " = ?", new String[]{id + ""});
        } catch (Exception e) {
            String ErrorMessage = "Delete post with ID : " + id;
            Log.v(DataSetHelper.LOG, ErrorMessage, e);

        } finally {
            if (paramDB == null && db != null)
                db.close();
        }
    }

    @Override
    public void delete(SQLiteDatabase paramDB, String selection, String[] selectionArgs) {
        SQLiteDatabase db = paramDB;

        try {
            if (db == null)
                db = dataSet.getWritableDatabase();

            db.delete(tableName, selection, selectionArgs);
        } catch (Exception e) {
            Log.v(DataSetHelper.LOG, "", e);

        } finally {
            if (db != null && paramDB == null)
                db.close();
        }
    }

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#getList(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    @Override
    public ArrayList<X> getList(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy, String limit) throws Exception {
        ArrayList<X> list = new ArrayList<>();

        Cursor cursor = null;
        try {
            cursor = db.query(tableName, null, selection, selectionArgs, null, null, orderBy, limit);

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

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#getList(android.database.Cursor)
     */
    @Override
    public ArrayList<X> getList(Cursor cursor) throws Exception {
        ArrayList<X> list = new ArrayList<>();

        try {
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
    public ArrayList<X> getDistinctList(SQLiteDatabase db, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) throws Exception {
        ArrayList<X> list = new ArrayList<>();

        Cursor cursor = null;
        try {

            cursor = db.query(true, tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);

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
    public ArrayList<X> rawQuery(SQLiteDatabase db, String query, String[] selectionArgs) {
        ArrayList<X> list = new ArrayList<>();

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

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#getCount(android.database.sqlite.SQLiteDatabase, java.lang.String, java.lang.String[], java.lang.String, java.lang.String)
     */
    @Override
    public int getCount(SQLiteDatabase db, String selection, String[] selectionArgs, String orderBy, String limit) throws Exception {
        Cursor cursor = null;
        try {

            cursor = db.query(tableName, null, selection, selectionArgs, null, null, orderBy, limit);

            return cursor.getCount();

        } catch (Exception e) {

        } finally {
            if (cursor != null)
                cursor.close();
        }
        return 0;
    }

    @SuppressWarnings("unchecked")
    public X createObject(Cursor cursor) {
        try {
            X object = objectPool.get();

            if (object == null)
                object = (X) ClassHelper.getObject(kclass);

//            IDField.setValue(object, cursor);

            for (DatabaseField<X> field : Fields) {
                field.setValue(object, cursor);
            }

            return object;
        } catch (Exception e) {

        }
        return null;
    }

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#insertOrUpdate(android.database.sqlite.SQLiteDatabase, X)
     */
    @Override
    public int insertOrUpdate(SQLiteDatabase param, X data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = dataSet.getWritableDatabase();

            Cursor cursor = null;

            boolean exist = false;

            try {

                String selection = DataSetHelper.ID + " = ?";

                String[] selectionArgs = new String[]{getIDAsString(data)};

                if (SubIDField != null) {
                    selection += " OR " + SubIDField.getName() + " = ?";
                    selectionArgs = new String[]{getIDAsString(data), getSubIDAsString(data)};
                }

                cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);

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
            Log.v(DataSetHelper.LOG, ErrorMessage, e);

        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    @Override
    public int update(SQLiteDatabase param, X data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = dataSet.getWritableDatabase();

            int id = getID(data);

            String selection = DataSetHelper.ID + " = ?";
            String[] selectionArgs = new String[]{getIDAsString(data)};

            if (SubIDField != null) {
                selection += " OR " + SubIDField.getName() + " = ?";
                selectionArgs = new String[]{getIDAsString(data), getSubIDAsString(data)};
            }

            if (id <= 0) {
                // find the id
                Cursor cursor = null;
                try {
                    cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);

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

            db.update(tableName, getContentValues(data, false, true), selection, selectionArgs);

            return id;

        } catch (Exception e) {
            String ErrorMessage = "Error while updating";
            Log.v(DataSetHelper.LOG, ErrorMessage, e);

        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    private String getIDAsString(X data) {
        if (IDField.field.getType().equals(long.class) || IDField.field.getType().equals(Long.class)) {
            return Long.toString(getLongID(data));
        } else
            return Integer.toString(getID(data));
    }

    private String getSubIDAsString(X data) {
        Object o;
        try {
            o = SubIDField.getField().get(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return o == null ? null : o.toString();
    }

    @Override
    public int insert(SQLiteDatabase param, X data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = dataSet.getWritableDatabase();


            int id = (int) db.insert(tableName, null, getContentValues(data, !database.autoIncrementID(), false));

            IDField.field.set(data, id);

            return id;

        } catch (Exception e) {
            String ErrorMessage = "Error while inserting";
            Log.v(DataSetHelper.LOG, ErrorMessage, e);

        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    private ContentValues getContentValues(X data, boolean withID, boolean update) {
        ContentValues values = new ContentValues();

        if (withID)
            IDField.addValue(values, data, update);

        for (DatabaseField<X> field : Fields) {
            field.addValue(values, data, update);
        }

        return values;
    }

    /* (non-Javadoc)
     * @see crystal.util.sqlite.IDatabaseHelper#close()
     */
    @Override
    public void close() {
        dataSet.close();
    }

    private int getID(X data) {
        try {
            Object id = IDField.getField().get(data);
            if (id.getClass() == Long.class || id.getClass() == long.class)
                return Long.valueOf((Long) id).intValue();
            return Integer.valueOf((Integer) id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private long getLongID(X data) {
        try {
            return (Long) IDField.getField().get(data);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public SQLiteDatabase getWritableDatabase() {
        return dataSet.getWritableDatabase();
    }

    @Override
    public SQLiteDatabase getReadableDatabase() {

        return dataSet.getReadableDatabase();
    }

    @Override
    public X getItem(SQLiteDatabase param, String selection, String[] selectionArgs, String orderBy) {
        List<X> list;

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
                db = dataSet.getWritableDatabase();

            Cursor cursor = null;
            try {
                cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);

                if (cursor.moveToNext()) {
                    return cursor.getInt(cursor.getColumnIndex(DataSetHelper.ID));
                }

            } catch (Exception e) {
                throw e;
            } finally {
                if (cursor != null)
                    cursor.close();
            }
        } catch (Exception e) {
            String ErrorMessage = "Error while exist";
            Log.v(DataSetHelper.LOG, ErrorMessage, e);

        } finally {
            if (param == null && db != null)
                db.close();
        }

        return -1;
    }

    @Override
    public boolean exist(SQLiteDatabase param, X data) {
        SQLiteDatabase db = param;

        try {
            if (db == null)
                db = dataSet.getWritableDatabase();

            int id = getID(data);

            String selection = DataSetHelper.ID + " = ?";
            String[] selectionArgs = new String[]{getIDAsString(data)};

            if (SubIDField != null) {
                selection += " OR " + SubIDField.getName() + " = ?";
                selectionArgs = new String[]{getIDAsString(data), getSubIDAsString(data)};
            }

            Cursor cursor = null;
            try {
                cursor = db.query(tableName, null, selection, selectionArgs, null, null, null);

                if (cursor.moveToNext()) {

                    if (getID(data) <= 0) {
                        id = cursor.getInt(cursor.getColumnIndex(DataSetHelper.ID));
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
            Log.v(DataSetHelper.LOG, ErrorMessage, e);

        } finally {
            if (param == null && db != null)
                db.close();
        }

        return false;
    }

    @Override
    public X getItem(SQLiteDatabase param, int id) {
        return getItem(param, DataSetHelper.ID + " = ?", new String[]{Integer.toString(id)}, null);
    }

    public boolean isFieldExist(SQLiteDatabase db, String tableName, String fieldName, String sqliteField) {
        boolean isExist = false;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery("PRAGMA table_info(" + tableName + ")", null);

            if (cursor != null && cursor.moveToFirst()) {

                cursor.moveToFirst();
                do {
                    String currentColumn = cursor.getString(1);
                    String currentType = cursor.getString(2);

                    if (currentColumn.equalsIgnoreCase(fieldName) || currentType.equalsIgnoreCase(sqliteField)) {
                        isExist = true;
                    }
                } while (cursor.moveToNext());
            }
        } finally {
            if (cursor != null && !cursor.isClosed())
                cursor.close();
        }
        return isExist;
    }
}