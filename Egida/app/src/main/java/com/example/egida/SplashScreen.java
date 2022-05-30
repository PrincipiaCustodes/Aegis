package com.example.egida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Keys keys = new Keys();
        Keys.Data data = keys.new Data();

        SharedPrefs.setFIRST_START(this, true);

        if(SharedPrefs.getSIGNUP_STATUS(this).equals(getString(R.string.signup_status_created))){
            startNextActivity(LogInActivity.class);
        } else {
            startNextActivity(SignUpActivity.class);
        }
    }

    private void startNextActivity(Class nextActivity){
        SharedPrefs.setFIRST_START(this, false);

        Intent intent = new Intent(getApplicationContext(), nextActivity);
        startActivity(intent);
        finish();
    }
}