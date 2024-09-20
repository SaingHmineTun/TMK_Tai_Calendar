package it.saimao.tmktaicalendar.activities;

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

import it.saimao.tmktaicalendar.ShanDate;
import it.saimao.tmktaicalendar.database.AppDatabase;
import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.adapters.NoteAdapter;
import it.saimao.tmktaicalendar.database.NoteDao;
import it.saimao.tmktaicalendar.databinding.ActivityNoteBinding;
import it.saimao.tmktaicalendar.mmcalendar.Language;
import it.saimao.tmktaicalendar.mmcalendar.MyanmarDate;

public class NoteActivity extends AppCompatActivity {

    private ActivityNoteBinding binding;
    private LocalDate todayDate;
    private MyanmarDate myanmarDate;
    private ShanDate shanDate;
    private NoteAdapter noteAdapter;
    private List<Note> noteList;
    private NoteDao noteDao;
    private final int REQUEST_EDIT_NOTE = 123;

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
        noteList = noteDao.getNotesByDate(todayDate);

        if (noteList == null || noteList.isEmpty()) {
            binding.rvNotes.setVisibility(View.GONE);
            binding.lyEmpty.setVisibility(View.VISIBLE);
        } else {
            binding.rvNotes.setVisibility(View.VISIBLE);
            binding.lyEmpty.setVisibility(View.GONE);
            noteAdapter.setNoteList(noteList);
        }
    }

    private void initListener() {

        binding.ibAddNote.setOnClickListener(view -> {
            Intent intent = new Intent(this, AddNoteActivity.class);
            intent.putExtra("date", todayDate);
            startActivity(intent);
        });

    }

    private void initDatabase() {

        var appDatabase = AppDatabase.getAppDatabase(this);
        noteDao = appDatabase.noteDao();

    }

    private void initUi() {
        Serializable ser;
        if (getIntent() != null && (ser = getIntent().getSerializableExtra("date")) != null) {
            todayDate = (LocalDate) ser;
            myanmarDate = MyanmarDate.of(todayDate);
            shanDate = new ShanDate(myanmarDate);
            initData();
        }
    }

    private void initData() {
        binding.pMurng.setText(ShanDate.getPeeMurng(shanDate.getShanYearValue()));
        binding.pHtam.setText(ShanDate.getPeeHtam(todayDate.getYear()));
        binding.pSasana.setText(myanmarDate.getBuddhistEra());
        binding.pKawza.setText(myanmarDate.getYear());
        setNoteDescription();

        noteAdapter = new NoteAdapter(new NoteAdapter.NoteClickListener() {
            @Override
            public void onNoteClicked(Note note) {

                Intent intent = new Intent(NoteActivity.this, AddNoteActivity.class);
                intent.putExtra("note", note);
                startActivityForResult(intent, 123);

            }

            @Override
            public void onNoteDeleted(Note note) {

                deleteNote(note);

            }
        });
        binding.rvNotes.setAdapter(noteAdapter);
        binding.rvNotes.setLayoutManager(new LinearLayoutManager(this));


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

    private void refreshAdapter() {
        noteList = noteDao.getNotesByDate(todayDate);
        noteAdapter.setNoteList(noteList);

    }

    private void setNoteDescription() {
        StringBuilder sb = new StringBuilder();
        sb.append(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL).format(todayDate)).append("\n");
        sb.append("ပီႊတႆး ").append(shanDate.getShanYear()).append(" ၼီႈ၊ ");
        sb.append(ShanDate.translate(myanmarDate.getMonthName(Language.ENGLISH))).append(" ");
        if (myanmarDate.getMoonPhaseValue() == 1 || myanmarDate.getMoonPhaseValue() == 3) {
            sb.append(myanmarDate.getMoonPhase()).append("။\n");
        } else {
            sb.append(myanmarDate.getMoonPhase()).append(" ");
            sb.append(myanmarDate.getFortnightDay()).append(" ");
            sb.append(myanmarDate.getMoonPhaseValue() == 0 ? " ဝၼ်း" : "").append(myanmarDate.getMoonPhaseValue() == 2 ? " ၶမ်ႈ။\n" : "။\n");
        }
        sb.append(ShanDate.toString(todayDate, myanmarDate));
        binding.description.setText(sb.toString().trim());
    }
}