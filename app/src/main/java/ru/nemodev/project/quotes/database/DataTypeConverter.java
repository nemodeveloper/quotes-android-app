package ru.nemodev.project.quotes.database;

import android.arch.persistence.room.TypeConverter;

import java.util.Calendar;

public class DataTypeConverter
{
    @TypeConverter
    public static Calendar toCalendar(Long value)
    {
        if (value == null)
            return null;

        Calendar temp = Calendar.getInstance();
        temp.setTimeInMillis(value);
        return temp;
    }

    @TypeConverter
    public static Long fromCalendar(Calendar date)
    {
        return date == null
                ? null
                : date.getTimeInMillis();
    }
}
