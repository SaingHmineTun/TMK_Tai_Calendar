package it.saimao.pakpicalendar.utils;

import android.content.Context;

public class PrefManager {
    private static final String SP_NAME = "pakpi_calendar";

    // 0 for Normal Calendar, 1 for Pakpi Calendar
    private static final String CALENDAR_TYPE = "calendar_type";

    public static void saveCalendarType(Context context, CalendarType type) {
        var sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        var editor = sp.edit();
        editor.putInt(CALENDAR_TYPE, type.getValue());
        editor.apply();
    }

    public static CalendarType getCalendarType(Context context) {
        var sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
        var type = sp.getInt(CALENDAR_TYPE, 0);
        if (type == 0) return CalendarType.NORMAL;
        else return CalendarType.PAKPI;
    }
}
