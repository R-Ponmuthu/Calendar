package com.it.core.db;

import android.content.Context;

import com.it.core.db.annotations.Database;
import com.it.core.db.annotations.Table;

public class DatabaseHelperFactory {

	public static <X> IDatabaseHelper<X> createInstance(Context context, Class<X> c)
	{
		Table dataSetTable = c.getAnnotation(Table.class);
		if(dataSetTable != null)
			return DataSetHelper.getTableHelper(context, dataSetTable.dataSet(), c);
		Database database = c.getAnnotation(Database.class);
		if(database != null)
			return new DatabaseHelper<>(context, c);
		throw new IllegalArgumentException("The class " + c.getName() + " does not implement either Table or Database");
	}
	
}
