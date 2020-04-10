package com.example.virtualstudyroom.model;

import android.net.Uri;

import com.google.firebase.Timestamp;

public class Yell {
    private String userName;
    private String toUserId;
    private Uri iconUrl;
    private Timestamp sentAt;

    public Yell(String userName, String toUserId, Uri iconUrl, Timestamp sentAt){
        this.userName = userName;
        this.toUserId = toUserId;
        this.iconUrl = iconUrl;
        this.sentAt = sentAt;
    }

    public String getUserName(){ return userName; }
    public String getToUserId(){ return toUserId;}
    public Uri getIconUrl(){ return iconUrl; }
    public Timestamp getSentAt(){ return sentAt; }
}
