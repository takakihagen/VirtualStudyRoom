package com.example.virtualstudyroom.ui.utils;

import android.content.Intent;
import android.provider.ContactsContract;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class YellService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.d("YellService++++", token);

        //sendRegistrationToServer("1", token);
    }
}

