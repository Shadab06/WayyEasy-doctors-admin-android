package com.wayyeasy.wayyeasydoctors.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.wayyeasy.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.SharedPreferenceManager;
import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding dashboard;
    ActionBarDrawerToggle toggle;
    private TextView userName, userSpeciality, userRatings;
    private ImageView userImage;
    private boolean isOnline = false;
    private MenuItem availability;
    SharedPreferenceManager preferenceManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboard = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboard.getRoot());

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
        userName = header.findViewById(R.id.user_name);
        userSpeciality = header.findViewById(R.id.user_speciality);
        userRatings = header.findViewById(R.id.user_rating);
        userImage = header.findViewById(R.id.user_profile);

        //Menu ends

        header.setOnClickListener(view -> startActivity(new Intent(getApplicationContext(), ProfileActivity.class)));

        preferenceManager = new SharedPreferenceManager(getApplicationContext());
        if (preferenceManager.getBoolean(Constants.KEY_IS_DOCTOR_SIGNED_IN)) {
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
        if (isOnline) {
            item.setIcon(R.drawable.offline);
            dashboard.toolbar.setTitle("You are online now");
        } else {
            item.setIcon(R.drawable.online);
            dashboard.toolbar.setTitle("Offline");
        }
    }

    public void toProfileActivity(View view) {
        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
    }
}