package com.it.calendar.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DateTimeHelper {

    public static String dateFormat = "d/M/yyyy";
    public static SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat, Locale.US);

    public static Long getMillisFromDate(String date) {

        Date mDate = null;
        try {
            mDate = simpleDateFormat.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        //long dateInMilliseconds = mDate.getTime();

        return mDate.getTime();
    }

    public static String getDateFromMillis(long milliseconds) {

        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(milliseconds);
        return simpleDateFormat.format(calendar.getTime());
    }

    public static Date getCalendarViewFromDate(Calendar calendar) {
        calendar.set(Calendar.DAY_OF_MONTH, 1);

        return calendar.getTime();
    }

    public static Date getToDate(Calendar calendar) {

        //Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        int days = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        calendar.add(Calendar.DATE, days - 1);

        return calendar.getTime();
    }
}
