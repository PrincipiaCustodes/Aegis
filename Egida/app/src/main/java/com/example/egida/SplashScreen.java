package com.example.egida;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    private final String FIRST_START_TAG = "is_first_start";
    private final String SIGNUP_STATUS_PREF_TAG = "signup_status";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        SharedPreferences sharedPreferences = getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        boolean isFirstStart = sharedPreferences.getBoolean(FIRST_START_TAG, true);

        if(sharedPreferences.getString(SIGNUP_STATUS_PREF_TAG, "").equals("account_created")){
            startNextActivity(LogInActivity.class);
        } else {
            startNextActivity(SignUpActivity.class);
        }
    }

    private void startNextActivity(Class nextActivity){
        SharedPreferences sharedPreferences = getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(FIRST_START_TAG, false);
        editor.apply();

        Intent intent = new Intent(getApplicationContext(), nextActivity);
        startActivity(intent);
        finish();
    }
}