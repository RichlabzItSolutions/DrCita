package com.drcita.user.models.viewreceipt;

public class Notes {
    private int noteId;
    private String note;

    public int getNoteId() {
        return noteId;
    }

    public void setNoteId(int noteId) {
        this.noteId = noteId;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public String toString() {
        return "Notes{" +
                "noteId=" + noteId +
                ", note='" + note + '\'' +
                '}';
    }
}
