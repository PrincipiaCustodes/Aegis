package com.example.egida;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;

import android.annotation.SuppressLint;
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

    private TextView welcomeText;
    private EditText password;
    private ImageView nextButton;

    private CustomAlertDialog customAlertDialog1;
    // добавить диалог

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        welcomeText = findViewById(R.id.hello_text);
        password = findViewById(R.id.login_password_input_textField);
        nextButton = findViewById(R.id.login_next_btn);

        getWindow().setNavigationBarColor(getResources().getColor(R.color.black));

        welcomeText.setText(getString(R.string.login_activity_welcome_text) + " " + SharedPrefs.getNICKNAME(this));

        // если пользователь использует биометрические данные, то вызываем диалог биометрии, иначе запрашиваем пароль
        if(SharedPrefs.getBIOMETRICS_STATUS(this).equals(getString(R.string.biometrics_status_use))){
            Biometrics biometrics = new Biometrics() {
                @Override
                public void nextAction() {
                    Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            };
            biometrics.biometricsPrompt(this);
        } else {
            canUseBiometrics();
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // сравниваем хеш введённого пароля с сохранённым
                try {
                    if((new ShaEncoder(password.getText().toString()).sha256EncodeInput())
                            .equals(SharedPrefs.getPASSWORD(getApplicationContext()))) {
                        Toast.makeText(getApplicationContext(), getString(R.string.authentication_successful),
                                Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(getApplicationContext(), getString(R.string.wrong_password),
                                Toast.LENGTH_SHORT).show();
                    }
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    private void canUseBiometrics(){
        BiometricManager biometricManager = BiometricManager.from(this);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // alertDialog, о том, что ПРЯМО СЕЙЧАС пользователь может использовать билметрические данные
                customAlertDialog1 = new CustomAlertDialog(this) {
                    @Override
                    public void positiveAction() {
                        SharedPrefs.setBIOMETRICS_STATUS(getApplicationContext(), getString(R.string.biometrics_status_use));

                        // рестарт активности
                        Intent intent = getIntent();
                        finish();
                        startActivity(intent);
                    }

                    @Override
                    public void negativeAction() {
                        getDialog().dismiss();
                    }
                };

                customAlertDialog1.setAlertDialogImageId(R.drawable.icon);
                customAlertDialog1.setNewAlertDialogTittle(getString(R.string.about_biometrics_alertDialog_status_can_use));
                customAlertDialog1.setNewAlertDialogDescription(getString(R.string.about_biometrics_alertDialog_status_description));
                customAlertDialog1.setNewAlertDialogQuestion(getString(R.string.about_biometrics_alertDialog_status_question));
                customAlertDialog1.setNewAlertDialogOkButton(getString(R.string.about_biometrics_alertDialog_status_ok));
                customAlertDialog1.setNewAlertDialogNoButton(getString(R.string.about_biometrics_alertDialog_status_no));
                customAlertDialog1.setupAlertDialogSettings();
                customAlertDialog1.getDialog().show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                break;
        }
    }
}