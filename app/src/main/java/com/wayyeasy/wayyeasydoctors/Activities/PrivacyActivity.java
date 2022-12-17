package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wayyeasy.wayyeasydoctors.databinding.ActivityPrivacyBinding;

public class PrivacyActivity extends AppCompatActivity {

    ActivityPrivacyBinding privacyBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        privacyBinding = ActivityPrivacyBinding.inflate(getLayoutInflater());
        setContentView(privacyBinding.getRoot());
    }
}