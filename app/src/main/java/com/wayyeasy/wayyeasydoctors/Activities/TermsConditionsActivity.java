package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wayyeasy.wayyeasydoctors.databinding.ActivityTermsConditionsBinding;


public class TermsConditionsActivity extends AppCompatActivity {

    ActivityTermsConditionsBinding termsConditionsBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        termsConditionsBinding = ActivityTermsConditionsBinding.inflate(getLayoutInflater());
        setContentView(termsConditionsBinding.getRoot());
    }
}