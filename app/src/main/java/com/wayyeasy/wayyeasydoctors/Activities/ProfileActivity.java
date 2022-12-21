package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.SharedPreferenceManager;
import com.wayyeasy.wayyeasydoctors.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasydoctors.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityProfileBinding;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.HashMap;

public class ProfileActivity extends AppCompatActivity {

    ActivityProfileBinding profileBinding;
    private final int UPLOAD_VERIFICATION_DOCS = 1001;
    private final int UPLOAD_PROFILE_IMAGE = 1002;
    private String docImage, profileImage;
    SharedPreferenceManager preferenceManager;
    ResponseDialog dialog;
    ProgressDialog progressDialog;
    private String status = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        dialog = new ResponseDialog();
        progressDialog = new ProgressDialog(ProfileActivity.this);

        if (preferenceManager.getBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN)) {
            status = preferenceManager.getString(Constants.status);
        }

        String[] specialitySelection = getResources().getStringArray(R.array.speciality);

        ArrayAdapter<String> specialityAdapter = new ArrayAdapter<>(getApplicationContext(), android.R.layout.simple_dropdown_item_1line, specialitySelection);
        profileBinding.specialityInput.setAdapter(specialityAdapter);

        profileBinding.editProfile.setOnClickListener(view -> {
            uploadProfileImage();
        });
    }

    private void uploadProfileImage() {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ImagePicker.with(ProfileActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(128)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(UPLOAD_PROFILE_IMAGE);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    public void uploadProofDoc(View view) {
        Dexter.withContext(getApplicationContext())
                .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new PermissionListener() {
                    @Override
                    public void onPermissionGranted(PermissionGrantedResponse permissionGrantedResponse) {
                        ImagePicker.with(ProfileActivity.this)
                                .crop()                    //Crop image(Optional), Check Customization for more option
                                .compress(256)            //Final image size will be less than 1 MB(Optional)
                                .maxResultSize(1080, 1080)    //Final image resolution will be less than 1080 x 1080(Optional)
                                .start(UPLOAD_VERIFICATION_DOCS);
                    }

                    @Override
                    public void onPermissionDenied(PermissionDeniedResponse permissionDeniedResponse) {

                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(PermissionRequest permissionRequest, PermissionToken permissionToken) {
                        permissionToken.continuePermissionRequest();
                    }
                }).check();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == UPLOAD_VERIFICATION_DOCS) {

                Uri fileUri = data.getData();
                profileBinding.proofImg.setText(data.getData().getLastPathSegment());

                try {
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    encodeBitmapImage(bitmap, UPLOAD_VERIFICATION_DOCS);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
            if (requestCode == UPLOAD_PROFILE_IMAGE) {
                Uri fileUri = data.getData();
                try {
                    InputStream inputStream = getContentResolver().openInputStream(fileUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    profileBinding.userProfile.setImageBitmap(bitmap);
                    encodeBitmapImage(bitmap, UPLOAD_PROFILE_IMAGE);
                } catch (Exception ex) {
                    Toast.makeText(getApplicationContext(), ex.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void encodeBitmapImage(Bitmap bitmap, int resultCode) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);

        byte[] bytesOfImage = byteArrayOutputStream.toByteArray();
        if (resultCode == UPLOAD_VERIFICATION_DOCS) {
            docImage = android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
        }
        if (resultCode == UPLOAD_PROFILE_IMAGE) {
            profileImage = android.util.Base64.encodeToString(bytesOfImage, Base64.DEFAULT);
        }
    }

    public void sendDataForVerification(View view) {
        profileBinding.errorMsg.setVisibility(View.VISIBLE);
        if (profileImage == null) {
            profileBinding.errorMsg.setText("Please upload profile image.");
            return;
        }

        if (docImage == null) {
            profileBinding.errorMsg.setText("Please upload license or certificate in order to verify your account.");
            return;
        }

        uploadData(profileImage, docImage, profileBinding.specialityInput.getText().toString(), profileBinding.qualificationInput.getText().toString(), profileBinding.priceInput.getText().toString(), profileBinding.addressInput.getText().toString(), profileBinding.descriptionInput.getText().toString());
    }

    private void uploadData(String profileImage, String docImage, String speciality, String qualification, String price, String address, String desc) {
        if (speciality != null && qualification != null && price != null && address != null && desc != null) {
            progressDialog.showDialog();
            HashMap<String, Object> userMap = new HashMap<>();
            userMap.put(Constants.image, profileImage);
            userMap.put(Constants.proofDocs, docImage);
            userMap.put(Constants.specialityType, speciality);
            userMap.put(Constants.qualifiation, qualification);
            userMap.put(Constants.price, price);
            userMap.put(Constants.address, address);
            userMap.put(Constants.description, desc);
            userMap.put(Constants.status, "pending");

            FirebaseFirestore database = FirebaseFirestore.getInstance();
            database.collection(Constants.FIREBASE_DOCTORS_DB)
                    .document(preferenceManager.getString(Constants.firebaseId))
                    .update(userMap)
                    .addOnCompleteListener(task -> {
                        preferenceManager.putBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN, true);
                        preferenceManager.putString(Constants.status, "pending");
                        profileBinding.errorMsg.setVisibility(View.GONE);
                        progressDialog.dismissDialog();
                        startActivity(new Intent(getApplicationContext(), DashboardActivity.class));
                    })
                    .addOnFailureListener(e -> {

                    });
        } else {
            profileBinding.errorMsg.setText("All fields are mandatory.");
        }
    }
}