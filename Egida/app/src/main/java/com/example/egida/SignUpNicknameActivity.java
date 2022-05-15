package com.example.egida;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class SignUpNicknameActivity extends AppCompatActivity {

    private ImageView nextButton;
    private EditText nickname;

    private SharedPreferences sharedPref;
    final private String PREF_TAG = "nickname";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_nickname);

        nextButton = findViewById(R.id.next_btn_nickname);
        nickname = findViewById(R.id.nickname_input_textField);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNickname();
                Toast.makeText(getApplicationContext(), "text: " + nickname.getText().toString(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), SignUpPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void saveNickname() {
        sharedPref = getSharedPreferences("PreferencesFile", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_TAG, nickname.getText().toString());
        editor.commit();
        Toast.makeText(getApplicationContext(), "nickname: " + nickname.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}