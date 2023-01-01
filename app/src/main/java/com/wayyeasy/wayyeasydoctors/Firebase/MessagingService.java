package com.wayyeasy.wayyeasydoctors.Firebase;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.wayyeasy.wayyeasydoctors.Activities.IncomingInvitation;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class MessagingService extends FirebaseMessagingService {
    public static final String TAG = "Firebase CloudMessage";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        super.onMessageReceived(message);

        String type = message.getData().get(Constants.REMOTE_MSG_TYPE);
        if (type != null) {
            if (type.equals(Constants.REMOTE_MSG_INVITATION)) {
                Intent intent = new Intent(getApplicationContext(), IncomingInvitation.class);
                intent.putExtra(Constants.REMOTE_MESSAGE_MEETING_TYPE,
                        message.getData().get(Constants.REMOTE_MESSAGE_MEETING_TYPE));
                intent.putExtra(Constants.name, message.getData().get(Constants.name));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        }
    }
}
