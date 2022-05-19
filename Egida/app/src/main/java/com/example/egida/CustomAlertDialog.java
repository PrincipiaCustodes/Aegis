package com.example.egida;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public abstract class CustomAlertDialog {

    private Dialog dialog;

    private ImageView alertDialogImage;
    private TextView alertDialogTittle;
    private TextView alertDialogDescription;
    private TextView alertDialogQuestion;
    private Button alertDialogOkButton;
    private Button alertDialogNoButton;

    private boolean alertDialogCancelable;
    private int alertDialogImageId;
    private String newAlertDialogTittle;
    private String newAlertDialogDescription;
    private String newAlertDialogQuestion;
    private String newAlertDialogOkButton;
    private String newAlertDialogNoButton;

    public void setAlertDialogImageId(int alertDialogImageId) {
        this.alertDialogImageId = alertDialogImageId;
    }

    public void setNewAlertDialogTittle(String newAlertDialogTittle) {
        this.newAlertDialogTittle = newAlertDialogTittle;
    }

    public void setNewAlertDialogDescription(String newAlertDialogDescription) {
        this.newAlertDialogDescription = newAlertDialogDescription;
    }

    public void setNewAlertDialogQuestion(String newAlertDialogQuestion) {
        this.newAlertDialogQuestion = newAlertDialogQuestion;
    }

    public void setNewAlertDialogOkButton(String newAlertDialogOkButton) {
        this.newAlertDialogOkButton = newAlertDialogOkButton;
    }

    public void setNewAlertDialogNoButton(String newAlertDialogNoButton) {
        this.newAlertDialogNoButton = newAlertDialogNoButton;
    }

    public void setAlertDialogCancelable(boolean alertDialogCancelable) {
        this.alertDialogCancelable = alertDialogCancelable;
    }

    public Dialog getDialog() {
        return dialog;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    public CustomAlertDialog(Context currentContext) {
        dialog = new Dialog(currentContext);
        dialog.setContentView(R.layout.custom_alertdialog_layout);

        alertDialogOkButton = dialog.findViewById(R.id.custom_alertDialog_ok);
        alertDialogNoButton = dialog.findViewById(R.id.custom_alertDialog_no);
        alertDialogImage = dialog.findViewById(R.id.custom_alertDialog_image);
        alertDialogTittle = dialog.findViewById(R.id.custom_alertDialog_tittle);
        alertDialogDescription = dialog.findViewById(R.id.custom_alertDialog_description);
        alertDialogQuestion = dialog.findViewById(R.id.custom_alertDialog_question);

        dialog.getWindow().setBackgroundDrawable(currentContext.getDrawable(R.drawable.custom_alertdialog_background_inset));
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().getAttributes().windowAnimations = R.style.custom_alertdialog_animation;
        dialog.setCancelable(alertDialogCancelable);

        alertDialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                positiveAction();
            }
        });

        alertDialogNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                negativeAction();
            }
        });
    }

    public void setupAlertDialogSettings(){
        alertDialogImage.setImageResource(alertDialogImageId);
        alertDialogTittle.setText(newAlertDialogTittle);
        alertDialogDescription.setText(newAlertDialogDescription);
        alertDialogQuestion.setText(newAlertDialogQuestion);
        alertDialogOkButton.setText(newAlertDialogOkButton);
        alertDialogNoButton.setText(newAlertDialogNoButton);
    }

    public abstract void positiveAction();
    public abstract void negativeAction();
}
