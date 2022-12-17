package com.wayyeasy.wayyeasydoctors.Activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;

import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.ActivityDashboardBinding;

public class DashboardActivity extends AppCompatActivity {

    ActivityDashboardBinding dashboard;
    ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboard = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboard.getRoot());

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
    }
}