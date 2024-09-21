package it.saimao.tmktaicalendar;

import android.content.res.Resources;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.database.NoteDao;

public class Utils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static List<Note> getTodayEvents(NoteDao noteDao, LocalDate date) {
        return noteDao.getAllNotes().stream().filter(note -> {
            if (note.isEveryYear()) {
                return date.getMonthValue() == note.getCreated().getMonthValue() && date.getDayOfMonth() == note.getCreated().getDayOfMonth();
            } else {
                return date.isEqual(note.getCreated());
            }
        }).collect(Collectors.toList());
    }


}
