package com.theodoilamviec.Account.sheets;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.theodoilamviec.Account.activities.TaoMatKhau;
import com.theodoilamviec.theodoilamviec.R;

import com.theodoilamviec.theodoilamviec.DB.APP_DATABASE;
import com.theodoilamviec.theodoilamviec.models.Note;
import com.theodoilamviec.theodoilamviec.models.TrashNote;
import com.theodoilamviec.theodoilamviec.sharedPreferences.SharedPref;


import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

public class MoreActionsBottomSheetModal extends BottomSheetDialogFragment {

    Bundle bundle;

    SharedPref sharedPref;

    public interface OnDeleteListener {
        void onDeleteListener(int requestCode);
    }

    public interface OnLockListener {
        void onLockListener(int requestCode);
    }

    public interface OnSpeechInputListener {
        void onSpeechInputListener(int requestCode, String text);
    }

    private final int REQUEST_DELETE_NOTE_CODE = 3;
    private final int REQUEST_DISCARD_NOTE_CODE = 4;
    public static final int REQUEST_LOCK_NOTE_CODE = 5;
    public static final int REQUEST_UNLOCK_NOTE_CODE = 6;
    private final int REQUEST_SET_PIN_CODE = 6;
    private final int REQUEST_CODE_TEXT_TO_SPEECH = 7;
    public static final int REQUEST_SPEECH_INPUT_CODE = 8;

    private Note note;

    OnDeleteListener onDeleteListener;
    OnLockListener onLockListener;
    OnSpeechInputListener onSpeechInputListener;

