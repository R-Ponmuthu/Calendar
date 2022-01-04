package com.it.core.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.WeakHashMap;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.it.core.db.annotations.DataSet;
import com.it.core.db.annotations.Table;


public class DataSetHelper<T> extends SQLiteOpenHelper {

    private Class<?> c;

    private DataSet dataSetAnnotation;

    public static final String ID = BaseColumns._ID;

    static final String LOG = "DataSetHelper";

    private static WeakHashMap<Class<?>, DataSetHelper<?>> singletons = new WeakHashMap<>();

    private ArrayList<TableHelper<?>> Tables = new ArrayList<>();
    private HashMap<Class<?>, TableHelper<?>> Map = new HashMap<>();

    public DataSetHelper(Context context, Class<T> c) {
        super(context, getDBName(c), null, getVersion(c));
        this.c = c;

        try {
            parseClass(c);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private void parseClass(Class<?> c) throws IllegalArgumentException, IllegalAccessException, NoSuchFieldException {
        //DataSet database = getDataSetAnnotation();

        Class<?>[] tables = (Class<?>[]) c.getField("TABLES").get(null);

        if (tables.length == 0)
            throw new IllegalArgumentException("tables in DataSet cannot be empty");

        for (Class<?> kclass : tables) {
            TableHelper helper = new TableHelper(this, kclass);
            Tables.add(helper);
            Map.put(kclass, helper);
        }
    }

    @SuppressWarnings("unchecked")
    public <X> TableHelper<X> getTableHelper(Class<X> c) {
        return (TableHelper<X>) Map.get(c);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        for (TableHelper<?> table : Tables) {
            table.onCreate(db);
        }
    }

	/*@Override
	public synchronized SQLiteDatabase getWritableDatabase() {
		try {
			throw new Exception();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return super.getWritableDatabase();
	}*/

    @SuppressWarnings("rawtypes")
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int old = oldVersion + 1;
        for (; old <= newVersion; old++) {
            for (TableHelper table : Tables) {
                if (table.version == old) {
                    table.onCreate(db);
                }
            }
        }

        for (TableHelper table : Tables) {
            if (table.version <= oldVersion) {
                table.onUpgrade(db, oldVersion, newVersion);
            }
        }
    }

    DataSet getDataSetAnnotation() {
        if (dataSetAnnotation == null) {
            dataSetAnnotation = getDataSetAnnotation(c);
        }

        return dataSetAnnotation;
    }

    public Cursor rawQuery(SQLiteDatabase db, String sql, String[] selectionArgs) {
        try {
            return db.rawQuery(sql, selectionArgs);
        } catch (Exception e) {
            Log.e("DataSetHelper", "rawQuery", e);
        }

        return null;
    }

    private static DataSet getDataSetAnnotation(Class<?> c) {
        while (!c.equals(Object.class)) {
            if (c.isAnnotationPresent(DataSet.class)) {
                try {
                    DataSet db = c.getAnnotation(DataSet.class);
                    return db;
                } catch (Exception e) {

                }
            }

            c = c.getSuperclass();

            if (c == null)
                break;
        }

        throw new IllegalArgumentException("DataSet not added");
    }

    static Table getDatabaseAnnotation(Class<?> c) {
        while (!c.equals(Object.class)) {
            if (c.isAnnotationPresent(Table.class)) {
                try {
                    Table db = c.getAnnotation(Table.class);
                    return db;
                } catch (Exception e) {

                }
            }

            c = c.getSuperclass();

            if (c == null)
                break;
        }

        throw new IllegalArgumentException("Table name not supplied; class = " + c.getName());
    }

    private static String getDBName(Class<?> c) {
        return getDataSetAnnotation(c).name();
    }

    private static int getVersion(Class<?> c) {
        return getDataSetAnnotation(c).version();
    }

    @SuppressWarnings("unchecked")
    public static <T> DataSetHelper<T> createDataSet(Context context, Class<T> c) {
        if (singletons.containsKey(c))
            return (DataSetHelper<T>) singletons.get(c);
        else {
            DataSetHelper<T> helper = new DataSetHelper<>(context, c);
            //singletons.put(c, helper);
            return helper;
        }
    }

    public static <T, X> TableHelper<X> getTableHelper(Context context, Class<T> dataSetClass, Class<X> tableClass) {
        return createDataSet(context, dataSetClass).getTableHelper(tableClass);
    }

}
