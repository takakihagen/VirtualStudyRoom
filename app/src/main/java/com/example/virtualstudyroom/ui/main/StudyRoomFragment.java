package com.example.virtualstudyroom.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.virtualstudyroom.R;

public class StudyRoomFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_room, container, false);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        ((MainActivity) getActivity()).setButtons();
    }
}
