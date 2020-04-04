package com.example.virtualstudyroom.model;

import android.net.Uri;

import com.google.firebase.Timestamp;

public class CurrentUser {
    private String userName;
    private Uri iconURI;
    private Timestamp startAt;
    private String status;
    private long pauseTieme;
    private Timestamp pauseStartTime;

    private long TotalPauseTimeInMin;
    private long TotalStudyTimeInMin;

    public CurrentUser(String userName, Uri iconURI, Timestamp startAt,
                       String status, long pauseTieme){
        //if status is "study"
        this.userName = userName;
        this.iconURI = iconURI;
        this.startAt = startAt;
        this.status = status;
        this.pauseTieme = pauseTieme;

        TotalStudyTimeInMin = (Math.abs(Timestamp.now().getSeconds()-startAt.getSeconds())-pauseTieme)/60;
        TotalPauseTimeInMin = 0;
    }

    public CurrentUser(String userName, Uri iconURI, Timestamp startAt,
                       String status, long pauseTieme, Timestamp pauseStartTime){
        //if status is "pause"
        this.userName = userName;
        this.iconURI = iconURI;
        this.startAt = startAt;
        this.status = status;
        this.pauseTieme = pauseTieme;
        this.pauseStartTime = pauseStartTime;

        TotalStudyTimeInMin = 0;
        TotalPauseTimeInMin = Math.abs(Timestamp.now().getSeconds()-pauseStartTime.getSeconds())/60;
    }

    public String getUserName(){return userName;}
    public Uri getIconURI(){return iconURI;}
    public Timestamp getStartAt(){return startAt;}
    public String getStatus(){return status;}
    public long getTotalStudyTime(){return TotalStudyTimeInMin;}
    public long getTotalPauseTime(){return TotalPauseTimeInMin;}
}
