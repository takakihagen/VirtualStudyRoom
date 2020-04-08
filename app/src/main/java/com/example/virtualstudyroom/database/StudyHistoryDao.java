package com.example.virtualstudyroom.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface StudyHistoryDao {
    @Insert
    void insertHistory(StudyHistory history);

    @Query("SELECT * FROM StudyHistory ORDER BY start_time")
    LiveData<List<StudyHistory>> fetchAllHistory();

    @Update
    void updateHistory(StudyHistory note);

    @Delete
    void deleteHistory(StudyHistory history);
}
