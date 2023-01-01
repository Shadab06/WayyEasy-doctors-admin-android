package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;

import com.wayyeasy.wayyeasydoctors.Models.RealtimeCalling.user_booked_response_model;
import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityDashboardBinding;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityIncomingInvitationBinding;

public class IncomingInvitation extends AppCompatActivity {

    ActivityIncomingInvitationBinding incomingInvitationBinding;
    user_booked_response_model bookedUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        incomingInvitationBinding = ActivityIncomingInvitationBinding.inflate(getLayoutInflater());
        setContentView(incomingInvitationBinding.getRoot());

        Intent intent = getIntent();
        String meetingType = intent.getStringExtra("type");
        bookedUser = intent.getParcelableExtra("user");
        if (meetingType != null) {
            if (meetingType.equals("video")) {
                incomingInvitationBinding.callType.setImageResource(R.drawable.ic_video);
            }
        }

        if (bookedUser != null) {
            incomingInvitationBinding.userIcon.setText(bookedUser.getName().substring(0, 2));
            incomingInvitationBinding.userName.setText(bookedUser.getName());
            byte[] hospitalImageByte;
            Bitmap hospitalImageBitmap;

            hospitalImageByte = Base64.decode(bookedUser.getProfileImage(), Base64.DEFAULT);
            hospitalImageBitmap = BitmapFactory.decodeByteArray(hospitalImageByte, 0, hospitalImageByte.length);
//            outgoingInvitationBinding..setImageBitmap(hospitalImageBitmap);
        }
    }
}