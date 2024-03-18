//package com.theodoilamviec.Account.fragments;
//
//import android.os.Bundle;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.theodoilamviec.Account.DB.APP_DATABASE;
//import com.theodoilamviec.Account.Job;
//import com.theodoilamviec.Account.JobNotificationLocal;
//import com.theodoilamviec.Account.adapters.ReminderNotesAdapter;
//import com.theodoilamviec.Project;
//import com.theodoilamviec.ReminderHandleData;
//import com.theodoilamviec.theodoilamviec.R;
//
//import com.theodoilamviec.theodoilamviec.sharedPreferences.SharedPref;
//
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class RemindersFragment extends Fragment implements ReminderHandleData.IGetDataReminder {
//
//    // Bundle
//    Bundle bundle;
//
//    SharedPref sharedPref;
//
//    // REQUEST CODES
//    private final int REQUEST_CODE_ADD_NOTE_OK = 1;
//    private final int REQUEST_CODE_UPDATE_NOTE_OK = 2;
//    private final int REQUEST_CODE_VIEW_NOTE_OK = 3;
//    private final int REQUEST_CODE_UNLOCK_NOTE = 10;
//
//    private RecyclerView reminderNotesRecyclerview;
//
//    // View view
//    View view;
//
//    private List<JobNotificationLocal> notes;
//    private ReminderNotesAdapter reminderNotesAdapter;
//    private ReminderHandleData reminderHandleData = new ReminderHandleData();
//
//    private List<Project> projectsList = new ArrayList<>();
//
//    @Nullable
//    @Override
//    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        view = inflater.inflate(R.layout.fragment_reminders, container, true);
//
//        // initialize bundle
//        bundle = new Bundle();
//        reminderHandleData.getProject(this);
//        // notes recyclerview
//        reminderNotesRecyclerview = view.findViewById(R.id.notes_recyclerview);
//        reminderNotesRecyclerview.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//
//        // notes list, adapter
//        notes = new ArrayList<>();
//        reminderNotesAdapter = new ReminderNotesAdapter();
//        reminderNotesRecyclerview.setAdapter(reminderNotesAdapter);
//      //  List<JobNotificationLocal> jobNotificationList = APP_DATABASE.requestDatabase(requireActivity()).dao().requestAllJobNotificationLocal();
//       // reminderNotesAdapter.submitList(jobNotificationList);
//
//        return view;
//    }
//
//    @Override
//    public void onCreate(@Nullable Bundle savedInstanceState) {
//        sharedPref = new SharedPref(requireContext());
//        super.onCreate(savedInstanceState);
//    }
//
//    @Override
//    public void getProjectList(List<Project> projectList) {
//        projectsList.clear();
//        projectsList.addAll(projectList);
//        reminderHandleData.getJobsList(projectList,this);
//    }
//
//    @Override
//    public void getJobsList(List<Job> jobsList) {
//        reminderHandleData.getJobsList(projectsList,this);
//    }
//
//    @Override
//    public void getJobNotificationList(List<JobNotificationLocal> jobNotificationList) {
//        reminderHandleData.getJobNotificationList(this);
//    }
//}
