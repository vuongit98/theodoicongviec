//package com.theodoilamviec.Account.adapters;
//
//import android.graphics.BitmapFactory;
//import android.graphics.Color;
//import android.graphics.drawable.GradientDrawable;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.FrameLayout;
//import android.widget.LinearLayout;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.makeramen.roundedimageview.RoundedImageView;
//import com.theodoilamviec.Account.Job;
//import com.theodoilamviec.Account.listeners.TrashNotesListener;
//import com.theodoilamviec.theodoilamviec.R;
//import com.theodoilamviec.theodoilamviec.models.TrashNote;
//
//
//import java.util.Calendar;
//import java.util.Date;
//import java.util.List;
//
//public class TrashNotesAdapter extends RecyclerView.Adapter<TrashNotesAdapter.NoteViewHolder> {
//
//    private final List<Job> trashNotes;
//    private final TrashNotesListener trashNotesListener;
//
//    public TrashNotesAdapter(List<Job> trashNotes, TrashNotesListener trashNotesListener) {
//        this.trashNotes = trashNotes;
//        this.trashNotesListener = trashNotesListener;
//    }
//
//    @NonNull
//    @Override
//    public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        return new NoteViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_type_list, parent, false));
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull NoteViewHolder holder, int position) {
//        holder.setNote(trashNotes.get(position));
//        holder.layout.setOnClickListener(v -> trashNotesListener.onNoteClicked(trashNotes.get(position), position));
//        holder.layout.setOnLongClickListener(v -> {
//            trashNotesListener.onNoteLongClicked(trashNotes.get(position), position);
//            return true;
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return trashNotes.size();
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    static class NoteViewHolder extends RecyclerView.ViewHolder {
//
//        public RelativeLayout layout;
//        public LinearLayout noteContainer;
//
//        TextView noteTitle;
//        TextView noteCreatedAt;
//        TextView noteSubtitle;
//
//        RoundedImageView noteImage;
//
//        FrameLayout noteLocker;
//        private final Calendar calendar = Calendar.getInstance();
//
//        NoteViewHolder(@NonNull View note_view) {
//            super(note_view);
//
//            layout = note_view.findViewById(R.id.item_note_layout);
//            noteContainer = note_view.findViewById(R.id.item_note_container);
//
//            noteTitle = note_view.findViewById(R.id.item_note_title);
//            noteCreatedAt = note_view.findViewById(R.id.item_note_created_at);
//            noteSubtitle = note_view.findViewById(R.id.item_note_subtitle);
//
//            noteImage = note_view.findViewById(R.id.item_note_image);
//
//            noteLocker = note_view.findViewById(R.id.item_note_locker);
//        }
//
//        void setNote(Job job) {
//            noteTitle.setText(job.getNameJob());
//            noteSubtitle.setVisibility(View.GONE);
//            calendar.setTime(new Date(job.getTimeEndDate()));
//
////            noteCreatedAt.setText();
//
//        }
//
//    }
//
//}
