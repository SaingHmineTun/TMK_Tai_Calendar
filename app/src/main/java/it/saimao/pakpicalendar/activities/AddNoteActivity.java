package it.saimao.pakpicalendar.activities;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

import it.saimao.pakpicalendar.R;
import it.saimao.pakpicalendar.database.AppDatabase;
import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.database.NoteDao;
import it.saimao.pakpicalendar.databinding.ActivityAddNoteBinding;

public class AddNoteActivity extends AppCompatActivity {


    private ActivityAddNoteBinding binding;
    private Note note;
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
            if (note == null) {
                saveNote();
            } else {
                updateNote();
            }
        });

        binding.btCancel.setOnClickListener(view -> {
            if (note == null) {
                this.finish();
            } else {
                deleteNote();
            }
        });

    }

    private void deleteNote() {
        noteDao.deleteNote(note);
        finish();
    }

    private void updateNote() {

        String title = binding.etTitle.getText().toString();
        String desc = binding.etNote.getText().toString();
        boolean everyYear = binding.rbEveryYear.isChecked();
        if (!title.isEmpty()) {
            note.setTitle(title);
            note.setDescription(desc);
            note.setEveryYear(everyYear);
            if (!date.isEqual(note.getCreated())) note.setCreated(date);
            note.setUpdated(LocalDate.now());
            noteDao.updateNote(note);
            finish();
        }
    }

    private void saveNote() {
        String title = binding.etTitle.getText().toString();
        String note = binding.etNote.getText().toString();
        boolean everyYear = binding.rbEveryYear.isChecked();
        if (!title.isEmpty()) {
            noteDao.addNote(new Note(title, note, everyYear, date));
            finish();
        }
    }

    private DatePickerDialog datePickerDialog;

    private void showDatePicker() {
        if (datePickerDialog == null) {

            datePickerDialog = new DatePickerDialog(this);
            datePickerDialog.setOnDateSetListener((datePicker, year, month, day) -> {
                date = LocalDate.of(year, month + 1, day);
                updateDateDescription();
            });
        }
        datePickerDialog.updateDate(date.getYear(), date.getMonthValue() - 1, date.getDayOfMonth());
        datePickerDialog.show();
    }

    private void updateDateDescription() {
        binding.tvDay.setText(String.valueOf(date.getDayOfMonth()));
        binding.tvFriday.setText(date.getDayOfWeek().getDisplayName(TextStyle.FULL, Locale.ENGLISH).toUpperCase());
        binding.tvSeptember.setText(date.getMonth().getDisplayName(TextStyle.FULL, Locale.ENGLISH) + " " + date.getYear());

    }

    private void initUi() {
        if (getIntent() != null) {

            Serializable ser;

            if ((ser = getIntent().getSerializableExtra("note")) != null) {
                note = (Note) ser;
                date = note.getCreated();
                updateDateDescription();
                binding.etTitle.setText(note.getTitle());
                binding.etNote.setText(note.getDescription());
                if (note.isEveryYear()) binding.rbEveryYear.setChecked(true);
                else binding.rbOneYear.setChecked(true);
                binding.btSave.setText("Update");
                binding.btCancel.setText("Delete");

            } else if ((ser = getIntent().getSerializableExtra("date")) != null) {
                date = (LocalDate) ser;
                updateDateDescription();
            }
        }
    }
}