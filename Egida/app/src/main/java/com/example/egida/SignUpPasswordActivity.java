package com.example.egida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class SignUpPasswordActivity extends AppCompatActivity {

    private ImageView nextButton;
    private EditText password;

    private SharedPreferences sharedPref;
    final private String PREF_TAG = "password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_password);

        nextButton = findViewById(R.id.next_btn);
        password = findViewById(R.id.password_input_textField);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    savePassword();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void savePassword() throws NoSuchAlgorithmException {
        sharedPref = getSharedPreferences("PreferencesFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_TAG, new ShaEncoder(password.getText().toString()).sha256EncodeInput());
        editor.commit();
        Toast.makeText(getApplicationContext(), "password saved", Toast.LENGTH_SHORT).show();
    }
}