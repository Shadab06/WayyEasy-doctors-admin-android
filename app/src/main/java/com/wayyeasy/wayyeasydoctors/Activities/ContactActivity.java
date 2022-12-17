package com.wayyeasy.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.wayyeasy.wayyeasydoctors.databinding.ActivityContactBinding;

public class ContactActivity extends AppCompatActivity {

    ActivityContactBinding contactBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        contactBinding = ActivityContactBinding.inflate(getLayoutInflater());
        setContentView(contactBinding.getRoot());
    }
}