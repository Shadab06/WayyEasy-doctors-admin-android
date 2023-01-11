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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.CallingApiHandlers.CallingApiControllers;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.CallingApiHandlers.CallingApiSet;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.SharedPreferenceManager;
import com.wayyeasy.wayyeasydoctors.Models.RealtimeCalling.user_booked_response_model;
import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityOutgoingInvitationBinding;

import org.checkerframework.checker.nullness.qual.NonNull;
import org.json.JSONArray;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OutgoingInvitation extends AppCompatActivity {

    ActivityOutgoingInvitationBinding outgoingInvitationBinding;
    user_booked_response_model bookedUser;
    SharedPreferenceManager preferenceManager;
    private String inviterToken = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        outgoingInvitationBinding = ActivityOutgoingInvitationBinding.inflate(getLayoutInflater());
        setContentView(outgoingInvitationBinding.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());

        /*
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isComplete()) {
                inviterToken = task.getResult();
            }
        });
         */

        Intent intent = getIntent();
        String meetingType = intent.getStringExtra("type");
        bookedUser = intent.getParcelableExtra("user");
        if (meetingType != null) {
            if (meetingType.equals("video")) {
                outgoingInvitationBinding.callType.setImageResource(R.drawable.ic_video);
            }
        }
        Log.d(TAG, "onCreate: 63 "+preferenceManager.getString(Constants.KEY_FCM_TOKEN));
        Log.d(TAG, "onCreate: 63 "+ bookedUser.getFcmToken());

        if (bookedUser != null) {
            outgoingInvitationBinding.userIcon.setText(bookedUser.getName().substring(0, 2));
            outgoingInvitationBinding.userName.setText(bookedUser.getName());
//            byte[] hospitalImageByte;
//            Bitmap hospitalImageBitmap;
//
//            hospitalImageByte = Base64.decode(bookedUser.getProfileImage(), Base64.DEFAULT);
//            hospitalImageBitmap = BitmapFactory.decodeByteArray(hospitalImageByte, 0, hospitalImageByte.length);
//            outgoingInvitationBinding..setImageBitmap(hospitalImageBitmap);
        }

        outgoingInvitationBinding.rejectCall.setOnClickListener(view -> {
            if (bookedUser != null) {
                cancelInvitation(bookedUser.getFcmToken());
            }
        });

        if (meetingType != null && bookedUser != null) {
            initiateMeeting(meetingType, bookedUser.getFcmToken());
        }
    }

    private void initiateMeeting(String meetingType, String receiverToken) {
        try {

            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();


            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION);
            data.put(Constants.REMOTE_MESSAGE_MEETING_TYPE, meetingType);
            data.put(Constants.name, preferenceManager.getString(Constants.name));
            data.put(Constants.REMOTE_MESSAGE_INVITER_TOKEN, preferenceManager.getString(Constants.KEY_FCM_TOKEN));

            body.put(Constants.REMOTE_MESSAGE_DATA, data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION);
        } catch (Exception e) {
            Log.d(TAG, "Error: 105 " + e.getMessage());
            Toast.makeText(this, "Error: 105 " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRemoteMessage(String remoteBodyMessage, String type) {

        CallingApiControllers.getClient().create(CallingApiSet.class).sendRemoteMessage(
                        Constants.getRemoteMessageHeaders(), remoteBodyMessage)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response != null && response.isSuccessful()) {
                            if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                                Toast.makeText(getApplicationContext(), bookedUser.getFcmToken(), Toast.LENGTH_SHORT).show();
//                                      Toast.makeText(OutgoingInvitation.this, "Invitation send successfully", Toast.LENGTH_SHORT).show();
                            } else if (type.equals(Constants.REMOTE_MSG_INVITATION_RESPONSE)){
                                Toast.makeText(OutgoingInvitation.this, "Invitation Cancelled", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        } else {
                            Log.d(TAG, "onResponse: " + response);
                            Toast.makeText(OutgoingInvitation.this, "Response: " + response.message(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Log.d(TAG, "Error: 105 " + t.getMessage());
                        Toast.makeText(OutgoingInvitation.this, "Error: 127 " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }

    public void cancelInvitation(String receiverToken) {
        try {
            JSONArray tokens = new JSONArray();
            tokens.put(receiverToken);

            JSONObject body = new JSONObject();
            JSONObject data = new JSONObject();

            data.put(Constants.REMOTE_MSG_TYPE, Constants.REMOTE_MSG_INVITATION_RESPONSE);
            data.put(Constants.REMOTE_MSG_INVITATION_RESPONSE, Constants.REMOTE_MSG_INVITATION_CANCELLED);

            body.put(Constants.REMOTE_MESSAGE_DATA, data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION_RESPONSE);
        } catch (Exception e) {
            Log.d(TAG, "sendInvitationResponse: 65 " + e.getMessage());
            Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private BroadcastReceiver invitationResponseReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String type = intent.getStringExtra(Constants.REMOTE_MSG_INVITATION_RESPONSE);
            if (type!=null) {
                if (type.equals(Constants.REMOTE_MSG_INVITATION_ACCEPTED)) {
                    Toast.makeText(context, "Invitation Accepted", Toast.LENGTH_SHORT).show();
                } else if (type.equals(Constants.REMOTE_MSG_INVITATION_REJECTED)) {
                    Toast.makeText(context, "Invitation Rejected", Toast.LENGTH_SHORT).show();
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