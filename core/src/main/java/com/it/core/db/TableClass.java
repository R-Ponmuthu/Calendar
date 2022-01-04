package com.it.core.db;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;

import com.it.core.db.annotations.IsID;
import com.it.core.db.annotations.IsSubID;
import com.it.core.db.annotations.NonDatabaseField;


class TableClass<X> {

	ArrayList<DatabaseField<X>> Fields = new ArrayList<>();
	DatabaseField<X> IDField;
	DatabaseField<X> SubIDField;
	Class<X> kclass;

	private static HashMap<Class<?>, TableClass<?>> classContainer = new HashMap<>();

	private TableClass(Class<X> kclass) {
		this.kclass = kclass;
		parseClass(kclass);
	}

	void parseClass(Class<?> c) {
		for (Field field : c.getDeclaredFields()) {
			if (field.isAnnotationPresent(NonDatabaseField.class) || Modifier.isFinal(field.getModifiers()))
				continue;
			field.setAccessible(true);
			if (field.isAnnotationPresent(IsID.class)) {
				IDField = new DatabaseField<>(field);
				IDField.name = DataSetHelper.ID;
			} else {
				DatabaseField<X> dbField = new DatabaseField<>(field);
				if (field.isAnnotationPresent(IsSubID.class))
					SubIDField = dbField;
				Fields.add(dbField);
			}
		}

		if (!c.getSuperclass().equals(Object.class)) {
			parseClass(c.getSuperclass());
		}
	}

	@SuppressWarnings("unchecked")
	public static <X> TableClass<X> getClass(Class<X> c) {
		synchronized (classContainer) {
			if (classContainer.containsKey(c))
				return (TableClass<X>) classContainer.get(c);

			TableClass<X> table = new TableClass<>(c);
			classContainer.put(c, table);
			
			return table;
		}
	}

}
