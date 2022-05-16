package com.example.egida;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.security.NoSuchAlgorithmException;

public abstract class Password {

    private static final String PASSWORD_PREF_TAG = "password";

    Dialog dialog;
    private EditText password;
    private Button alertDialogOkButton;
    private Button alertDialogNoButton;
    private Context currentContext;

    public Password(Context currentContext){
        this.currentContext = currentContext;

        dialog = new Dialog(currentContext);
        dialog.setContentView(R.layout.password_alertdialog_layout);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            dialog.getWindow().setBackgroundDrawable(currentContext.getDrawable(R.drawable.custom_alertdialog_background_inset));
        }
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.setCancelable(false);   // устанавливает можно ли отменить диалог клавишей "назад"
        dialog.getWindow().getAttributes().windowAnimations = R.style.custom_alertdialog_animation; // анимация для диалога

        alertDialogOkButton = dialog.findViewById(R.id.password_alertDialog_ok);
        alertDialogNoButton = dialog.findViewById(R.id.password_alertDialog_no);
        password = dialog.findViewById(R.id.password_password_input_textField);

        alertDialogNoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        alertDialogOkButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if((new ShaEncoder(password.getText().toString()).sha256EncodeInput()).equals(getPassword())){
                        nextAction();
                        dialog.dismiss();
                    } else {
                        Toast.makeText(currentContext, "Wrong password", Toast.LENGTH_SHORT).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });

        dialog.show();
    }

    public abstract void nextAction();

    private String getPassword(){
        SharedPreferences sharedPref = currentContext.getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        return sharedPref.getString(PASSWORD_PREF_TAG, "password_error");
    }
}
