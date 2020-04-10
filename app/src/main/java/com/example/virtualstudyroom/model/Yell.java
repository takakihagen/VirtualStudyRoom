package com.example.virtualstudyroom.model;

import android.net.Uri;

import com.google.firebase.Timestamp;

public class Yell {
    private String userName;
    private Uri iconUrl;
    private Timestamp sentAt;

    public Yell(String userName, Uri iconUrl, Timestamp sentAt){
        this.userName = userName;
        this.iconUrl = iconUrl;
        this.sentAt = sentAt;
    }

    public String getUserName(){ return userName; }
    public Uri getIconUrl(){ return iconUrl; }
    public Timestamp getSentAt(){ return sentAt; }
}
