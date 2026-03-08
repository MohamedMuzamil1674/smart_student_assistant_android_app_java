package com.example.smartstudentassistant.notesdrivelink;

public class NotesModel {
    String notesTitle;

    String notesLinks;

    public NotesModel(){
        /* Default constructor required for calling */
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public String getNotesLinks() {
        return notesLinks;
    }

    public void setNotesLinks(String notesLinks) {
        this.notesLinks = notesLinks;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }
}
