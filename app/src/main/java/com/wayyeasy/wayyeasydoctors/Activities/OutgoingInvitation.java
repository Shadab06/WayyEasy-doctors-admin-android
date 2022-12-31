package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wayyeasy.wayyeasydoctors.databinding.ActivityIncomingInvitationBinding;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityOutgoingInvitationBinding;

public class OutgoingInvitation extends AppCompatActivity {

    ActivityOutgoingInvitationBinding outgoingInvitationBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outgoingInvitationBinding = ActivityOutgoingInvitationBinding.inflate(getLayoutInflater());
        setContentView(outgoingInvitationBinding.getRoot());


    }
}