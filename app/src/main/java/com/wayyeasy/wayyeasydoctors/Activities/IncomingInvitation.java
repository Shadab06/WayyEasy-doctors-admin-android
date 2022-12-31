package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wayyeasy.wayyeasydoctors.databinding.ActivityDashboardBinding;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityIncomingInvitationBinding;

public class IncomingInvitation extends AppCompatActivity {

    ActivityIncomingInvitationBinding incomingInvitationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomingInvitationBinding = ActivityIncomingInvitationBinding.inflate(getLayoutInflater());
        setContentView(incomingInvitationBinding.getRoot());
    }
}