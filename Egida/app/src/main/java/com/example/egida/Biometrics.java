package com.example.egida;


import android.app.Activity;
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

public class Biometrics {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    // для открытия активностей
    public void biometricsPrompt(Context currentContext, Class nextClass){
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
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(currentContext, nextClass);
                currentContext.startActivity(intent);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(currentContext, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        setupAuthDialog();
    }

    // для открытия фрагментов
    public void biometricsPrompt(Context currentContext, int id, Fragment nextFragment){
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
                        "Authentication succeeded!", Toast.LENGTH_SHORT).show();

                ((FragmentActivity)currentContext).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(id, nextFragment)
                        .commit();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(currentContext, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        setupAuthDialog();
    }

    private void setupAuthDialog(){
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Tittle | Biometric authentication")
                .setSubtitle("SubTittle | Fingerprint or face")
                .setDescription("Description")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
