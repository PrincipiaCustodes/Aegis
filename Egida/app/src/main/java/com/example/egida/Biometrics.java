package com.example.egida;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public abstract class Biometrics {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    // для настройки вида диалога
    private String dialogTittle;
    private String dialogSubTittle;
    private String dialogDescription;
    private String dialogNegativeButtonText;

    public abstract void nextAction();             // абстарктные метод, который будет переопределяться для конкретных случаев

    public void biometricsPrompt(Context currentContext){
        Log.d("auth", "start biometricPrompt method");
        executor = ContextCompat.getMainExecutor(currentContext);
        biometricPrompt = new BiometricPrompt((FragmentActivity) currentContext, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(currentContext,
                        "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(currentContext,
                        "Authentication successful!", Toast.LENGTH_SHORT).show();
                nextAction();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(currentContext, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Authentication")
                .setSubtitle("Please verify your identity to continue")
                .setDescription("Use fingerprint or face")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
