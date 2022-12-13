package com.example.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.wayyeasydoctors.databinding.ActivityDashboardBinding;
import com.example.wayyeasydoctors.databinding.ActivitySplashScreenBinding;

public class Dashboard extends AppCompatActivity {

    ActivityDashboardBinding dashboard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboard = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(dashboard.getRoot());
    }
}