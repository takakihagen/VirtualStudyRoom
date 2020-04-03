package com.example.virtualstudyroom.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {StudyHistory.class}, version = 1, exportSchema = false)
@TypeConverters({Converters.class})
public abstract class StudyHistoryDatabase extends RoomDatabase {
    private static final Object LOCK = new Object();
    private static final String DATABASE_NAME = "studyhistorylist";
    private static StudyHistoryDatabase sInstance;

    public static StudyHistoryDatabase getInstance(Context context){
        if(sInstance==null){
            synchronized (LOCK) {
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        StudyHistoryDatabase.class, StudyHistoryDatabase.DATABASE_NAME)
                        .build();
            }
        }
        return sInstance;
    }

    public abstract StudyHistoryDao studyHistoryDao();
}
