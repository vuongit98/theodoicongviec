package com.theodoilamviec.Account.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Size;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.makeramen.roundedimageview.RoundedImageView;
import com.theodoilamviec.Account.sheets.AttachImageBottomSheetModal;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.DB.APP_DATABASE;

import com.theodoilamviec.theodoilamviec.databinding.ActivityThemChuThichBinding;
import com.theodoilamviec.theodoilamviec.models.Category;
import com.theodoilamviec.theodoilamviec.models.Note;
import com.theodoilamviec.theodoilamviec.models.ReminderReceiver;
import com.theodoilamviec.Account.sheets.CategoriesBottomSheetModal;
import com.theodoilamviec.Account.sheets.MoreActionsBottomSheetModal;
import com.theodoilamviec.Account.sheets.NhacNhoModelSheet;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

public class ThemChuThich extends AppCompatActivity implements MoreActionsBottomSheetModal.OnDeleteListener,
        CategoriesBottomSheetModal.OnChooseListener, NhacNhoModelSheet.OnAddListener,
        NhacNhoModelSheet.OnRemoveListener,AttachImageBottomSheetModal.OnChooseImageListener,
   AttachImageBottomSheetModal.OnChooseVideoListener,
         MoreActionsBottomSheetModal.OnLockListener,
        MoreActionsBottomSheetModal.OnSpeechInputListener {

    // Bundle
    private Bundle bundle;

    // REQUEST CODES
    private static final int REQUEST_DELETE_NOTE_CODE = 3;
    private static final int REQUEST_DISCARD_NOTE_CODE = 4;
    private static final int REQUEST_VIEW_NOTE_IMAGE = 5;
    private static final int REQUEST_VIEW_NOTE_VIDEO = 6;

    /**
     * note fields added to activity
     * note title for note title
     * note subtitle for note subtitle
     * note description for note text & content
     * note created at for note date time
     */

    private EditText noteDescription;
    private TextView noteCreatedAt;

    // Save Note
    private Button noteSave;
    private RoundedImageView noteImage;
    private RoundedImageView noteVideo;
    private LinearLayout noteWebUrlContainer;
    private TextView noteWebUrl;
    private String selectedNoteColor;
    private String selectedImagePath;
    private String selectedVideoPath;
    private int selectedNoteCategory;
    private boolean isLocked = false;
    private String reminderSet;
    private Uri selectedImageUri;
    private Note presetNote;
    private AlarmManager alarm;
    private long alarmStartTime;
    PendingIntent reminderIntent;
    ActivityThemChuThichBinding activityAddNoteBinding;


    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        activityAddNoteBinding = ActivityThemChuThichBinding.inflate(getLayoutInflater());
        setContentView(activityAddNoteBinding.getRoot());

        // initialize bundle
        bundle = new Bundle();


        // alarm manager
        alarm = (AlarmManager)getSystemService(ALARM_SERVICE);
        alarmStartTime = 0;
        reminderSet = "";

        /* note title, text watcher is added
         * to note title to check if it's !Empty
         * then enables the save button. */

        activityAddNoteBinding.noteTitle.addTextChangedListener(noteTitleTextWatcher);

        // note created at
        activityAddNoteBinding.noteCreatedAt.setText(
                new SimpleDateFormat("MM.dd.yyyy, HH:mm a", Locale.getDefault())
                        .format(new Date()));

        // note subtitle

        // note description
        noteDescription = findViewById(R.id.note_description);

        /* note category start */

        selectedNoteCategory = 0;

        /* note category end */

        /* note image start */

        selectedImagePath = "";
        selectedImageUri = Uri.EMPTY;

        activityAddNoteBinding.noteImageRemove.setOnClickListener(v -> {
            Dialog confirmDialog = new Dialog(this);

            confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            confirmDialog.setContentView(R.layout.popup_confirm);

            // enable dialog cancel
            confirmDialog.setCancelable(true);
            confirmDialog.setOnCancelListener(dialog -> confirmDialog.dismiss());

            // confirm header
            TextView confirmHeader = confirmDialog.findViewById(R.id.confirm_header);
            confirmHeader.setText(getString(R.string.remove_image));

            // confirm text
            TextView confirmText = confirmDialog.findViewById(R.id.confirm_text);
            confirmText.setText(getString(R.string.remove_image_description));

            // confirm allow
            TextView confirmAllow = confirmDialog.findViewById(R.id.confirm_allow);
            confirmAllow.setOnClickListener(v1 -> {
                // remove image
                noteImage.setImageBitmap(null);
                findViewById(R.id.note_image_container).setVisibility(View.GONE);
                selectedImagePath = "";
                // dismiss dialog
                confirmDialog.dismiss();
            });

            // confirm cancel
            TextView confirmCancel = confirmDialog.findViewById(R.id.confirm_deny);
            confirmCancel.setOnClickListener(v2 -> confirmDialog.dismiss());

            if (confirmDialog.getWindow() != null) {
                confirmDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                confirmDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            }

            confirmDialog.show();
        });

        selectedVideoPath = "";
        findViewById(R.id.choose_category).setOnClickListener(v -> {
            CategoriesBottomSheetModal categoriesBottomSheetModal = new CategoriesBottomSheetModal();
            categoriesBottomSheetModal.show(getSupportFragmentManager(), "TAG");
        });

        findViewById(R.id.choose_category).setOnLongClickListener(v -> {
            Toast.makeText(this, getString(R.string.choose_category), Toast.LENGTH_SHORT).show();
            return true;
        });

        activityAddNoteBinding.noteSave.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(activityAddNoteBinding.noteTitle.getText())) {
                saveNote( activityAddNoteBinding.noteTitle.getText().toString(), activityAddNoteBinding.noteCreatedAt.getText().toString(), "", selectedNoteColor, activityAddNoteBinding.noteDescription.getText().toString(), selectedImagePath, selectedImageUri.toString(), selectedVideoPath, isLocked);

            } else {
                Toast.makeText(this, getString(R.string.note_title_required), Toast.LENGTH_SHORT).show();
            }
        });

        activityAddNoteBinding.goBack.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(activityAddNoteBinding.noteTitle.getText())) {
                saveNote( activityAddNoteBinding.noteTitle.getText().toString(), activityAddNoteBinding.noteCreatedAt.getText().toString(), "", selectedNoteColor, activityAddNoteBinding.noteDescription.getText().toString(), selectedImagePath, selectedImageUri.toString(), selectedVideoPath, isLocked);

            } else {
                finish();
            }
        });

        /* check if the note is preset (available or exist) */
        if (getIntent().getBooleanExtra("modifier", false)) {
            presetNote = (Note) getIntent().getSerializableExtra("note");
            bundle.putSerializable("note_data", presetNote);
            isModifier();
        }

        findViewById(R.id.more_actions).setOnClickListener(v -> {
            MoreActionsBottomSheetModal moreActionsBottomSheetModal = new MoreActionsBottomSheetModal();
            moreActionsBottomSheetModal.setArguments(bundle);
            moreActionsBottomSheetModal.show(getSupportFragmentManager(), "TAG");
        });

        findViewById(R.id.add_reminder).setOnClickListener(v -> {
            bundle.putString("REMINDER_SET", reminderSet);

            NhacNhoModelSheet reminderBottomSheetModal = new NhacNhoModelSheet();
            reminderBottomSheetModal.setArguments(bundle);
            reminderBottomSheetModal.show(getSupportFragmentManager(), "TAG");
        });
        findViewById(R.id.attach_image).setOnClickListener(v -> {
            AttachImageBottomSheetModal attachImageBottomSheetModal = new AttachImageBottomSheetModal();
            attachImageBottomSheetModal.show(getSupportFragmentManager(), "TAG");
        });

    }

    /**
     * save not into room database
     * table = notes, request_insert.
     * @param title for note title
     * @param created_at for note date
     * @param sub_title for note subtitle
     * @param color for note color
     * @param description for note description
     * @param image for image attachment
     * @param video for video attachment
     */
    private void saveNote(String title, String created_at, String sub_title, String color, String description, String image, String image_uri, String video, boolean locked) {
        final Note note = new Note();

        note.setNote_title(title);
        note.setNote_created_at(created_at);
        note.setNote_subtitle(sub_title);
        note.setNote_color(color);
        note.setNote_description(description);
        note.setNote_image_path(image);
        note.setNote_image_uri(image_uri);
        note.setNote_video_path(video);
        note.setNote_category_id(selectedNoteCategory);
        note.setNote_reminder(reminderSet);
        note.setNote_locked(locked);
        if (presetNote != null) {
            note.setNote_id(presetNote.getNote_id());
        }

        if (alarmStartTime != 0) {
            Objects.requireNonNull(alarm).set(AlarmManager.RTC_WAKEUP, alarmStartTime, reminderIntent);
        }

        @SuppressLint("StaticFieldLeak")
        class SaveNoteTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                APP_DATABASE.requestDatabase(getApplicationContext()).dao().request_insert_note(note);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);

                Intent intent = new Intent();
                setResult(RESULT_OK, intent);
                finish();

            }
        }

        new SaveNoteTask().execute();
    }

    /**
     * text watcher enables save
     * button when note title is not
     * empty, and other required fields
     */
    private final TextWatcher noteTitleTextWatcher = new TextWatcher() {
        @SuppressLint("SetTextI18n")
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @SuppressLint("SetTextI18n")
        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            activityAddNoteBinding.noteSave.setEnabled(!TextUtils.isEmpty(activityAddNoteBinding.noteTitle.getText().toString()));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };



    /**
     * request open attach
     * link popup. after checking
     * the validation of the link,
     * when click on (add link), attach
     * link the note.
     */
    @SuppressLint("SetTextI18n")


    /**
     * request open reminder popup
     * @param title for note title
     * @param subtitle for note subtitle
     */
    private void requestOpenReminder(String title, String subtitle) {
        Dialog addReminderDialog = new Dialog(this);

        addReminderDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        addReminderDialog.setContentView(R.layout.popup_reminder);

        // enable dialog cancel
        addReminderDialog.setCancelable(true);
        addReminderDialog.setOnCancelListener(dialog -> addReminderDialog.dismiss());

        // time picker
        TimePicker timePicker = addReminderDialog.findViewById(R.id.time_picker);

        // confirm allow
        TextView confirmAllow = addReminderDialog.findViewById(R.id.confirm_allow);
        confirmAllow.setOnClickListener(v1 -> {
            Intent intent = new Intent(ThemChuThich.this, ReminderReceiver.class);
            intent.putExtra("notificationId", 1);
            intent.putExtra("title", title);
            intent.putExtra("subtitle", subtitle);

            // getBroadcast(context, requestCode, intent, flags);
            reminderIntent = PendingIntent.getBroadcast(ThemChuThich.this, 0,
                    intent, PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            // get current date
            Date date = Calendar.getInstance().getTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            String formattedDate = simpleDateFormat.format(date);

            // get reminder data
            String am_pm = "";
            int hour = timePicker.getCurrentHour();
            int minute = timePicker.getCurrentMinute();

            // create time
            Calendar startTime = Calendar.getInstance();
            startTime.set(Calendar.HOUR_OF_DAY, hour);
            startTime.set(Calendar.MINUTE, minute);
            startTime.set(Calendar.SECOND, 0);
            alarmStartTime = startTime.getTimeInMillis();

            if (startTime.get(Calendar.AM_PM) == Calendar.PM) {
                am_pm = "PM";
            } else if (startTime.get(Calendar.AM_PM) == Calendar.AM) {
                am_pm = "AM";
            }

            String formattedHour = (startTime.get(Calendar.HOUR) == 0) ? "12" : Integer.toString(startTime.get(Calendar.HOUR));

            reminderSet = formattedDate + " " + am_pm + " " + formattedHour + ":" + minute;

            findViewById(R.id.reminder_set).setVisibility(View.VISIBLE);
            TextView reminderText = findViewById(R.id.reminder_set_text);
            reminderText.setText(reminderSet);

            Toast.makeText(this, getString(R.string.reminder_set_successfully), Toast.LENGTH_SHORT).show();

            addReminderDialog.dismiss();
        });

        // confirm cancel
        TextView confirmCancel = addReminderDialog.findViewById(R.id.confirm_deny);
        confirmCancel.setOnClickListener(v2 -> addReminderDialog.dismiss());

        if (addReminderDialog.getWindow() != null) {
            addReminderDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            addReminderDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
            addReminderDialog.getWindow().getAttributes().windowAnimations = R.style.DetailAnimation;
            Window window = addReminderDialog.getWindow();
            WindowManager.LayoutParams WLP = window.getAttributes();
            WLP.gravity = Gravity.BOTTOM;
            window.setAttributes(WLP);
        }

        addReminderDialog.show();
    }

    /**
     * request for the file path
     * @param uri for uri
     * @return file path (images, videos allowed)
     */
    private String requestFilePath(Uri uri) {
        @SuppressLint("Recycle") Cursor returnCursor = getContentResolver().query(uri, null, null, null, null);

        int nameIndex = Objects.requireNonNull(returnCursor).getColumnIndex(OpenableColumns.DISPLAY_NAME);
        int sizeIndex = returnCursor.getColumnIndex(OpenableColumns.SIZE);
        returnCursor.moveToFirst();

        String name = (returnCursor.getString(nameIndex));
        String size = (Long.toString(returnCursor.getLong(sizeIndex)));

        File file = new File(getFilesDir(), name);
        try {
            InputStream inputStream = getContentResolver().openInputStream(uri);
            FileOutputStream outputStream = new FileOutputStream(file);

            int read = 0;
            int maxBufferSize = 1 * 1024 * 1024;
            int bytesAvailable = Objects.requireNonNull(inputStream).available();

            int bufferSize = Math.min(bytesAvailable, maxBufferSize);

            final byte[] buffers = new byte[bufferSize];
            while ((read = inputStream.read(buffers)) != -1) {
                outputStream.write(buffers, 0, read);
            }
            Log.e("File Size", "Size " + file.length());
            Log.e("Size", "Size: " + size);
            inputStream.close();
            outputStream.close();
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        return file.getPath();
    }

    /**
     * check whether the note
     * is preset (available) or
     * not to apply given data
     */
    private void isModifier() {
        activityAddNoteBinding.noteTitle.setText(presetNote.getNote_title());

        activityAddNoteBinding.noteDescription.setText(presetNote.getNote_description());
        activityAddNoteBinding.noteCreatedAt.setText(presetNote.getNote_created_at());
        selectedNoteColor = presetNote.getNote_color();
        selectedNoteCategory = presetNote.getNote_category_id();
        reminderSet = presetNote.getNote_reminder();
        isLocked = presetNote.isNote_locked();

        // check if image attachment is set
        if (presetNote.getNote_image_path() != null && !presetNote.getNote_image_path().trim().isEmpty()
                && presetNote.getNote_image_uri() != null) {
            activityAddNoteBinding.noteImage.setImageBitmap(BitmapFactory.decodeFile(presetNote.getNote_image_path()));
            findViewById(R.id.note_image_container).setVisibility(View.VISIBLE);
            selectedImagePath = presetNote.getNote_image_path();
            selectedImageUri = Uri.parse(presetNote.getNote_image_uri());
        }

        // check if video attachment is set
        if (presetNote.getNote_video_path() != null && !presetNote.getNote_video_path().trim().isEmpty()) {
            Bitmap video_thumbnail = ThumbnailUtils.createVideoThumbnail(presetNote.getNote_video_path(), MediaStore.Video.Thumbnails.FULL_SCREEN_KIND);
            noteVideo.setImageBitmap(video_thumbnail);
            findViewById(R.id.note_video_container).setVisibility(View.VISIBLE);
            selectedVideoPath = presetNote.getNote_video_path();
        }

        // check if link attachment is set
        if (presetNote.getNote_web_link() != null && !presetNote.getNote_web_link().trim().isEmpty()) {
            noteWebUrl.setText(presetNote.getNote_web_link());
            noteWebUrlContainer.setVisibility(View.VISIBLE);
        }

        // check if reminder is set
        if (presetNote.getNote_reminder() != null) {
            if (!presetNote.getNote_reminder().trim().isEmpty()) {
                findViewById(R.id.reminder_set).setVisibility(View.VISIBLE);
                TextView reminderText = findViewById(R.id.reminder_set_text);
                reminderText.setText(presetNote.getNote_reminder());
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_VIEW_NOTE_IMAGE && resultCode == RESULT_OK) {
            if (data != null) {
                if (Objects.requireNonNull(data.getStringExtra("request")).equals("remove_image")) {
                    // remove image
                    noteImage.setImageBitmap(null);
                    findViewById(R.id.note_image_container).setVisibility(View.GONE);
                    selectedImagePath = "";
                }
            }
        } else if (requestCode == REQUEST_VIEW_NOTE_VIDEO && resultCode == RESULT_OK) {
            if (data != null) {
                if (data.getStringExtra("request").equals("remove_video")) {
                    // remove video
                    noteVideo.setImageBitmap(null);
                    findViewById(R.id.note_video_container).setVisibility(View.GONE);
                    selectedVideoPath = "";
                }
            }
        }
    }
    @Override
    public void onDeleteListener(int requestCode) {
        /* check if the returned request code
        * belongs to a deleted note then close
        * the activity and refresh the recycler view */
        if (requestCode == REQUEST_DELETE_NOTE_CODE) {
            Intent intent = new Intent();
            intent.putExtra("is_note_removed", true);
            setResult(RESULT_OK, intent);
            Toast.makeText(this, getString(R.string.note_moved_to_trash), Toast.LENGTH_SHORT).show();
            finish();
        } else if (requestCode == REQUEST_DISCARD_NOTE_CODE) {
            finish();
        }
    }

    @Override
    public void onChooseListener(int requestCode, Category category) {
        int REQUEST_CHOOSE_CATEGORY_CODE = 5;
        if (requestCode == REQUEST_CHOOSE_CATEGORY_CODE) {
            if (category != null) {
                selectedNoteCategory = category.getCategory_id();
                TextView note_category = findViewById(R.id.note_category);
                note_category.setText(category.getCategory_title());

                Toast.makeText(this, category.getCategory_title() + " " + getString(R.string.category_is_selected), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onAddListener(int requestCode) {
        if (!TextUtils.isEmpty(activityAddNoteBinding.noteTitle.getText().toString()) ) {
            requestOpenReminder(activityAddNoteBinding.noteTitle.getText().toString(), "");
        } else {
            Toast.makeText(this, getString(R.string.note_title_subtitle_empty), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRemoveListener(int requestCode) {
        reminderSet = "";
        findViewById(R.id.reminder_set).setVisibility(View.GONE);
        TextView reminderText = findViewById(R.id.reminder_set_text);
        reminderText.setText("");

        Toast.makeText(this, getString(R.string.reminder_removed), Toast.LENGTH_SHORT).show();
    }

    /**
     * load Google AdMob rewarded ad
     */

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        if (!TextUtils.isEmpty(activityAddNoteBinding.noteTitle.getText())) {
            finish();
            saveNote(activityAddNoteBinding.noteTitle.getText().toString(), noteCreatedAt.getText().toString(), "", selectedNoteColor, noteDescription.getText().toString(), selectedImagePath, selectedImageUri.toString(), selectedVideoPath, isLocked);

        } else {
            finish();
        }
    }



    @Override
    public void onLockListener(int requestCode) {
        if (requestCode == MoreActionsBottomSheetModal.REQUEST_LOCK_NOTE_CODE) {
            isLocked = true;
        } else if (requestCode == MoreActionsBottomSheetModal.REQUEST_UNLOCK_NOTE_CODE) {
            isLocked = false;
        }
    }

    @Override
    public void onSpeechInputListener(int requestCode, String text) {
        if (requestCode == MoreActionsBottomSheetModal.REQUEST_SPEECH_INPUT_CODE) {
            noteDescription.append(" " + text);
        }
    }

    @Override
    public void onChooseImageListener(int requestCode, Bitmap bitmap, Uri uri) {
        if (requestCode == AttachImageBottomSheetModal.REQUEST_SELECT_IMAGE_FROM_GALLERY_CODE
                || requestCode == AttachImageBottomSheetModal.REQUEST_CAMERA_IMAGE_CODE) {
            // set image
            activityAddNoteBinding.noteImage.setImageBitmap(bitmap);
            findViewById(R.id.note_image_container).setVisibility(View.VISIBLE);
            // request image path
            selectedImagePath = requestFilePath(uri);
            // request image uri
            selectedImageUri = uri;
        }
    }

    @Override
    public void onChooseVideoListener(int requestCode, Uri uri) {
        if (requestCode == AttachImageBottomSheetModal.REQUEST_SELECT_VIDEO_FROM_GALLERY_CODE) {
            // request video thumbnail
            Bitmap video_thumbnail = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.Q) {
                try {
                    video_thumbnail = ThumbnailUtils.createVideoThumbnail(new File(requestFilePath(uri)), new Size(96,96),new CancellationSignal());

                }catch (Exception ex){
                    System.out.println("Error = "+ ex.getMessage());
                }
            }
            // set video image
            activityAddNoteBinding.noteVideo.setImageBitmap(video_thumbnail);
            findViewById(R.id.note_video_container).setVisibility(View.VISIBLE);
            // request video path
            selectedVideoPath = requestFilePath(uri);
        }
    }
}