package com.example.egida;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class SplashScreen extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            Keys keys = new Keys();
        } catch (IOException e) {
            e.printStackTrace();
        }

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