package com.example.virtualstudyroom.model;

import android.net.Uri;

import com.google.firebase.Timestamp;

public class CurrentUser {
    private String docId;
    private String userName;
    private String userId;
    private Uri iconURI;
    private Timestamp startAt;
    private String status;
    private long pauseTieme;
    private Timestamp pauseStartTime;

    private boolean sendabale;
    private String regToken;

    private long TotalPauseTimeInMin;
    private long TotalStudyTimeInMin;

    public CurrentUser(String dockId, String userName, String userId, Uri iconURI, Timestamp startAt,
                       String status, long pauseTieme){
        //if status is "study"
        this.docId = dockId;
        this.userName = userName;
        this.userId = userId;
        this.iconURI = iconURI;
        this.startAt = startAt;
        this.status = status;
        this.pauseTieme = pauseTieme;

        TotalStudyTimeInMin = (Math.abs(Timestamp.now().getSeconds()-startAt.getSeconds())-pauseTieme)/60;
        TotalPauseTimeInMin = 0;
    }

    public CurrentUser(String docId, String userName, String userId, Uri iconURI, Timestamp startAt,
                       String status, long pauseTieme, Timestamp pauseStartTime,
                       String regToken, boolean sendable){
        //if status is "pause"
        this.docId = docId;
        this.userName = userName;
        this.userId = userId;
        this.iconURI = iconURI;
        this.startAt = startAt;
        this.status = status;
        this.pauseTieme = pauseTieme;
        this.pauseStartTime = pauseStartTime;

        this.sendabale = sendable;
        this.regToken = regToken;

        TotalStudyTimeInMin = 0;
        TotalPauseTimeInMin = Math.abs(Timestamp.now().getSeconds()-pauseStartTime.getSeconds())/60;
    }

    public String getUserName(){return userName;}
    public Uri getIconURI(){return iconURI;}
    public Timestamp getStartAt(){return startAt;}
    public String getStatus(){return status;}
    public long getTotalStudyTime(){return TotalStudyTimeInMin;}
    public long getTotalPauseTime(){return TotalPauseTimeInMin;}

    public String getDocId(){return docId;}
    public boolean getSendable(){return sendabale;}
    public String getRegToken(){return regToken;}
    public String getUserId(){return userId;}
}
