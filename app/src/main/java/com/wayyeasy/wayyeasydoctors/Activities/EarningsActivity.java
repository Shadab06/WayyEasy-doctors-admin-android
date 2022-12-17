package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.wayyeasy.wayyeasydoctors.databinding.ActivityEarningsBinding;

public class EarningsActivity extends AppCompatActivity {

    ActivityEarningsBinding earningsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        earningsBinding = ActivityEarningsBinding.inflate(getLayoutInflater());
        setContentView(earningsBinding.getRoot());
    }
}