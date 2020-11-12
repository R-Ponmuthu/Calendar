package com.it.calendar;

import com.it.calendar.beans.GowriNeram;
import com.it.calendar.beans.Kalangal;
import com.it.calendar.beans.Krakakalam;
import com.it.calendar.beans.MainTable;
import com.it.calendar.beans.MuhurthamTable;
import com.it.calendar.beans.Panchangam;
import com.it.calendar.beans.ThirumanaPorutham;
import com.it.calendar.beans.Vasthu;
import com.it.calendar.beans.VirathaDay;


import io.realm.annotations.RealmModule;

@RealmModule(library = true, classes = {GowriNeram.class, Kalangal.class, Krakakalam.class, MainTable.class, MuhurthamTable.class, Panchangam.class, Vasthu.class, VirathaDay.class,
        ThirumanaPorutham.class})
public class CalendarModule {
}
