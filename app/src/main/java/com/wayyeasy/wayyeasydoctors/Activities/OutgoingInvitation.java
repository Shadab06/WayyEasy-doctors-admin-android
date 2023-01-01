package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;
import com.google.firebase.messaging.FirebaseMessaging;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.ApiHandlers.ApiControllers;
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
        FirebaseMessaging.getInstance().getToken().addOnCompleteListener(task -> {
            if (task.isSuccessful() && task.getResult() != null) {
                inviterToken = task.getResult();
            }
        });

        Intent intent = getIntent();
        String meetingType = intent.getStringExtra("type");
        bookedUser = intent.getParcelableExtra("user");
        if (meetingType != null) {
            if (meetingType.equals("video")) {
                outgoingInvitationBinding.callType.setImageResource(R.drawable.ic_video);
            }
        }

        if (bookedUser != null) {
            outgoingInvitationBinding.userIcon.setText(bookedUser.getName().substring(0, 2));
            outgoingInvitationBinding.userName.setText(bookedUser.getName());
            byte[] hospitalImageByte;
            Bitmap hospitalImageBitmap;

            hospitalImageByte = Base64.decode(bookedUser.getProfileImage(), Base64.DEFAULT);
            hospitalImageBitmap = BitmapFactory.decodeByteArray(hospitalImageByte, 0, hospitalImageByte.length);
//            outgoingInvitationBinding..setImageBitmap(hospitalImageBitmap);
        }

        outgoingInvitationBinding.rejectCall.setOnClickListener(view -> onBackPressed());

        if (meetingType != null && bookedUser != null) {
            initiateMeeting(meetingType, bookedUser.getToken());
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
            data.put(Constants.REMOTE_MESSAGE_INVITER_TOKEN, inviterToken);

            body.put(Constants.REMOTE_MESSAGE_DATA, data);
            body.put(Constants.REMOTE_MESSAGE_REGISTRATION_IDS, tokens);

            sendRemoteMessage(body.toString(), Constants.REMOTE_MSG_INVITATION);
        } catch (Exception e) {
            Toast.makeText(this, "Error: "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void sendRemoteMessage(String remoteBodyMessage, String type) {
        ApiControllers.getInstance().getApi().sendRemoteMessage(
                        Constants.getRemoteMessageHeaders(), remoteBodyMessage)
                .enqueue(new Callback<String>() {
                    @Override
                    public void onResponse(@NonNull Call<String> call, @NonNull Response<String> response) {
                        if (response != null && response.isSuccessful()) {
                            if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                                Toast.makeText(OutgoingInvitation.this, "Invitation send successfully", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(OutgoingInvitation.this, response.message(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<String> call, @NonNull Throwable t) {
                        Toast.makeText(OutgoingInvitation.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}