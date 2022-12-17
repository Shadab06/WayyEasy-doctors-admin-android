package com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants;

public class Constants {
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    public static final String name = "name";
    public static final String email = "email";
    public static final String mobile = "mobile";
    public static final String password = "password";
    public static final String mongoId = "mongoId";


    //firebase
    public static final String FIREBASE_DOCTORS_DB = "doctors";
    public static final String KEY_FCM_TOKEN = "fcm_token";
    public static final String KEY_FIREBASE_USER_ID = "firebase_id";

    //mongodb
    public static final String KEY_AUTHENTICATED_NUMBER = "mobile";
    public static final String KEY_MONGO_TOKEN = "registered_token";
    public static final String KEY_MONGO_ID = "registered_id";

    //shared preferences
    public static final String KEY_PREFERENCE_NAME_DOCTOR = "login_credentials";
    public static final String KEY_IS_DOCTOR_SIGNED_IN = "is_signed_in";
}