    public MoreActionsBottomSheetModal() {

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            onDeleteListener = (OnDeleteListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onDeleteListener");
        }

        try {
            onLockListener = (OnLockListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onLockListener");
        }

        try {
            onSpeechInputListener = (OnSpeechInputListener) context;
        } catch (final ClassCastException e) {
            throw new ClassCastException(context.toString() + " must implement onSpeechInputListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.more_actions_bottom_sheet_modal, container, false);

        note = (Note) requireArguments().getSerializable("note_data");

        // delete note
        LinearLayout deleteNote = view.findViewById(R.id.delete_note);
        deleteNote.setOnClickListener(v -> requestPresetTrashNote());

        // lock note
        LinearLayout lockNote = view.findViewById(R.id.lock_note);
        lockNote.setOnClickListener(v -> {
            if (sharedPref.loadNotePinCode() == 0) {
                // no password pin set
                startActivityForResult(new Intent(getContext(), TaoMatKhau.class), REQUEST_SET_PIN_CODE);
            } else {
                if (note != null) {
                    if (note.isNote_locked()) {
                        // set password and lock note
                        Toast.makeText(getContext(), getString(R.string.note_unlocked), Toast.LENGTH_SHORT).show();
                        onLockListener.onLockListener(REQUEST_UNLOCK_NOTE_CODE);
                    } else {
                        // set password and lock note
                        Toast.makeText(getContext(), getString(R.string.note_locked), Toast.LENGTH_SHORT).show();
                        onLockListener.onLockListener(REQUEST_LOCK_NOTE_CODE);
                    }
                    dismiss();
                } else {
                    Toast.makeText(getContext(), getString(R.string.save_note_before_lock), Toast.LENGTH_SHORT).show();
                }
            }
        });
        TextView lockNoteText = view.findViewById(R.id.lock_note_text);
        if (note != null) {
            if (note.isNote_locked()) {
                lockNoteText.setText(getString(R.string.unlock));
            } else {
                lockNoteText.setText(getString(R.string.lock));
            }
        } else {
            lockNoteText.setText(getString(R.string.lock));
        }




        // copy note to clipboard
        LinearLayout copyToClipBoard = view.findViewById(R.id.copy_to_clipboard);
        copyToClipBoard.setOnClickListener(v -> {
            if (note != null) {
                // get note details
                String note_title = note.getNote_title();
                String note_subtitle = note.getNote_subtitle();
                String note_description = note.getNote_description();
                // format note
                String note_text = "Note title: " + note_title + "\n\nNote subtitle: " + note_subtitle + "\n\nNote description: " + note_description;
                // copy to clipboard
                ClipboardManager clipboard = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData data = ClipData.newPlainText("Copy Note", note_text);
                clipboard.setPrimaryClip(data);
                Toast.makeText(getContext(), getString(R.string.copied_to_clipboard), Toast.LENGTH_SHORT).show();
                // dismiss bottom sheet
                dismiss();
            } else {
                Toast.makeText(getContext(), getString(R.string.save_note_before_copy), Toast.LENGTH_SHORT).show();
            }
        });

        // export note as


        // speech to text
        LinearLayout speechToText = view.findViewById(R.id.speech_to_text);
        speechToText.setOnClickListener(v -> {
            if (note != null) {
                Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
                intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_notes));

                try {
                    startActivityForResult(intent, REQUEST_CODE_TEXT_TO_SPEECH);
                } catch (Exception e) {
                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(getContext(), getString(R.string.save_note_before_speech), Toast.LENGTH_SHORT).show();
            }
        });
        LinearLayout shareNote = view.findViewById(R.id.share);
        shareNote.setOnClickListener(v -> {
            if (note != null) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, note.getNote_description());
                startActivity(Intent.createChooser(share, getString(R.string.share_note)));
            } else {
                Toast.makeText(getContext(), getString(R.string.save_note_before_share), Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        sharedPref = new SharedPref(requireContext());
        super.onCreate(savedInstanceState);

        bundle = new Bundle();
    }



    /**
     * request save data from note class
     * to trash note class
     */
    private void requestPresetTrashNote() {
        if (note != null) {
            final TrashNote presetTrashNote = new TrashNote();
            presetTrashNote.setNote_id(note.getNote_id());
            presetTrashNote.setNote_title(note.getNote_title());
            presetTrashNote.setNote_created_at(note.getNote_created_at());
            presetTrashNote.setNote_subtitle(note.getNote_subtitle());
            presetTrashNote.setNote_description(note.getNote_description());
            presetTrashNote.setNote_image_path(note.getNote_image_path());
            presetTrashNote.setNote_color(note.getNote_color());
            presetTrashNote.setNote_web_link(note.getNote_web_link());
            presetTrashNote.setNote_category_id(note.getNote_category_id());
            presetTrashNote.setNote_reminder(note.getNote_reminder());
            presetTrashNote.setNote_locked(note.isNote_locked());

            requestMoveNoteToTrash(presetTrashNote);
        } else {
            onDeleteListener.onDeleteListener(REQUEST_DISCARD_NOTE_CODE);
            Toast.makeText(getContext(), getString(R.string.note_discarded), Toast.LENGTH_SHORT).show();
            dismiss();
        }
    }

    /** request move the preset note
     * to trash before delete
     * @param trash_note for class
     */
    private void requestMoveNoteToTrash(TrashNote trash_note) {
        if (note != null) {
            @SuppressLint("StaticFieldLeak")
            class MoveNoteTask extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... voids) {
                    APP_DATABASE.requestDatabase(getContext()).dao().request_insert_trash_note(trash_note);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    requestDeleteNote(note);
                    dismiss();
                }
            }

            new MoveNoteTask().execute();
        }
    }

    /**
     * request to delete a preset note
     * @param note for class
     */
    private void requestDeleteNote(Note note) {
        if (note != null) {
            @SuppressLint("StaticFieldLeak")
            class DeleteNoteTask extends AsyncTask<Void, Void, Void> {
                @Override
                protected Void doInBackground(Void... voids) {
                    APP_DATABASE.requestDatabase(getContext()).dao().request_delete_note(note);
                    return null;
                }

                @Override
                protected void onPostExecute(Void aVoid) {
                    super.onPostExecute(aVoid);
                    onDeleteListener.onDeleteListener(REQUEST_DELETE_NOTE_CODE);
                    dismiss();
                }
            }

            new DeleteNoteTask().execute();
        } else {
            onDeleteListener.onDeleteListener(REQUEST_DISCARD_NOTE_CODE);
            dismiss();
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_SET_PIN_CODE) {
            if (data != null) {
                if (Objects.equals(data.getStringExtra("result"), "lock")) {
                    onLockListener.onLockListener(REQUEST_LOCK_NOTE_CODE);
                }
            }
        } else if (requestCode == REQUEST_CODE_TEXT_TO_SPEECH) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null) {
                    onSpeechInputListener.onSpeechInputListener(REQUEST_SPEECH_INPUT_CODE, result.get(0));
                    dismiss();
                }
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 1000) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), getString(R.string.permission_granted), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), getString(R.string.permission_denied), Toast.LENGTH_SHORT).show();
            }
        }
    }
}
