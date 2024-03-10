package com.theodoilamviec.Account.listeners;

import com.theodoilamviec.theodoilamviec.models.Note;

public interface NotesActionListener {
    void onNoteAction(Note note, int position, boolean isSelected);
}
