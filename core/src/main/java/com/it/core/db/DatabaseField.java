package com.it.core.db;

import java.lang.reflect.Field;
import java.util.Date;
import java.util.UUID;

import android.content.ContentValues;
import android.database.Cursor;

import com.it.core.db.annotations.CannotUpdate;
import com.it.core.db.annotations.FieldName;
import com.it.core.db.annotations.FieldVersion;
import com.google.gson.annotations.SerializedName;

public class DatabaseField<X> {
	Field field;
	String name;
	private FieldVersion version;
	private boolean canUpdate = true;
	
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
		
		CannotUpdate cannotUpdate = field.getAnnotation(CannotUpdate.class);
		canUpdate = cannotUpdate == null;
	}

	public FieldVersion getVersion() {
		return version;
	}

	public void setValue(X object, Cursor cursor) {
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
		else if (type.equals(int.class) || type.equals(Integer.class) || type.equals(long.class) || type.equals(Long.class)
				|| type.equals(short.class) || type.equals(Short.class) || type.equals(boolean.class) || type.equals(Boolean.class))
			return "integer";
		else if (type.equals(double.class) || type.equals(Double.class) || type.equals(float.class) || type.equals(Float.class))
			return "real";
		else
			return "text";
	}

	public void addValue(ContentValues values, X data, boolean update) {
		try {
			if(update && !canUpdate)
				return;
			Class<?> type = field.getType();
			Object fieldValue = field.get(data);


			if(fieldValue == null) {
				values.putNull(name);
			}
			if (type.equals(Date.class)) {
				values.put(name, ((Date) fieldValue).getTime());
			}
			else if (type.equals(int.class) || type.equals(Integer.class)) {
				values.put(name, Integer.parseInt(fieldValue.toString()));
			}
			else if (type.equals(long.class) || type.equals(Long.class)) {
				values.put(name, Long.parseLong(fieldValue.toString()));
			}
			else if (type.equals(short.class) || type.equals(Short.class)) {
				values.put(name, Short.parseShort(fieldValue.toString()));
			}
			else if (type.equals(double.class) || type.equals(Double.class)) {
				values.put(name, Double.parseDouble(fieldValue.toString()));
			}
			else if (type.equals(float.class) || type.equals(Float.class)) {
				values.put(name, Float.parseFloat(fieldValue.toString()));
			}
			else if (type.equals(boolean.class) || type.equals(Boolean.class)) {
				values.put(name, Boolean.parseBoolean(fieldValue.toString()));
			}
			else {
				if(fieldValue == null) {
					values.putNull(name);
				}
				else {
					values.put(name, fieldValue.toString());
				}
			}
		} catch (Exception e) {

		}
	}
}