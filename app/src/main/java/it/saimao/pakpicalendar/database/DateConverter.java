package it.saimao.pakpicalendar.database;

import androidx.room.TypeConverter;

import java.time.LocalDate;

public class DateConverter {

    @TypeConverter
    public LocalDate fromLongToLocalDate(long l) {
        return LocalDate.ofEpochDay(l);
    }

    @TypeConverter
    public long fromLocalDateToLong(LocalDate date) {
        return date.toEpochDay();
    }


}
