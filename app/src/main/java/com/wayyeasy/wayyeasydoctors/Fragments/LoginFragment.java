package com.wayyeasy.wayyeasydoctors.Fragments;

import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.wayyeasy.wayyeasydoctors.ComponentFiles.ApiHandlers.ApiControllers;
import com.wayyeasy.wayyeasydoctors.CustomDialogs.ResponseDialog;
import com.wayyeasy.wayyeasydoctors.Models.verify_response_model;
import com.wayyeasy.wayyeasydoctors.R;
import com.wayyeasy.wayyeasydoctors.databinding.FragmentLoginBinding;

import java.util.HashMap;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginFragment extends Fragment {

    FragmentLoginBinding loginBinding;
    ResponseDialog dialog;
    public static final String TAG = "Login Fragment";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        loginBinding = FragmentLoginBinding.inflate(inflater, container, false);
        loginBinding.loginHeading.setPaintFlags(loginBinding.loginHeading.getPaintFlags() |   Paint.UNDERLINE_TEXT_FLAG);

        dialog = new ResponseDialog();

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

        HashMap<String, String> loginData = new HashMap<>();
        loginData.put("email", email);
        loginData.put("password", password);

        Call<verify_response_model> call = ApiControllers.getInstance()
                .getApi()
                .login(loginData);

        call.enqueue(new Callback<verify_response_model>() {
            @Override
            public void onResponse(Call<verify_response_model> call, Response<verify_response_model> response) {

            }

            @Override
            public void onFailure(Call<verify_response_model> call, Throwable t) {
                dialog.showDialog(getActivity(), "Login Failed", t.toString());
            }
        });
    }
}