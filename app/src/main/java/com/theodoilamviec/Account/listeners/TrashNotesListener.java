package com.theodoilamviec.Account.listeners;

import com.theodoilamviec.theodoilamviec.models.TrashNote;

public interface TrashNotesListener {
    void onNoteClicked(TrashNote trashNote, int position);

    void onNoteLongClicked(TrashNote trashNote, int position);
}
