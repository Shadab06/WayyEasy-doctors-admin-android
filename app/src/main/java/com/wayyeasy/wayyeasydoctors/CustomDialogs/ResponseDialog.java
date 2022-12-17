package com.wayyeasy.wayyeasydoctors.CustomDialogs;

import android.app.Activity;
import android.app.Dialog;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.wayyeasy.wayyeasydoctors.R;

public class ResponseDialog {
    public void showDialog(Activity activity, String header, String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.dialog_response_layout);

        TextView dialogHeader = (TextView) dialog.findViewById(R.id.dialog_header);
        TextView dialogMessage = (TextView) dialog.findViewById(R.id.dialog_message);

        dialogHeader.setText(header);
        dialogMessage.setText(message);

        Button btnOK = (Button) dialog.findViewById(R.id.dialog_btn_ok);

        btnOK.setOnClickListener(v -> dialog.dismiss());

        dialog.show();
    }
}
