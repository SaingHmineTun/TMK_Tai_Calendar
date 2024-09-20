package it.saimao.tmktaicalendar.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.DatePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import it.saimao.tmktaicalendar.R;
import it.saimao.tmktaicalendar.database.AppDatabase;
import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.database.NoteDao;
import it.saimao.tmktaicalendar.databinding.ActivityAddNoteBinding;

public class AddNoteActivity extends AppCompatActivity {


    private ActivityAddNoteBinding binding;
    private LocalDate date;
    private NoteDao noteDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddNoteBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        initUi();
        initListener();
        noteDao = AppDatabase.getAppDatabase(this).noteDao();
    }

    private void initListener() {
        binding.lyDate.setOnClickListener(view -> {
            showDatePicker();
        });

        binding.btSave.setOnClickListener(view -> {
            saveNote();
        });

    }

    private void saveNote() {
        String title = binding.etTitle.getText().toString();
        String note = binding.etNote.getText().toString();
        if (!title.isEmpty()) {
            noteDao.addNote(new Note(title, note, date));
            finish();
        }
    }

    private DatePickerDialog datePickerDialog;

    private void showDatePicker() {
        if (datePickerDialog == null) {

            datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                    date = LocalDate.of(year, month + 1, day);
                    updateDateDescription();
                }
            });
        }
        datePickerDialog.show();
    }

    private void updateDateDescription() {
        binding.tvDay.setText(String.valueOf(date.getDayOfMonth()));
        binding.tvFriday.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase());
        binding.tvSeptember.setText(date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getYear());

    }

    private void initUi() {
        Serializable ser;
        if (getIntent() != null && (ser = getIntent().getSerializableExtra("date")) != null) {
            date = (LocalDate) ser;
            updateDateDescription();
        }
    }
}