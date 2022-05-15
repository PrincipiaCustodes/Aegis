package com.example.egida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class LogInActivity extends AppCompatActivity {

    private static final String NICKNAME_PREF_TAG = "nickname";
    private static final String PASSWORD_PREF_TAG = "password";
    private static final String SECURITY_STATUS_PREF_TAG = "security_status";

    private TextView helloText;
    private EditText password;
    private ImageView nextButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        helloText = findViewById(R.id.hello_text);
        password = findViewById(R.id.login_password_input_textField);
        nextButton = findViewById(R.id.login_next_btn);

        helloText.setText("Glad to see you again, " + getNickname());

        if(getSecurityStatus().equals("use biometrics")){
            Biometrics biometrics = new Biometrics();
            biometrics.biometricsPrompt(LogInActivity.this, MainActivity.class);
        } else {
            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    try {
                        if((new ShaEncoder(password.getText().toString()).sha256EncodeInput()).equals(getPassword())){
                            Toast.makeText(getApplicationContext(), "Authentication successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(getApplicationContext(), "Wrong password", Toast.LENGTH_SHORT).show();
                        }
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private String getNickname(){
        SharedPreferences sharedPref = getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        return sharedPref.getString(NICKNAME_PREF_TAG, "nickname_error");
    }

    private String getPassword(){
        SharedPreferences sharedPref = getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        return sharedPref.getString(PASSWORD_PREF_TAG, "password_error");
    }

    private String getSecurityStatus(){
        SharedPreferences sharedPref = getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        return sharedPref.getString(SECURITY_STATUS_PREF_TAG, "security_status_error");
    }
}