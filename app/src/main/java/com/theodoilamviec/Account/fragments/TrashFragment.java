package com.theodoilamviec.Account.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.theodoilamviec.Account.DB.APP_DATABASE;
import com.theodoilamviec.Account.Job;
import com.theodoilamviec.Account.activities.TrashActivity;
import com.theodoilamviec.theodoilamviec.InfoJobActivity;
import com.theodoilamviec.theodoilamviec.R;
import com.theodoilamviec.theodoilamviec.adapter.JobAdapter;
import com.theodoilamviec.theodoilamviec.models.TrashNote;
import com.theodoilamviec.theodoilamviec.utils.GridSpacingItemDecoration;

import java.util.ArrayList;
import java.util.List;

public class TrashFragment extends Fragment implements JobAdapter.IClickJob, TrashFragmentManager.IProjectData {

    // Bundle
    Bundle bundle;

    // View view
    View view;
    JobAdapter jobAdapter;
    TrashFragmentManager trashFragmentManager;
    private List<Job> jobsList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_trash, container, false);

        // initialize bundle
        bundle = new Bundle();
        jobAdapter = new JobAdapter(this);
        trashFragmentManager = new TrashFragmentManager(this);

        // notes recyclerview
        RecyclerView trashNotesRecyclerview = view.findViewById(R.id.notes_recyclerview);
        trashNotesRecyclerview.setLayoutManager(new GridLayoutManager(getContext(), 2));
        trashNotesRecyclerview.addItemDecoration(new GridSpacingItemDecoration(2,10, true));
        trashNotesRecyclerview.setAdapter(jobAdapter);

        trashFragmentManager.getAllProject();

        // delete all trash notes
        TrashActivity.extraAction.setOnClickListener(v -> {
            Dialog confirmDialog = new Dialog(requireContext());

            confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

            confirmDialog.setContentView(R.layout.popup_confirm);

            // enable dialog cancel
            confirmDialog.setCancelable(true);
            confirmDialog.setOnCancelListener(dialog -> confirmDialog.dismiss());

            // confirm header
            TextView confirmHeader = confirmDialog.findViewById(R.id.confirm_header);
            confirmHeader.setText(getString(R.string.delete_all_notes));

            // confirm text
            TextView confirmText = confirmDialog.findViewById(R.id.confirm_text);
            confirmText.setText(getString(R.string.delete_all_notes_description));

            // confirm allow
            TextView confirmAllow = confirmDialog.findViewById(R.id.confirm_allow);
            confirmAllow.setOnClickListener(v1 -> {
                requestDeleteAllTrashNotes();
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

        return view;
    }


    /**
     * request to delete all trash
     * notes records from database
     */
    private void requestDeleteAllTrashNotes() {
        trashFragmentManager.deleteAll(jobsList);
        jobsList.forEach(it -> APP_DATABASE.requestDatabase(requireActivity()).dao().deleteJobNotificationLocal(it.getIdJob(), it.getIdProject()));
        jobsList.clear();
        jobAdapter.submitList(jobsList);
    }
    @Override
    public void getJob(Job job) {
        Intent intent = new Intent(requireActivity(), InfoJobActivity.class);
        intent.putExtra("isMenu", 1);
        Bundle bundle = new Bundle();
        bundle.putParcelable("job", job);
        intent.putExtra("bundle", bundle);
        startActivity(intent);
    }

    @Override
    public void getListIdProject(List<String> idProjectList) {
        for (String id : idProjectList) {
            trashFragmentManager.getAllJobByIdProject(id);
        }
    }

    @Override
    public void getListJobsById(List<Job> jobList) {
        jobsList.addAll(jobList);
        jobAdapter.submitList(jobsList);
    }
}
