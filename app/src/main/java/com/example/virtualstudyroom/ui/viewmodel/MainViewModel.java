package com.example.virtualstudyroom.ui.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.virtualstudyroom.database.StudyHistory;
import com.example.virtualstudyroom.database.StudyHistoryDatabase;

import java.util.List;

public class MainViewModel extends AndroidViewModel {

    private LiveData<List<StudyHistory>> studyHistories;

    public MainViewModel(@NonNull Application application) {
        super(application);
        StudyHistoryDatabase database = StudyHistoryDatabase.getInstance(this.getApplication());
        studyHistories = database.studyHistoryDao().fetchAllHistory();
    }

    public LiveData<List<StudyHistory>> getHistories(){
        return studyHistories;
    }
}
