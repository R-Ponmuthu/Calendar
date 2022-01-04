package com.it.core.util;

import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;

import com.it.core.db.annotations.IsID;
import com.it.core.log.LogEntry;

/**
 * Created by vivek on 24-11-2017.
 */

public class ObjectPool<T> {


    private static HashMap<Class<?>, ObjectPool<?>> objectPools = new HashMap<>();

    private LinkedBlockingQueue<WeakReference<T>> pool = new LinkedBlockingQueue<WeakReference<T>>();
    private Class<T> kclass;

    private List<Field> listFields = new ArrayList<>();

    public ObjectPool(Class<T> kclass) {
        this.kclass = kclass;
        scanClass(kclass);
    }

    private void scanClass(Class<?> kclass) {
        for (Field field : kclass.getDeclaredFields()) {
            listFields.add(field);
        }

        if(kclass.getSuperclass() != null && !kclass.getSuperclass().getName().equals(Object.class))
            scanClass(kclass.getSuperclass());
    }

    /*public synchronized void recycle(T object)
    {
        clearObject(kclass, object);
        pool.add(new WeakReference<T>(object));
    }*/


    public synchronized void recycle(T object)
    {
        if(object instanceof LogEntry)
        {
            clearObject(kclass, object);
            pool.add(new WeakReference<T>(object));
        }
    }

    /*public synchronized void recycle(T object)
    {

    }*/

    private void clearObject(Class<?> kclass, T object) {

        for (Field field : listFields) {
            try {

                if((field.getModifiers() & Modifier.TRANSIENT) > 0)
                    continue;

                field.setAccessible(true);

                if(field.isAnnotationPresent(IsID.class))
                    field.set(object, 0);
                else if(!field.getType().isPrimitive())
                    field.set(object, null);
                else if (field.getType().getName().equals(Boolean.class.getName()) || field.getType().getName().equals(boolean.class.getName())) {
                    field.set(object, false);
                } else if (field.getType().getName().equals(Integer.class.getName()) || field.getType().getName().equals(int.class.getName())) {
                    field.set(object, 0);
                } else if (field.getType().getName().equals(Float.class.getName()) || field.getType().getName().equals(float.class.getName())) {
                    field.set(object, 0);
                } else if (field.getType().getName().equals(Double.class.getName()) || field.getType().getName().equals(double.class.getName())) {
                    field.set(object, 0);
                } else if (field.getType().getName().equals(Long.class.getName()) || field.getType().getName().equals(long.class.getName())) {
                    field.set(object, 0);
                } else if (field.getType().getName().equals(Short.class.getName()) || field.getType().getName().equals(short.class.getName())) {
                    field.set(object, 0);
                } else if (field.getType().getName().equals(Byte.class.getName()) || field.getType().getName().equals(byte.class.getName())) {
                    field.set(object, 0x0);
                }

            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

    }

    public synchronized T get() {

        T obj = null;

        while(pool.size() > 0 && obj == null)
        {
            WeakReference<T> ref = pool.poll();

            obj = ref.get();
        }

        return obj;

    }

    public static <X> ObjectPool<X> getPool(Class<X> kclass)
    {
        ObjectPool<X> objectPool;

        synchronized (objectPools) {

            objectPool = (ObjectPool<X>) objectPools.get(kclass);

            if (objectPool == null) {
                objectPool = new ObjectPool<>(kclass);
                objectPools.put(kclass, objectPool);
            }

        }

        return objectPool;
    }
}
