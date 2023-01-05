package com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants;

import java.util.HashMap;

public class Constants {
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //doctor details MongoDB
    public static final String name = "name";
    public static final String email = "email";
    public static final String mobile = "mobile";
    public static final String password = "password";
    public static final String mongoId = "mongoId";
    public static final String status = "status";
    public static final String description = "description";
    public static final String qualification = "qualification";
    public static final String specialityType = "specialityType";
    public static final String badge = "badge";
    public static final String image = "image";
    public static final String proofDocs = "proofDocs";
    public static final String address = "address";
    public static final String price = "price";
    public static final String isFull = "isFull";
    public static final String ratings = "ratings";
    public static final String token = "token";
    public static final String role = "role";

    //firebase
    public static final String FIREBASE_DOCTORS_DB = "doctors";
    public static final String KEY_FCM_TOKEN = "fcmToken";
    public static final String KEY_FIREBASE_USER_ID = "firebaseId";

    //mongodb
    public static final String KEY_AUTHENTICATED_NUMBER = "mobile";
    public static final String KEY_MONGO_TOKEN = "registered_token";
    public static final String KEY_MONGO_ID = "registered_id";

    //shared preferences
    public static final String KEY_PREFERENCE_NAME_DOCTOR = "login_credentials";
    public static final String KEY_IS_DOCTOR_SIGNED_IN = "is_signed_in";

    //remote messaging
    public static final String REMOTE_MESSAGE_AUTHORIZATION = "Authorization";
    public static final String REMOTE_MESSAGE_CONTENT_TYPE = "Content-Type";

    public static final String REMOTE_MSG_TYPE = "type";
    public static final String REMOTE_MSG_INVITATION = "invitation";
    public static final String REMOTE_MESSAGE_MEETING_TYPE = "meetingType";
    public static final String REMOTE_MESSAGE_INVITER_TOKEN = "inviter_token";
    public static final String REMOTE_MESSAGE_DATA = "data";
    public static final String REMOTE_MESSAGE_REGISTRATION_IDS = "registration_ids";

    public static final String REMOTE_MSG_INVITATION_RESPONSE = "invitationResponse";

    public static final String REMOTE_MSG_INVITATION_ACCEPTED = "invitationAccepted";
    public static final String REMOTE_MSG_INVITATION_REJECTED = "invitationRejected";
    public static final String REMOTE_MSG_INVITATION_CANCELLED = "invitationCancelled";

    public static HashMap<String, String> getRemoteMessageHeaders() {
        HashMap<String, String> headers = new HashMap<>();
        headers.put(Constants.REMOTE_MESSAGE_AUTHORIZATION,
                "key=AAAAwZdntJM:APA91bHb7P0Peao9GAB7rT-qxrEgn8dZITtiDiAFO-n8S9pFN1xWd5qWX_wkJpSBUQwxi14PR4UzHBNdqGjkNszy4b4MsvNHrko1b2xmG72oWMur5cw1om7pI4dCT_Hdu1tT059qFKPZ");
        headers.put(Constants.REMOTE_MESSAGE_CONTENT_TYPE, "application/json");
        return headers;
    }
}
