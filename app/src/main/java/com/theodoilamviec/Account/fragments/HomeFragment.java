package com.theodoilamviec.Account.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.theodoilamviec.theodoilamviec.R;

public class HomeFragment extends Fragment {

    // View view
    View view;

    // fragments
    final Fragment notes_fragment = new NotesFragment();

    // active fragment
    Fragment active = notes_fragment;

    // bottom navigation


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // bottom navigation

        FragmentManager fragment_manager = getChildFragmentManager();

        // fragment manager

        fragment_manager.beginTransaction().add(R.id.fragment_container, notes_fragment, "1").commit();
        fragment_manager.beginTransaction().hide(active).show(notes_fragment).commit();
        active = notes_fragment;
        // bottom navigation on item selected listener


        // bottom navigation on item reselected

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
    }


}