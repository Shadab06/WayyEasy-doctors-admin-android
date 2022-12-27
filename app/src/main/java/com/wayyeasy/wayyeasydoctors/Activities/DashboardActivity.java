package com.wayyeasy.wayyeasydoctors.Activities;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.wayyeasy.wayyeasydoctors.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.SharedPreferenceManager;
import com.wayyeasy.wayyeasydoctors.CustomDialogs.ProgressDialog;
import com.wayyeasy.wayyeasydoctors.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasydoctors.Models.verify_response_model_sub;
import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityDashboardBinding;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DashboardActivity extends AppCompatActivity {

    public static final String TAG = "Dashboard";

    ActivityDashboardBinding dashboard;
    ActionBarDrawerToggle toggle;
    private TextView doctorNameHeader, doctorSpecialityHeader, doctorRatingsHeader;
    private ImageView doctorImageHeader;
    private boolean isOnline = false;
    SharedPreferenceManager preferenceManager;
    ProgressDialog progressDialog;
    ResponseDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboard = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboard.getRoot());

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        progressDialog = new ProgressDialog(DashboardActivity.this);
        dialog = new ResponseDialog();

        if (preferenceManager.getBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN))
            if (preferenceManager.getString(Constants.status).equals("active"))
                fetchProfileIfActive();

        //Menu starts
        setSupportActionBar(dashboard.toolbar);

        toggle = new ActionBarDrawerToggle(this, dashboard.navDrawer, dashboard.toolbar, R.string.opened, R.string.closed);
        dashboard.navDrawer.addDrawerListener(toggle);
        toggle.syncState();
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));

        dashboard.navMenu.setNavigationItemSelectedListener(menuItem -> {
            switch (menuItem.getItemId()) {
                case R.id.notification:
                    dashboard.navDrawer.closeDrawer(androidx.core.view.GravityCompat.START);
                    startActivity(new Intent(getApplicationContext(), NotificationsActivity.class));
                    break;

                case R.id.earnings:
                    dashboard.navDrawer.closeDrawer(androidx.core.view.GravityCompat.START);
                    startActivity(new Intent(getApplicationContext(), EarningsActivity.class));
                    break;

                case R.id.contact:
                    dashboard.navDrawer.closeDrawer(androidx.core.view.GravityCompat.START);
                    startActivity(new Intent(getApplicationContext(), ContactActivity.class));
                    break;

                case R.id.terms_conditions:
                    startActivity(new Intent(getApplicationContext(), TermsConditionsActivity.class));
                    dashboard.navDrawer.closeDrawer(androidx.core.view.GravityCompat.START);
                    break;

                case R.id.privacy:
                    dashboard.navDrawer.closeDrawer(androidx.core.view.GravityCompat.START);
                    startActivity(new Intent(getApplicationContext(), PrivacyActivity.class));
                    break;
            }
            return true;
        });

        View header = dashboard.navMenu.getHeaderView(0);
        doctorNameHeader = header.findViewById(R.id.doctor_name_header);
        doctorSpecialityHeader = header.findViewById(R.id.doctor_speciality_header);
        doctorRatingsHeader = header.findViewById(R.id.user_rating_header);
        doctorImageHeader = header.findViewById(R.id.doctor_profile_header);

        updateDoctorsData();

        header.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ProfileActivity.class)));
        //Menu ends
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.on_toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.availability:
                isOnline = !isOnline;
                updateAvailability(item, isOnline);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void updateAvailability(MenuItem item, boolean isOnline) {
        if (preferenceManager.getBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN)) {
            if (preferenceManager.getString(Constants.status).equals("active")) {
                if (isOnline) {
                    item.setIcon(R.drawable.offline);
                    dashboard.toolbar.setTitle("You are online now");
                } else {
                    item.setIcon(R.drawable.online);
                    dashboard.toolbar.setTitle("Offline");
                }
            } else {
                Toast.makeText(this, "Profile is not active yet.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void toProfileActivity(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }

    private void fetchProfileIfActive() {
        progressDialog.showDialog();
        Call<verify_response_model_sub> call = ApiControllers.getInstance()
                .getApi()
                .getPhysicianById("Bearer " + preferenceManager.getString(Constants.token), preferenceManager.getString(Constants.mongoId));

        call.enqueue(new Callback<verify_response_model_sub>() {
            @Override
            public void onResponse(Call<verify_response_model_sub> call, Response<verify_response_model_sub> response) {
                progressDialog.dismissDialog();
                if (response.code() == 200) {
                    verify_response_model_sub data = response.body();
                    preferenceManager.putBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN, true);
                    preferenceManager.putString(Constants.mongoId, data.get_id());
                    preferenceManager.putString(Constants.role, data.getRole());
                    preferenceManager.putString(Constants.name, data.getName());
                    preferenceManager.putString(Constants.isFull, data.getIsFull());
                    preferenceManager.putString(Constants.address, data.getAddress());
                    preferenceManager.putString(Constants.description, data.getDescription());
                    preferenceManager.putString(Constants.qualifiation, data.getQualifiation());
                    preferenceManager.putString(Constants.specialityType, data.getSpecialityType());
                    preferenceManager.putString(Constants.mobile, data.getMobile());
                    preferenceManager.putString(Constants.price, data.getPrice());
                    preferenceManager.putString(Constants.image, data.getImage());
                    preferenceManager.putString(Constants.status, data.getStatus());
                    updateDoctorsData();
                }
            }

            @Override
            public void onFailure(Call<verify_response_model_sub> call, Throwable t) {
                progressDialog.dismissDialog();
                dialog.showDialog(DashboardActivity.this, "Error", t.getMessage().toString());
            }
        });
    }

    private void updateDoctorsData() {
        if (preferenceManager.getBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN)) {
            if (preferenceManager.getString(Constants.name) != null && preferenceManager.getString(Constants.name).length() > 0)
                doctorNameHeader.setText(preferenceManager.getString(Constants.name));

            if (preferenceManager.getString(Constants.specialityType) != null && preferenceManager.getString(Constants.specialityType).length() > 0)
                doctorSpecialityHeader.setText(preferenceManager.getString(Constants.specialityType));

            if (preferenceManager.getString(Constants.ratings) != null && preferenceManager.getString(Constants.ratings).length() > 0)
                doctorRatingsHeader.setText(preferenceManager.getString(Constants.ratings));

            if (preferenceManager.getString(Constants.image) != null && preferenceManager.getString(Constants.image).length() > 0) {
                byte[] profileImageByte;
                Bitmap profileImageBitmap;

                profileImageByte = Base64.decode(preferenceManager.getString(Constants.image), Base64.DEFAULT);
                profileImageBitmap = BitmapFactory.decodeByteArray(profileImageByte, 0, profileImageByte.length);
                doctorImageHeader.setImageBitmap(profileImageBitmap);
            }

            if (preferenceManager.getString(Constants.status).equals("inActive")) {
                dashboard.inActiveMsg.setVisibility(View.VISIBLE);
            }
            if (preferenceManager.getString(Constants.status).equals("pending")) {
                dashboard.inActiveMsg.setVisibility(View.VISIBLE);
                dashboard.statusMeg.setText("Profile activation request has been sent.\nWe will update you within 1 day.");
                dashboard.statusBtn.setText("Visit Profile Page");
            }
            if (preferenceManager.getString(Constants.status).equals("active")) {
                dashboard.inActiveMsg.setVisibility(View.GONE);
            }
        }
    }
}