package com.example.wayyeasydoctors.Fragments;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.wayyeasydoctors.Activities.Dashboard;
import com.example.wayyeasydoctors.ComponentFiles.Constants.Constants;
import com.example.wayyeasydoctors.R;
import com.example.wayyeasydoctors.databinding.FragmentRegisterBinding;

public class RegisterFragment extends Fragment {

    FragmentRegisterBinding registerBinding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        registerBinding = FragmentRegisterBinding.inflate(inflater, container, false);

        registerBinding.registerHeading.setPaintFlags(registerBinding.registerHeading.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        registerBinding.registerBtn.setOnClickListener(view ->
                registerUser(registerBinding.name.getText().toString().trim(), registerBinding.email.getText().toString().trim(),
                registerBinding.mobile.getText().toString().trim(), registerBinding.password.getText().toString().trim(),
                registerBinding.cPassword.getText().toString().trim()));

        registerBinding.cPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (!registerBinding.password.getText().toString().trim().equals(charSequence)) {
                    registerBinding.errorMsg.setText("Passwords does not match !");
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        registerBinding.toLogin.setOnClickListener(view -> getFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.left_to_right_slider, R.anim.right_to_left_slider)
                .replace(R.id.frame, new LoginFragment()).commit());

        return registerBinding.getRoot();
    }

    private void registerUser(String name, String email, String mobile, String password, String cPassword) {
        if (name.length() == 0 || email.length() == 0 || mobile.length() == 0 || password.length() == 0 || cPassword.length() == 0) {
            registerBinding.errorMsg.setText("All fields are mandatory");
            return;
        }

        if (!password.equals(cPassword)) return;

        if (email.matches(Constants.emailPattern)) {
            if (mobile != null && mobile.length() > 8) {
                registerBinding.errorMsg.setVisibility(View.GONE);
                toRegisterProcess(name, email, mobile, password);
            } else
                registerBinding.errorMsg.setText("Please enter a valid number");
        } else
            registerBinding.errorMsg.setText("Please enter a valid email address");
    }

    private void toRegisterProcess(String name, String email, String mobile, String password) {
        startActivity(new Intent(getActivity(), Dashboard.class));
    }
}