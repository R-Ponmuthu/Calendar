package com.it.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
public class ClassHelper {

	public static Class<?> getClass(String className) throws ClassNotFoundException {
		return Class.forName(className);
	}

	public static Object getObject(String className, Object... param) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException,
			InvocationTargetException, ClassNotFoundException {
		return getObject(getClass(className), param);
	}

	public static Object getObject(Class<?> c, Object... param) throws NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		Constructor<?> constructor;

		if (param.length == 0) {
			constructor = c.getConstructor();
		}
		else {
			Class<?>[] Classes = new Class<?>[param.length];
			for (int i = 0; i < param.length; i++) {
				Object object = param[i];
				Classes[i] = object.getClass();
			}

			try {
				constructor = c.getConstructor(Classes);
			}
			catch (NoSuchMethodException e) {
				try {
					constructor = findConstructor(c, Classes);
				}
				catch (Exception e2) {
					throw e;
				}
			}
		}

		return constructor.newInstance(param);
	}

	static public Constructor<?> findConstructor(Class<?> klass, Class<?>... parameterTypes) throws NoSuchMethodException {
		Constructor<?> c = null;
		Constructor<?>[] constructors = klass.getConstructors();
		if(constructors.length == 0)
            constructors = klass.getDeclaredConstructors();
        for (Constructor<?> constructor : constructors) {
			Class<?>[] thisConstructorParameterTypes = constructor.getParameterTypes();
			if (thisConstructorParameterTypes.length == parameterTypes.length) {
				boolean match = true;
				for (int i = 0; i < thisConstructorParameterTypes.length && match; i++) {
					if (thisConstructorParameterTypes[i].isPrimitive())
					{
						if(!matchPrimitive(thisConstructorParameterTypes[i], parameterTypes[i]))
							match = false;
					}
					else if (!thisConstructorParameterTypes[i].isAssignableFrom(parameterTypes[i]))
						match = false;
				}
				if (match)
					return constructor;
			}
		}
		throw new NoSuchMethodException();
	}


	private static boolean matchPrimitive(Class<?> primitive, Class<?> c) {
		
		if(primitive.equals(int.class) && c.equals(Integer.class))
			return true;
		else if(primitive.equals(short.class) && c.equals(Short.class))
			return true;
		else if(primitive.equals(long.class) && c.equals(Long.class))
			return true;
		else if(primitive.equals(double.class) && c.equals(Double.class))
			return true;
		else if(primitive.equals(float.class) && c.equals(Float.class))
			return true;
		else if(primitive.equals(byte.class) && c.equals(Byte.class))
			return true;
		else return primitive.equals(boolean.class) && c.equals(Boolean.class);

    }

	

}
