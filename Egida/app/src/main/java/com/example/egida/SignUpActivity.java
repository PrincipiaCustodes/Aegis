package com.example.egida;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        SignUpNicknameFragment signUpNicknameFragment = new SignUpNicknameFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.signup_fragment_container, signUpNicknameFragment).commit();
    }
}