package it.saimao.pakpicalendar.utils;

import android.content.res.Resources;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.database.NoteDao;
import it.saimao.pakpicalendar.mmcalendar.Language;
import it.saimao.pakpicalendar.mmcalendar.MyanmarDate;

public class Utils {

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static List<Note> getTodayEvents(NoteDao noteDao, LocalDate date) {
        return noteDao.getAllNotes().stream().filter(note -> {
            if (note.isEveryYear()) {
                return date.getYear() >= note.getCreated().getYear() && date.getMonthValue() == note.getCreated().getMonthValue() && date.getDayOfMonth() == note.getCreated().getDayOfMonth();
            } else {
                return date.isEqual(note.getCreated());
            }
        }).collect(Collectors.toList());
    }

    public static String description(LocalDate date) {
        MyanmarDate selectedMyanmarDate = MyanmarDate.of(date);
        ShanDate shanDate = new ShanDate(selectedMyanmarDate);
        StringBuilder sb = new StringBuilder();


        sb.append(ShanDate.translate("Sasana Year")).append(" ").append(selectedMyanmarDate.getBuddhistEra()).append(" ဝႃႇ၊ ").append(ShanDate.translate("Myanmar Year")).append(" ").append(selectedMyanmarDate.getYear()).append(" ၶု၊ ").append("ပီႊတႆး ").append(shanDate.getShanYear()).append(" ၼီႈ၊ ");

        sb.append(ShanDate.translate(selectedMyanmarDate.getMonthName(Language.ENGLISH))).append(" ");
        if (selectedMyanmarDate.getMoonPhaseValue() == 1 || selectedMyanmarDate.getMoonPhaseValue() == 3) {
            sb.append(selectedMyanmarDate.getMoonPhase()).append("။");
        } else {
            sb.append(selectedMyanmarDate.getMoonPhase()).append(" ");
            sb.append(selectedMyanmarDate.getFortnightDay()).append(" ");
            sb.append(selectedMyanmarDate.getMoonPhaseValue() == 0 ? " ဝၼ်း" : "").append(selectedMyanmarDate.getMoonPhaseValue() == 2 ? " ၶမ်ႈ။" : "။");
        }

        return sb.toString();
    }

}
