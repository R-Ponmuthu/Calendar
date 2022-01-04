package com.it.calendar;

import android.content.Context;

import com.it.calendar.beans.GowriNeram;
import com.it.calendar.beans.Kalangal;
import com.it.calendar.beans.Krakakalam;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.MuhurthamTable;
import com.it.calendar.beans.Panchangam;
import com.it.calendar.beans.ThirumanaPorutham;
import com.it.calendar.beans.Vasthu;
import com.it.calendar.beans.VirathaDay;
import com.it.core.db.DataSetHelper;
import com.it.core.db.annotations.DataSet;


@DataSet(name = "Calendar.db", version = 1)
public class CalendarDB {

    public static final Class<?>[] TABLES = new Class<?>[]{

            GowriNeram.class,
            Kalangal.class,
            Krakakalam.class,
            MainTable.class,
            MuhurthamTable.class,
            Panchangam.class,
            ThirumanaPorutham.class,
            Vasthu.class,
            VirathaDay.class
    };

    public static DataSetHelper<CalendarDB> getHelper(Context context) {
        return DataSetHelper.createDataSet(context, CalendarDB.class);
    }
}

