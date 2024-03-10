package com.theodoilamviec.Account.listeners;

import com.theodoilamviec.theodoilamviec.models.Note;

public interface NotesListener {
    void onNoteClicked(Note note, int position);

    void onNoteLongClicked(Note note, int position);
}
