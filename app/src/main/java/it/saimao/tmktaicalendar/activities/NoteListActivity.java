package it.saimao.tmktaicalendar.activities;

import static it.saimao.tmktaicalendar.activities.NoteActivity.REQUEST_EDIT_NOTE;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.List;

import it.saimao.tmktaicalendar.adapters.NoteListAdapter;
import it.saimao.tmktaicalendar.database.AppDatabase;
import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.database.NoteDao;
import it.saimao.tmktaicalendar.databinding.ActivityNoteListBinding;

public class NoteListActivity extends AppCompatActivity implements NoteListAdapter.NoteClickListener {


    private ActivityNoteListBinding binding;
    private NoteListAdapter noteListAdapter;

    private NoteDao noteDao;
    private List<Note> notes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNoteListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        initDatabase();
        initUi();
        initListeners();

    }

    private void initListeners() {
        binding.ibNavigateBack.setOnClickListener(v -> {
            this.finish();
        });

        binding.fabAddNote.setOnClickListener(v -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra("date", LocalDate.now());
            startActivityForResult(intent, REQUEST_EDIT_NOTE);
        });

    }

    private void initDatabase() {
        noteDao = AppDatabase.getAppDatabase(this).noteDao();
    }

    @Override
    protected void onStart() {
        super.onStart();
        refreshAdapter();

    }

    private void initUi() {

        noteListAdapter = new NoteListAdapter(this);
        binding.rvNotes.setAdapter(noteListAdapter);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));

    }

    private void refreshAdapter() {

        notes = noteDao.getAllNotes();
        noteListAdapter.updateNoteList(notes);
    }

    @Override
    public void onNoteClicked(Note note) {
        Intent intent = new Intent(this, AddNoteActivity.class);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_EDIT_NOTE);
    }

    private void deleteNote(Note note) {

        noteDao.deleteNote(note);
        refreshAdapter();
        Snackbar.make(binding.getRoot(), "Note deleted success!", Snackbar.LENGTH_LONG).setAction("Restore", view -> {
            noteDao.addNote(note);
            refreshAdapter();
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

    @Override
    public void onNoteDeleted(Note note) {
        deleteNote(note);

    }
}