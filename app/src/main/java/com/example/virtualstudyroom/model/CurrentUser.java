package com.example.virtualstudyroom.model;

import android.net.Uri;

import com.google.firebase.Timestamp;

public class CurrentUser {
    private String userName;
    private Uri iconURI;
    private Timestamp startAt;

    public CurrentUser(String userName, Uri iconURI, Timestamp startAt){
        this.userName = userName;
        this.iconURI = iconURI;
        this.startAt = startAt;
    }

    public String getUserName(){return userName;}
    public Uri getIconURI(){return iconURI;}
    public Timestamp getStartAt(){return startAt;}
}
