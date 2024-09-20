package it.saimao.tmktaicalendar.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.saimao.tmktaicalendar.database.Note;
import it.saimao.tmktaicalendar.databinding.AdapterNoteBinding;

public class NoteAdapter extends RecyclerView.Adapter<NoteAdapter.NoteViewHolder> {


    private List<Note> noteList;
    private final NoteClickListener listener;

    public void setNoteList(List<Note> noteList) {
        this.noteList = noteList;
        this.notifyDataSetChanged();
    }

    public interface NoteClickListener {
        void onNoteClicked(Note note);
        void onNoteDeleted(Note note);
    }


    public NoteAdapter(NoteClickListener listener) {
        noteList = new ArrayList<>();
        this.listener = listener;
    }


    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        AdapterNoteBinding binding = AdapterNoteBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new NoteViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {


        Note note = noteList.get(position);
        holder.binding.tvNoteTitle.setText(note.getTitle());
        holder.binding.cvNote.setOnClickListener(view -> {
            listener.onNoteClicked(note);
        });
        holder.binding.ibDeleteNote.setOnClickListener(view -> listener.onNoteDeleted(note));

    }

    @Override
    public int getItemCount() {
        return noteList.size();
    }

    class NoteViewHolder extends RecyclerView.ViewHolder {


        AdapterNoteBinding binding;

        public NoteViewHolder(@NonNull AdapterNoteBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
