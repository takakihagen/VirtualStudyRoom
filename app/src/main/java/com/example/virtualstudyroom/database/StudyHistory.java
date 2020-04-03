package com.example.virtualstudyroom.database;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import java.util.Date;

@Entity
public class StudyHistory {
    @Ignore
    public StudyHistory(Long totalTime, Date startTime){
        this.totalTime = totalTime;
        this.startTime = startTime;
    }

    public StudyHistory(int hid, Long totalTime, Date startTime){
        this.hid = hid;
        this.totalTime = totalTime;
        this.startTime = startTime;
    }

    @PrimaryKey(autoGenerate = true)
    public int hid;

    @ColumnInfo(name="total_time")
    public Long totalTime;

    @ColumnInfo(name="start_time")
    public Date startTime;

    public int getHid(){return hid;}
    public void setHid(int history_id){hid = history_id;}

    public Long getTotalTime(){return totalTime;}
    public void setTotalTime(Long time){totalTime = time;}

    public Date getStartTime(){return startTime;}
    public void setStartTime(Date date){startTime = date;}
}
