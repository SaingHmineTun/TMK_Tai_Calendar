package it.saimao.pakpicalendar.activities;

import android.app.DatePickerDialog;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;

import it.saimao.pakpicalendar.R;
import it.saimao.pakpicalendar.adapters.NoteAdapter;
import it.saimao.pakpicalendar.database.AppDatabase;
import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.database.NoteDao;
import it.saimao.pakpicalendar.databinding.ActivityNoteBinding;
import it.saimao.pakpicalendar.mmcalendar.Language;
import it.saimao.pakpicalendar.mmcalendar.MyanmarDate;
import it.saimao.pakpicalendar.utils.ShanDate;
import it.saimao.pakpicalendar.utils.Utils;

public class NoteActivity extends AppCompatActivity {

    private ActivityNoteBinding binding;
    private LocalDate todayDate;
    private MyanmarDate myanmarDate;
    private ShanDate shanDate;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private NoteDao noteDao;
    public static final int REQUEST_EDIT_NOTE = 123;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initDatabase();
        initListener();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshAdapter();
    }

    @Override
    protected void onPause() {
        super.onPause();
        updateWidget();
    }

    private void updateWidget() {
        Intent intent = new Intent(this, NoteActivity.class);
        intent.setAction(AppWidgetManager.ACTION_APPWIDGET_UPDATE);
        // Get all widget IDs to update
        int[] ids = AppWidgetManager.getInstance(this).getAppWidgetIds(new ComponentName(this, NoteActivity.class));
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_IDS, ids);
        // Send broadcast to update the widget
        sendBroadcast(intent);
    }

    private void refreshAdapter() {
        noteList = Utils.getTodayEvents(noteDao, todayDate);
        if (noteList == null || noteList.isEmpty()) {
            binding.rvNotes.setVisibility(View.GONE);
            binding.lyEmpty.setVisibility(View.VISIBLE);
        } else {
            if (binding.rvNotes.getVisibility() == View.GONE) {
                binding.rvNotes.setVisibility(View.VISIBLE);
                binding.lyEmpty.setVisibility(View.GONE);
            }
            noteAdapter.updateNoteList(noteList);

        }

    }

    private void initListener() {

        binding.ibAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra("date", todayDate);
            startActivity(intent);
        });

        binding.ibNextDay.setOnClickListener(view -> {
            todayDate = todayDate.plusDays(1);
            updateUi();
        });

        binding.ibPreDay.setOnClickListener(view -> {
            todayDate = todayDate.minusDays(1);
            updateUi();
        });

        binding.cvEngDay.setOnClickListener(view -> showDatePicker());
    }

    private void updateUi() {
        initData();
        refreshAdapter();

    }


    private void initDatabase() {

        var appDatabase = AppDatabase.getAppDatabase(this);
        noteDao = appDatabase.noteDao();

    }

    private DatePickerDialog datePickerDialog;

    private void showDatePicker() {
        if (datePickerDialog == null) {

            datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                todayDate = LocalDate.of(year, month + 1, day);
                updateUi();

            });
        }
        datePickerDialog.updateDate(todayDate.getYear(), todayDate.getMonthValue() - 1, todayDate.getDayOfMonth());
        datePickerDialog.show();
    }

    private void initUi() {
        noteAdapter = new NoteAdapter(new NoteAdapter.NoteClickListener() {
            @Override
            public void onNoteClicked(Note note) {

                Intent intent = new Intent(NoteActivity.this, AddNoteActivity.class);
                intent.putExtra("note", note);
                startActivityForResult(intent, REQUEST_EDIT_NOTE);

            }

            @Override
            public void onNoteDeleted(Note note) {

                deleteNote(note);

            }
        });
        binding.rvNotes.setAdapter(noteAdapter);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));

        Serializable ser;
        if (getIntent() != null && (ser = getIntent().getSerializableExtra("date")) != null) {
            todayDate = (LocalDate) ser;
            myanmarDate = MyanmarDate.of(todayDate);
            shanDate = new ShanDate(myanmarDate);
            initData();
        }
    }

    private void initData() {
        myanmarDate = MyanmarDate.of(todayDate);
        shanDate = new ShanDate(myanmarDate);
        binding.pMurng.setText(ShanDate.getPeeMurng(shanDate.getShanYearValue()));
        binding.pHtam.setText(ShanDate.getPeeHtam(todayDate.getYear()));
        binding.pSasana.setText(myanmarDate.getBuddhistEra());
        binding.pKawza.setText(myanmarDate.getYear());
        binding.tvEngDay.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(todayDate));
        setNoteDescription();

    }

    private void deleteNote(Note note) {

        noteDao.deleteNote(note);
        refreshAdapter();
        Snackbar.make(binding.getRoot(), "Note deleted success!", Snackbar.LENGTH_LONG).setAction("Restore", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                noteDao.addNote(note);
                refreshAdapter();
            }
        }).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_EDIT_NOTE && resultCode == RESULT_OK) {
            Note note = (Note) data.getSerializableExtra("note");
            deleteNote(note);
        }
    }

    private void setNoteDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append("ပီႊတႆး ").append(shanDate.getShanYear()).append(" ၼီႈ၊ ");
        sb.append(ShanDate.translate(myanmarDate.getMonthName(Language.ENGLISH))).append(" ");
        if (myanmarDate.getMoonPhaseValue() == 1 || myanmarDate.getMoonPhaseValue() == 3) {
            sb.append(myanmarDate.getMoonPhase()).append("၊ ");
        } else {
            sb.append(myanmarDate.getMoonPhase()).append(" ");
            sb.append(myanmarDate.getFortnightDay()).append(" ");
            sb.append(myanmarDate.getMoonPhaseValue() == 0 ? " ဝၼ်း" : "").append(myanmarDate.getMoonPhaseValue() == 2 ? " ၶမ်ႈ၊ " : "၊ ");
        }
        sb.append("ဝၼ်း").append(myanmarDate.getWeekDay()).append("။\n");

        List<String> holidays = ShanDate.getHolidays(myanmarDate);
        if (!holidays.isEmpty()) {
            for (String holiday : holidays) {
                String day = ShanDate.translate(holiday);
                if (day == null)
                    day = holiday;
                sb.append(day).append("၊ ");
            }
            binding.tvEngDay.setTextColor(getResources().getColor(R.color.md_theme_error));
        } else {
            binding.tvEngDay.setTextColor(getResources().getColor(R.color.md_theme_onBackground));
        }

        sb.append(ShanDate.toString(todayDate, myanmarDate));
        binding.description.setText(sb.toString().trim());
    }
}