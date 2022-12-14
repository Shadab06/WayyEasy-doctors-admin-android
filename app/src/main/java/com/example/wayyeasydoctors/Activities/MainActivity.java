package com.example.wayyeasydoctors.Activities;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.example.wayyeasydoctors.Fragments.LoginFragment;
import com.example.wayyeasydoctors.R;
import com.example.wayyeasydoctors.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding mainBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(mainBinding.getRoot());

        getSupportFragmentManager().beginTransaction().add(R.id.frame, new LoginFragment()).commit();
    }
}