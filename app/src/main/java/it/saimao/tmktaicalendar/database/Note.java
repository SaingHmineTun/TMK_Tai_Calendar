package it.saimao.tmktaicalendar.database;

import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.time.LocalDate;

@Entity
public class Note {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String title;
    private String description;
    private LocalDate created;
    private LocalDate updated;

    public Note() {

    }

    @Ignore
    public Note(String title, String description, LocalDate date) {
        this.title = title;
        this.description = description;
        this.created = date;
        this.updated = created;
    }

    @Ignore
    public Note(String title, String description) {
        this.title = title;
        this.description = description;
        this.created = LocalDate.now();
        this.updated = created;
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
}
