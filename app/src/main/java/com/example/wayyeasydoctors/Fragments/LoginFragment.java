package com.example.wayyeasydoctors.Fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wayyeasydoctors.Activities.Dashboard;
import com.example.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.example.wayyeasydoctors.R;
import com.example.wayyeasydoctors.databinding.FragmentLoginBinding;
import com.example.wayyeasydoctors.databinding.FragmentRegisterBinding;

public class LoginFragment extends Fragment {

    FragmentLoginBinding loginBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);

        loginBinding.loginHeading.setPaintFlags(loginBinding.loginHeading.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        loginBinding.loginBtn.setOnClickListener(view -> {
            if (loginBinding.emailMobile.getText().toString().length() > 9) {
                if (loginBinding.password.getText().toString().equals("") && loginBinding.password.getText().length() < 8) {
                    loginBinding.errorMsg.setVisibility(View.VISIBLE);
                    loginBinding.errorMsg.setText("Password should be of 8 characters.");
                } else
                    loginBinding.errorMsg.setVisibility(View.GONE);
                loginUser(loginBinding.emailMobile.getText().toString().trim(), loginBinding.password.getText().toString().trim());
            } else {
                loginBinding.errorMsg.setVisibility(View.VISIBLE);
                loginBinding.errorMsg.setText("Please enter proper email or mobile !!!");
            }
        });

        loginBinding.toRegister.setOnClickListener(view -> {
            getFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.left_to_right_slider, R.anim.right_to_left_slider)
                    .replace(R.id.frame, new RegisterFragment()).commit();
        });

        return loginBinding.getRoot();
    }

    private void loginUser(String email, String password) {
        startActivity(new Intent(getActivity(), Dashboard.class));
    }
}