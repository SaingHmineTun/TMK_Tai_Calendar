package it.saimao.pakpicalendar.adapters;

import androidx.recyclerview.widget.DiffUtil;

import java.util.List;

import it.saimao.pakpicalendar.database.Note;

public class NoteDiffCallback extends DiffUtil.Callback {

    private final List<Note> newNotes;
    private final List<Note> oldNotes;

    public NoteDiffCallback(List<Note> oldNotes, List<Note> newNotes) {
        this.oldNotes = oldNotes;
        this.newNotes = newNotes;
    }

    @Override
    public int getOldListSize() {
        return oldNotes.size();
    }

    @Override
    public int getNewListSize() {
        return newNotes.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNotes.get(oldItemPosition).getId() == newNotes.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return oldNotes.get(oldItemPosition).equals(newNotes.get(newItemPosition));
    }
}
