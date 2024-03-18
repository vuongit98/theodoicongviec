package com.theodoilamviec.Account.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Account.JobNotification;
import com.theodoilamviec.Account.JobNotificationLocal;
import com.theodoilamviec.theodoilamviec.databinding.ItemNoteTypeListBinding;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReminderNotesAdapter extends RecyclerView.Adapter<ReminderNotesAdapter.NoteViewHolder> {

    private final List<JobNotificationLocal> jobNotificationList = new ArrayList<>();

    private final IClickReminderNotes iClickReminderNotes ;
    public interface IClickReminderNotes {
        void getReminderNote(JobNotificationLocal jobNotificationLocal);
    }

    public ReminderNotesAdapter(IClickReminderNotes iClickReminderNotes) {
        this.iClickReminderNotes = iClickReminderNotes ;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void submitList(List<JobNotificationLocal> data) {
        jobNotificationList.clear();
        jobNotificationList.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new NoteViewHolder(ItemNoteTypeListBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
        holder.set_note(jobNotificationList.get(position), iClickReminderNotes);
    }

    @Override
    public int getItemCount() {
        return jobNotificationList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    static class NoteViewHolder extends RecyclerView.ViewHolder {
        Calendar calendar = Calendar.getInstance();
        ItemNoteTypeListBinding binding;

        NoteViewHolder(@NonNull ItemNoteTypeListBinding note_view) {
            super(note_view.getRoot());
            binding = note_view;
        }

        @SuppressLint("SetTextI18n")
        void set_note(JobNotificationLocal note, IClickReminderNotes iClickReminderNotes) {
            binding.itemNoteTitle.setText(note.getNameJob());
            calendar.setTime(new Date(note.getTimeEnd()));
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int dateOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
            binding.itemNoteCreatedAt.setText("End At: "+dateOfMonth+"/"+month+"/"+year);
            binding.itemNoteLayout.setOnClickListener(e -> {
                iClickReminderNotes.getReminderNote(note);
            });

//            noteTitle.setText(note.getNote_title());
//            noteCreatedAt.setText(note.getNote_created_at());
//
//            if (note.getNote_subtitle() != null) {
//                if (note.getNote_subtitle().trim().isEmpty()) {
//                    noteSubtitle.setVisibility(View.GONE);
//                } else {
//                    noteSubtitle.setText(note.getNote_subtitle());
//                }
//            } else {
//                noteSubtitle.setVisibility(View.GONE);
//            }
//
//            if (!note.getNote_reminder().trim().isEmpty()) {
//                reminderContainer.setVisibility(View.VISIBLE);
//                noteReminder.setText(note.getNote_reminder());
//            } else {
//                reminderContainer.setVisibility(View.GONE);
//                noteReminder.setText("");
//            }
//
//            // set note background
//            GradientDrawable shape = (GradientDrawable) layout.getBackground();
//            if (note.getNote_color() != null) {
//                shape.setColor(Color.parseColor(note.getNote_color()));
//            } else {
//                shape.setColor(Color.parseColor("#ebebeb"));
//            }
//
//            // set note image
//            if (!note.getNote_image_path().trim().isEmpty()) {
//                noteImage.setImageBitmap(BitmapFactory.decodeFile(note.getNote_image_path()));
//                noteImage.setVisibility(View.VISIBLE);
//            } else {
//                noteImage.setImageBitmap(null);
//                noteImage.setVisibility(View.GONE);
//            }
//
//            if (note.isNote_locked()) {
//                noteContainer.setVisibility(View.GONE);
//                noteLocker.setVisibility(View.VISIBLE);
//            } else {
//                noteLocker.setVisibility(View.GONE);
//                noteContainer.setVisibility(View.VISIBLE);
//            }
//
//            // set note background
//            GradientDrawable lockerShape = (GradientDrawable) noteLocker.getBackground();
//            if (note.getNote_color() != null) {
//                lockerShape.setColor(Color.parseColor(note.getNote_color()));
//            } else {
//                lockerShape.setColor(Color.parseColor("#ebebeb"));
//            }
        }

    }

}
