package com.wayyeasy.wayyeasydoctors.Activities;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.github.dhaval2404.imagepicker.ImagePicker;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        profileBinding = ActivityProfileBinding.inflate(getLayoutInflater());
        setContentView(profileBinding.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        dialog = new ResponseDialog();
        progressDialog = new ProgressDialog(ProfileActivity.this);

        updateProfileStatus();

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
                                .compress(256)            //Final image size will be less than 1 MB(Optional)
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
                        profileBinding.errorMsg.setVisibility(View.GONE);
                        progressDialog.dismissDialog();
                        preferenceManager.putBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN, true);
                        preferenceManager.putString(Constants.status, "pending");
                        preferenceManager.putString(Constants.image, profileImage);
                        preferenceManager.putString(Constants.proofDocs, docImage);
                        preferenceManager.putString(Constants.specialityType, speciality);
                        preferenceManager.putString(Constants.qualifiation, qualification);
                        preferenceManager.putString(Constants.price, price);
                        preferenceManager.putString(Constants.address, address);
                        preferenceManager.putString(Constants.description, desc);

                        Intent intent = new Intent(getApplicationContext(), DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    })
                    .addOnFailureListener(e -> {
                        dialog.showDialog(ProfileActivity.this, "Error", e.getMessage().toString());
                    });
        } else {
            profileBinding.errorMsg.setText("All fields are mandatory.");
        }
    }

    private void updateProfileStatus() {
        if (preferenceManager.getBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN)) {
            profileBinding.name.setText(preferenceManager.getString(Constants.name));
            profileBinding.email.setText(preferenceManager.getString(Constants.email));
            profileBinding.mobile.setText(preferenceManager.getString(Constants.mobile));

            if (preferenceManager.getString(Constants.status).equals("pending")) {
                profileBinding.status.setText("Pending");
                profileBinding.status.setTextColor(getResources().getColor(R.color.light_orange));
                profileBinding.specialityInput.setText(preferenceManager.getString(Constants.specialityType));
                profileBinding.qualificationInput.setText(preferenceManager.getString(Constants.qualifiation));
                profileBinding.priceInput.setText(preferenceManager.getString(Constants.price));
                profileBinding.addressInput.setText(preferenceManager.getString(Constants.address));
                profileBinding.descriptionInput.setText(preferenceManager.getString(Constants.description));
                profileBinding.updateProfileBtn.setVisibility(View.GONE);
                profileBinding.pendingMsg.setVisibility(View.VISIBLE);
                profileBinding.pendingMsg.setText("Profile activation request has been sent.\nWe will update you within 1 day.");
            }

            if (preferenceManager.getString(Constants.status).equals("inActive")) {
                profileBinding.status.setText("In Active");
                profileBinding.status.setTextColor(getResources().getColor(R.color.red));
            }

            if (preferenceManager.getString(Constants.status).equals("active")) {
                profileBinding.updateProfileBtn.setVisibility(View.GONE);
                profileBinding.pendingMsg.setVisibility(View.GONE);
                profileBinding.pendingMsg.setText("Profile has been successfully updated");
                profileBinding.pendingMsg.setTextColor(getResources().getColor(R.color.theme_color));
                profileBinding.status.setText("Active");
                profileBinding.status.setTextColor(getResources().getColor(R.color.theme_color));
                profileBinding.name.setText(preferenceManager.getString(Constants.name));
                profileBinding.email.setText(preferenceManager.getString(Constants.email));
                profileBinding.mobile.setText(preferenceManager.getString(Constants.mobile));
                profileBinding.specialityInput.setText(preferenceManager.getString(Constants.specialityType));
                profileBinding.qualificationInput.setText(preferenceManager.getString(Constants.qualifiation));
                profileBinding.priceInput.setText(preferenceManager.getString(Constants.price));
                profileBinding.addressInput.setText(preferenceManager.getString(Constants.address));
                profileBinding.descriptionInput.setText(preferenceManager.getString(Constants.description));
                profileBinding.proofImg.setText("Document approved");
                profileBinding.proofImg.setTextColor(getResources().getColor(R.color.theme_color));

                byte[] profileImageByte;
                Bitmap profileImageBitmap;

                profileImageByte = Base64.decode(preferenceManager.getString(Constants.image), Base64.DEFAULT);
                profileImageBitmap = BitmapFactory.decodeByteArray(profileImageByte, 0, profileImageByte.length);
                profileBinding.userProfile.setImageBitmap(profileImageBitmap);
            }
        }
    }
}