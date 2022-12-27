package com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants;

public class Constants {
    public static final String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    //doctor details MongoDB
    public static final String name = "name";
    public static final String email = "email";
    public static final String mobile = "mobile";
    public static final String password = "password";
    public static final String mongoId = "mongoId";
    public static final String firebaseId = "firebaseId";
    public static final String status = "status";
    public static final String description = "description";
    public static final String qualifiation = "qualifiation";
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
