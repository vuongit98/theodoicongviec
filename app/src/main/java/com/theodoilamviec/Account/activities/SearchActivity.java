package com.theodoilamviec.Account.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;


import com.theodoilamviec.Account.listeners.NotesActionListener;
import com.theodoilamviec.Account.listeners.NotesListener;
import com.theodoilamviec.theodoilamviec.R;

import com.theodoilamviec.Account.adapters.NotesAdapter;
import com.theodoilamviec.theodoilamviec.DB.APP_DATABASE;
import com.theodoilamviec.theodoilamviec.databinding.ActivitySearchBinding;
import com.theodoilamviec.theodoilamviec.models.Note;

import com.theodoilamviec.theodoilamviec.sharedPreferences.SharedPref;
import com.theodoilamviec.Account.sheets.ImplementedPasswordBottomSheetModal;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class SearchActivity extends AppCompatActivity implements NotesListener, NotesActionListener, ImplementedPasswordBottomSheetModal.OnUnlockListener {

    Bundle bundle;

    // REQUEST CODES
    private final int REQUEST_CODE_UPDATE_NOTE_OK = 2;
    private final int REQUEST_CODE_TEXT_TO_SPEECH = 4;

    private List<Note> notes;
    private NotesAdapter notesAdapter;

    // selected filter
    private String selectedFilterResource;
    private char selectedFilter;

    boolean isClosed = false;
    SharedPref sharedPref;



    int noteClickedPosition = -1;
    ActivitySearchBinding searchBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        searchBinding = ActivitySearchBinding.inflate(getLayoutInflater());


        setContentView(searchBinding.getRoot());

        // bundle
        bundle = new Bundle();


        // return back and finish activity

        searchBinding.goBack.setOnClickListener(v -> {
            startActivity(new Intent(SearchActivity.this, MainActivity.class));

            finish();
        });


        searchBinding.notesRecyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));

        // notes list, adapter
        notes = new ArrayList<>();
        notesAdapter = new NotesAdapter(this,notes, this, this);
        searchBinding.notesRecyclerview.setAdapter(notesAdapter);

        // check if notes are not empty
        if (notes.size() != 0) {
            notes.clear();
            notesAdapter.notifyDataSetChanged();
        }

        // selected filter
        // default -> global
        selectedFilterResource = "";
        selectedFilter = 'G'; // FOR GLOBAL

        // search bar

        searchBinding.searchBar.addTextChangedListener(searchTextWatcher);

        searchBinding.searchMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, getString(R.string.speak_notes));

            try {
                startActivityForResult(intent, REQUEST_CODE_TEXT_TO_SPEECH);
            } catch (Exception e) {
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void requestSetSearchAction() {
        isClosed = false;
        findViewById(R.id.search_content_container).setVisibility(View.GONE);
        searchBinding.closeSearch.setVisibility(View.VISIBLE);
        searchBinding.searchBar.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        searchBinding.closeSearch.setOnClickListener(v -> request_close_search());
    }

    private void request_close_search() {

        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        Objects.requireNonNull(inputMethodManager).hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);

        findViewById(R.id.close_search).setVisibility(View.GONE);
        searchBinding.goBack.setVisibility(View.VISIBLE);
        findViewById(R.id.search_content_container).setVisibility(View.VISIBLE);

        selectedFilterResource = "";
        selectedFilter = 'G'; // FOR GLOBAL

        isClosed = true;
        searchBinding.searchBar.getText().clear();
    }

    private void request_search_notes(String keyword) {
        @SuppressLint("StaticFieldLeak")
        class GetNotesTask extends AsyncTask<Void, Void, List<Note>> {

            @Override
            protected List<Note> doInBackground(Void... voids) {
                switch (selectedFilter) {
                    case 'C':
                        return APP_DATABASE.requestDatabase(getApplicationContext()).dao().request_search_notes_by_color(keyword, selectedFilterResource);
                    case 'T':
                        switch (selectedFilterResource) {
                            case "_images":
                                return APP_DATABASE.requestDatabase(getApplicationContext()).dao().request_search_notes_by_images(keyword);
                            case "_videos":
                                return APP_DATABASE.requestDatabase(getApplicationContext()).dao().request_search_notes_by_video(keyword);
                            case "_reminders":
                                return APP_DATABASE.requestDatabase(getApplicationContext()).dao().request_search_notes_by_reminder(keyword);
                        }
                    case 'G':
                    default:
                        return APP_DATABASE.requestDatabase(getApplicationContext()).dao().request_search_notes_by_global(keyword);
                }
            }

            @Override
            protected void onPostExecute(List<Note> notes_inline) {
                super.onPostExecute(notes_inline);
                notes.addAll(notes_inline);
                notesAdapter.notifyDataSetChanged();
            }

        }
        new GetNotesTask().execute();
    }

    /**
     * text watcher for notes search bar
     */
    private final TextWatcher searchTextWatcher = new TextWatcher() {
        @SuppressLint("SetTextI18n")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (count == 0) {
                requestSetSearchAction();
            }
        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            notes.clear();
            notesAdapter.notifyDataSetChanged();
        }

        @Override
        public void afterTextChanged(Editable s) {

            if (!isClosed) {
                request_search_notes(s.toString());
            }
        }
    };

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_TEXT_TO_SPEECH) {
            if (resultCode == RESULT_OK && null != data) {
                ArrayList<String> result = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                if (result != null) {
                    searchBinding.searchBar.setText(result.get(0));
                }
            }
        } else if (requestCode == REQUEST_CODE_UPDATE_NOTE_OK && resultCode == RESULT_OK) {
            notes.clear();
            notesAdapter.notifyDataSetChanged();
            request_search_notes(searchBinding.searchBar.getText().toString());
        }
    }

    @Override
    public void onNoteAction(Note note, int position, boolean isSelected) {
        noteClickedPosition = position;

        if (sharedPref.loadNotePinCode() == 0) {
            Intent intent = new Intent(SearchActivity.this, ThemChuThich.class);
            intent.putExtra("modifier", true);
            intent.putExtra("note", note);
            startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE_OK);
        } else {
            if (note.isNote_locked()) {
                bundle.putSerializable("data", note);
                bundle.putInt("activity", 1);
                ImplementedPasswordBottomSheetModal passwordBottomSheetModal = new ImplementedPasswordBottomSheetModal();
                passwordBottomSheetModal.setArguments(bundle);
                passwordBottomSheetModal.show(getSupportFragmentManager(), "TAG");
            } else {
                Intent intent = new Intent(SearchActivity.this, ThemChuThich.class);
                intent.putExtra("modifier", true);
                intent.putExtra("note", note);
                startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE_OK);
            }
        }
    }

    @Override
    public void onNoteClicked(Note note, int position) {

    }

    @Override
    public void onNoteLongClicked(Note note, int position) {

    }

    @Override
    public void onUnlockListener(Note note) {
        Intent intent = new Intent(SearchActivity.this, ThemChuThich.class);
        intent.putExtra("modifier", true);
        intent.putExtra("note", note);
        startActivityForResult(intent, REQUEST_CODE_UPDATE_NOTE_OK);
    }

    /**
     * request initialize type search
     * by setting filter for search
     */


    /**
     * request initialize color search
     * by setting filter for search
     */


    @Override
    public void onBackPressed() {
        if (findViewById(R.id.close_search).getVisibility() == View.VISIBLE) {
            request_close_search();
        } else {
            finish();
        }
    }
}