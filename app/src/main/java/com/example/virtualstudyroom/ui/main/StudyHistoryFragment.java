package com.example.virtualstudyroom.ui.main;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.TokenWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.virtualstudyroom.R;
import com.example.virtualstudyroom.database.StudyHistory;
import com.example.virtualstudyroom.ui.viewmodel.MainViewModel;
import com.google.android.gms.common.internal.Objects;

import java.util.List;

public class StudyHistoryFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_study_history, container, false);
        retrieveTask();
        return view;
    }

    private void retrieveTask(){
        MainViewModel vModel = new ViewModelProvider(getActivity()).get(MainViewModel.class);
        vModel.getHistories().observe(getActivity(), new Observer<List<StudyHistory>>() {
            @Override
            public void onChanged(List<StudyHistory> studyHistories) {
                for(StudyHistory s: studyHistories){
                    Log.d("Lpppp", s.getTotalTime().toString());
                    Log.d("Lpppp", s.getStartTime().toString());
                }
            }
        });
    }
}
