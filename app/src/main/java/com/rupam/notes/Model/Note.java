package com.rupam.notes.Model;

public class Note {

    public String title;
    public String noteBody;
    public String name;
    public String dateAdded;
    public String key;

    public Note() {
    }

    public Note(String title, String noteBody, String name, String dateAdded, String key) {
        this.title = title;
        this.noteBody = noteBody;
        this.name = name;
        this.dateAdded = dateAdded;
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public void setNoteBody(String noteBody) {
        this.noteBody = noteBody;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
