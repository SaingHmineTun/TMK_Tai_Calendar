package it.saimao.pakpicalendar.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

import it.saimao.pakpicalendar.R;
import it.saimao.pakpicalendar.database.Note;
import it.saimao.pakpicalendar.databinding.AdapterNoteBinding;
import it.saimao.pakpicalendar.databinding.AdapterNoteListBinding;

public class NoteListAdapter extends RecyclerView.Adapter<NoteListAdapter.NoteViewHolder> {


    private List<Note> noteList;
    private final NoteClickListener listener;

    public void updateNoteList(List<Note> noteList) {
        var diffResult = DiffUtil.calculateDiff(new NoteDiffCallback(this.noteList, noteList));
        this.noteList = noteList;
        diffResult.dispatchUpdatesTo(this);
    }

    public interface NoteClickListener {
        void onNoteClicked(Note note);

        void onNoteDeleted(Note note);
    }


    public NoteListAdapter(NoteClickListener listener) {
        noteList = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AdapterNoteListBinding binding = AdapterNoteListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {


        Note note = noteList.get(position);
        holder.binding.cvNote.setOnClickListener(view -> {
            listener.onNoteClicked(note);
        });

        if (note.isEveryYear()) {
            holder.binding.ivEvent.setImageResource(R.drawable.event);
            String date = LocalDate.now().getYear() - note.getCreated().getYear() == 0 ? note.getTitle() : note.getTitle() + " (" + (LocalDate.now().getYear() - note.getCreated().getYear()) + " ပီႊ)";
            holder.binding.tvNoteTitle.setText(date);
        } else {
            holder.binding.ivEvent.setImageResource(R.drawable.note);
            holder.binding.tvNoteTitle.setText(note.getTitle());
        }
        holder.binding.ibDeleteNote.setOnClickListener(view -> listener.onNoteDeleted(note));
        holder.binding.tvNoteDay.setText(DateTimeFormatter.ofLocalizedDate(FormatStyle.LONG).format(note.getCreated()));

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {


        AdapterNoteListBinding binding;

        public NoteViewHolder(@NonNull AdapterNoteListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
