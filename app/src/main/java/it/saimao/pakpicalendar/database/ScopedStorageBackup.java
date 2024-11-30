package it.saimao.pakpicalendar.database;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ScopedStorageBackup {
    public static boolean backupDatabase(Context context, String databaseName) {
        AppDatabase.getAppDatabase(context);
        File dbFile = context.getDatabasePath(databaseName);
        try {
            Uri uri = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                // Define the custom directory
                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DISPLAY_NAME, "pakpi-calendar-" + getCurrentDataTime() + ".db");
                values.put(MediaStore.MediaColumns.MIME_TYPE, "application/x-sqlite3");
                values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOCUMENTS + "/Pakpi Calendar");

                uri = context.getContentResolver().insert(MediaStore.Files.getContentUri("external"), values);
            }

            if (uri != null) {
                try (FileInputStream fis = new FileInputStream(dbFile); OutputStream os = context.getContentResolver().openOutputStream(uri)) {
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = fis.read(buffer)) > 0) {
                        os.write(buffer, 0, length);
                    }
                }
            }
            return true;

        } catch (Exception e) {

        }

        return false;
    }



    private static String getCurrentDataTime() {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy-HHmmss", Locale.ENGLISH);
        Date date = new Date();
        return formatter.format(date);
    }

}
