package com.wayyeasy.wayyeasydoctors.Activities;

import static com.wayyeasy.wayyeasydoctors.Activities.DashboardActivity.TAG;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.wayyeasy.wayyeasydoctors.ComponentFiles.CallingApiHandlers.CallingApiControllers;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.CallingApiHandlers.CallingApiSet;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasydoctors.Models.RealtimeCalling.user_booked_response_model;
import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityIncomingInvitationBinding;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
//            byte[] hospitalImageByte;
//            Bitmap hospitalImageBitmap;
//
//            hospitalImageByte = Base64.decode(bookedUser.getProfileImage(), Base64.DEFAULT);
//            hospitalImageBitmap = BitmapFactory.decodeByteArray(hospitalImageByte, 0, hospitalImageByte.length);
//            outgoingInvitationBinding..setImageBitmap(hospitalImageBitmap);
        }

        incomingInvitationBinding.receiveCall.setOnClickListener(view -> sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_ACCEPTED,
                getIntent().getStringExtra(Constants.REMOTE_MESSAGE_INVITER_TOKEN)));

        incomingInvitationBinding.rejectCall.setOnClickListener(view -> sendInvitationResponse(Constants.REMOTE_MSG_INVITATION_REJECTED,
                getIntent().getStringExtra(Constants.REMOTE_MESSAGE_INVITER_TOKEN)));
    }

    public void sendInvitationResponse(String type, String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, type);

            body.put(Constants.REMOTE_MESSAGE_DATA, data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), type);
        } catch (Exception e) {
            Log.d(TAG, "sendInvitationResponse: 65 "+e.getMessage());
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRemoteMessage(String remoteBodyMessage, String type) {

        CallingApiControllers.getClient().create(CallingApiSet.class).sendRemoteMessage(
                        Constants.getRemoteMessageHeaders(), remoteBodyMessage)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response != null && response.isSuccessful()) {
                            if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                                Toast.makeText(IncomingInvitation.this, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(IncomingInvitation.this, "Invitation Rejected", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Log.d(TAG, "onResponse: " + response);
                            Toast.makeText(IncomingInvitation.this, "Response: " + response.message(), Toast.LENGTH_SHORT).show();
                        }
                        finish();
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d(TAG, "Error: 105 " + t.getMessage());
                        Toast.makeText(IncomingInvitation.this, "Error: 127 " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if (type!=null) {
                if (type.equals(Constants.REMOTE_MSG_INVITATION_CANCELLED)) {
                    Toast.makeText(context, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
                    finish();
                }
            }
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(invitationResponseReceiver, new IntentFilter(Constants.REMOTE_MSG_INVITATION_RESPONSE));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(getApplicationContext()).unregisterReceiver(invitationResponseReceiver);
    }
}