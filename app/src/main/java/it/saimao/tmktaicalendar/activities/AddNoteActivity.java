package it.saimao.tmktaicalendar.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
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
        Intent intent = new Intent();
        intent.putExtra("note", note);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void updateNote() {

        String title = binding.etTitle.getText().toString();
        String desc = binding.etNote.getText().toString();
        if (!title.isEmpty()) {
            note.setTitle(title);
            note.setDescription(desc);
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
                binding.btSave.setText("Update");
                binding.btCancel.setText("Delete");

            } else if ((ser = getIntent().getSerializableExtra("date")) != null) {
                date = (LocalDate) ser;
                updateDateDescription();
            }
        }
    }
}