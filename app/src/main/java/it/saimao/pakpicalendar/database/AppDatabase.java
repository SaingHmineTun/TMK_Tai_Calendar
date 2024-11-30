package it.saimao.pakpicalendar.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Note.class}, version = 1, exportSchema = false)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {

    public static String DB_NAME = "tmk_calendar";

    public abstract NoteDao noteDao();

    private static AppDatabase appDatabase;

    public static synchronized AppDatabase getAppDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = Room.databaseBuilder(context, AppDatabase.class, DB_NAME).setJournalMode(JournalMode.TRUNCATE).allowMainThreadQueries().build();
        }
        return appDatabase;
    }

}
