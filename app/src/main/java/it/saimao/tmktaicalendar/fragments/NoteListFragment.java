package it.saimao.tmktaicalendar.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;

import java.time.LocalDate;
import java.util.List;

import it.saimao.tmktaicalendar.activities.HomeActivity;
import it.saimao.tmktaicalendar.adapters.NoteListAdapter;
import it.saimao.tmktaicalendar.database.AppDatabase;
import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.database.NoteDao;
import it.saimao.tmktaicalendar.databinding.FragmentHolidaysBinding;
import it.saimao.tmktaicalendar.databinding.FragmentNoteListBinding;

public class NoteListFragment extends Fragment implements NoteListAdapter.NoteClickListener {


    private FragmentNoteListBinding binding;
    private NoteListAdapter noteListAdapter;

    private NoteDao noteDao;
    private List<Note> notes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initDatabase();
        initUi();
        initListeners();
    }

    private void initListeners() {

        binding.fabAddNote.setOnClickListener(v -> {
            HomeActivity activity = (HomeActivity) getActivity();
            activity.goAddNote(LocalDate.now());
        });

    }


    private void initDatabase() {
        noteDao = AppDatabase.getAppDatabase(getContext()).noteDao();
    }

    @Override
    public void onStart() {
        super.onStart();
        refreshAdapter();

    }

    private void initUi() {

        noteListAdapter = new NoteListAdapter(this);
        binding.rvNotes.setAdapter(noteListAdapter);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(getContext()));

    }

    private void refreshAdapter() {

        notes = noteDao.getAllNotes();
        noteListAdapter.updateNoteList(notes);
    }

    @Override
    public void onNoteClicked(Note note) {
        HomeActivity activity = (HomeActivity) getActivity();
        activity.goNoteDetailByNote(note);

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
    public void onNoteDeleted(Note note) {
        deleteNote(note);

    }
}