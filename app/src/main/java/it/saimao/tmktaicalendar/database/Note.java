package it.saimao.tmktaicalendar.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

@Entity
public class Note implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private boolean everyYear;
    private LocalDate created;
    private LocalDate updated;

    public Note() {

    }

    @Ignore
    public Note(String title, String description, boolean everyYear, LocalDate date) {
        this.title = title;
        this.description = description;
        this.everyYear = everyYear;
        this.created = date;
        this.updated = LocalDate.now();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getCreated() {
        return created;
    }

    public void setCreated(LocalDate created) {
        this.created = created;
    }

    public LocalDate getUpdated() {
        return updated;
    }

    public void setUpdated(LocalDate updated) {
        this.updated = updated;
    }

    public boolean isEveryYear() {
        return everyYear;
    }

    public void setEveryYear(boolean everyYear) {
        this.everyYear = everyYear;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Note note)) return false;
        return id == note.id && everyYear == note.everyYear && Objects.equals(title, note.title) && Objects.equals(description, note.description) && created.isEqual(note.getCreated()) && updated.isEqual(note.getUpdated());
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, description, everyYear, created, updated);
    }
}
