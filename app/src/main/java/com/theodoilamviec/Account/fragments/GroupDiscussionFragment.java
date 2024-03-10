//package com.theodoilamviec.Account.fragments;
//
//import android.app.Dialog;
//import android.os.Bundle;
//
//import androidx.annotation.NonNull;
//import androidx.annotation.Nullable;
//import androidx.fragment.app.Fragment;
//import androidx.recyclerview.widget.LinearLayoutManager;
//import androidx.recyclerview.widget.RecyclerView;
//
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.view.Window;
//import android.widget.Toast;
//
//import com.google.firebase.database.DataSnapshot;
//import com.google.firebase.database.DatabaseError;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.firebase.database.ValueEventListener;
//import com.theodoilamviec.Account.Group;
//import com.theodoilamviec.Account.adapters.GroupListAdapter;
//import com.theodoilamviec.theodoilamviec.R;
//import com.theodoilamviec.theodoilamviec.databinding.FragmentGroupDiscussionBinding;
//import com.theodoilamviec.theodoilamviec.databinding.ItemDialogCreateGroupBinding;
//
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * A simple {@link Fragment} subclass.
// * Use the {@link GroupDiscussionFragment#newInstance} factory method to
// * create an instance of this fragment.
// */
//public class GroupDiscussionFragment extends Fragment {
//
//    // TODO: Rename parameter arguments, choose names that match
//    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
//    private static final String ARG_PARAM1 = "param1";
//    private static final String ARG_PARAM2 = "param2";
//
//    // TODO: Rename and change types of parameters
//    private String mParam1;
//    private String mParam2;
//
//    public GroupDiscussionFragment() {
//        // Required empty public constructor
//    }
//
//    /**
//     * Use this factory method to create a new instance of
//     * this fragment using the provided parameters.
//     *
//     * @param param1 Parameter 1.
//     * @param param2 Parameter 2.
//     * @return A new instance of fragment GroupDiscussionFragment.
//     */
//    // TODO: Rename and change types and number of parameters
//    public static GroupDiscussionFragment newInstance(String param1, String param2) {
//        GroupDiscussionFragment fragment = new GroupDiscussionFragment();
//        Bundle args = new Bundle();
//        args.putString(ARG_PARAM1, param1);
//        args.putString(ARG_PARAM2, param2);
//        fragment.setArguments(args);
//        return fragment;
//    }
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        if (getArguments() != null) {
//            mParam1 = getArguments().getString(ARG_PARAM1);
//            mParam2 = getArguments().getString(ARG_PARAM2);
//        }
//    }
//
//    FragmentGroupDiscussionBinding binding ;
//    GroupListAdapter groupListAdapter ;
//
//    List<Group> groupsList = new ArrayList<>();
//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                             Bundle savedInstanceState) {
//        // Inflate the layout for this fragment
//        binding = FragmentGroupDiscussionBinding.inflate(getLayoutInflater());
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
////        groupListAdapter = new GroupListAdapter(this);
//        groupListAdapter.submitList(groupsList);
//        binding.rcvGroup.setLayoutManager(new LinearLayoutManager(requireActivity(), RecyclerView.VERTICAL,false));
//        FirebaseDatabase.getInstance().getReference("Groups").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists() && snapshot.hasChildren()){
//                    groupsList.clear();
//                    for (DataSnapshot dataSnapshot: snapshot.getChildren()) {
//
//                        Group group = dataSnapshot.getValue(Group.class);
//                        if (group != null) {
//                            groupsList.add(group);
//                        }
//                    }
//                    groupListAdapter.submitList(groupsList);
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                Toast.makeText(requireActivity(), "Error = "+ error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        binding.ftbAdd.setOnClickListener(e ->{
//            showDialog();
//        });
//    }
//
//    public void showDialog() {
//        Dialog dialog = new Dialog(requireActivity());
//        ItemDialogCreateGroupBinding binding1 = ItemDialogCreateGroupBinding.inflate(getLayoutInflater());
//        dialog.setContentView(binding1.getRoot());
//        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//
//        binding1.btnCreate.setOnClickListener(e ->{
//            String groupName = binding1.edtGroup.getText().toString().trim();
//
//            Group group = new Group(String.valueOf(System.currentTimeMillis()),groupName);
//            FirebaseDatabase.getInstance().getReference("Groups").child(group.getIdGroup())
//                            .setValue(group);
//            dialog.dismiss();
//        });
//        dialog.show();
//    }
//}