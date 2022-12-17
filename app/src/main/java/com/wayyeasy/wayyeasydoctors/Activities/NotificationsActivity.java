package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wayyeasy.wayyeasydoctors.databinding.ActivityNotificationsBinding;

public class NotificationsActivity extends AppCompatActivity {

    ActivityNotificationsBinding notificationsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        notificationsBinding = ActivityNotificationsBinding.inflate(getLayoutInflater());
        setContentView(notificationsBinding.getRoot());
    }
}