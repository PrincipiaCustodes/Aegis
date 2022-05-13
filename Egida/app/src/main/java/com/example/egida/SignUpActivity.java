package com.example.egida;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.Executor;

public class SignUpActivity extends AppCompatActivity {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private Button signUpButton;
    private Button infoBiometrics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        signUpButton = findViewById(R.id.signup_btn);
        infoBiometrics = findViewById(R.id.about_biometric_textButton);

        BiometricManager biometricManager = BiometricManager.from(getApplicationContext());
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                infoBiometrics.setText(getResources().getString(R.string.biometrics_signUp_state_1));
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                infoBiometrics.setVisibility(View.GONE);
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                infoBiometrics.setText(getResources().getString(R.string.biometrics_signUp_state_2));
                break;
        }

        View.OnClickListener onClickListener = new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                executor = ContextCompat.getMainExecutor(getApplicationContext());
                biometricPrompt = new BiometricPrompt(SignUpActivity.this, executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getApplicationContext(),
                                "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });

                // setup auth dialog
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Welcome!")
                        .setSubtitle("SubTittle | Fingerprint or face")
                        .setDescription("Description")
                        .setNegativeButtonText("Cancel")
                        .build();

                biometricPrompt.authenticate(promptInfo);
            }
        };

        signUpButton.setOnClickListener(onClickListener);
        infoBiometrics.setOnClickListener(onClickListener);
    }
}