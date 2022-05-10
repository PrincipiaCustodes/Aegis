package com.example.egida;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class Biometrics {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private void nextOpen(Context currentContext, Class nextClass){
        Intent intent = new Intent(currentContext, nextClass);
        currentContext.startActivity(intent);
    }

    private void nextOpen(Activity currentActivity, int id, Fragment nextFragment){
        int ft = ((FragmentActivity)currentActivity).getSupportFragmentManager()
                .beginTransaction()
                .replace(id, nextFragment)
                .commit();
    }

    private void checkConditionals(Context currentContext){
        BiometricManager biometricManager = BiometricManager.from(currentContext);
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // TODO: функция если всё ок
                Toast.makeText(currentContext, "You can use biometrics", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // TODO: функция для тех, у кого нет датчиков
                Toast.makeText(currentContext, "This device don't have sensors", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // TODO: функция для тех, у кого не настроены биометрические данные
                Toast.makeText(currentContext,
                        "You don't have saved fingerprints or face, check your security settings", Toast.LENGTH_SHORT).show();
                break;
        }
    }

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
                nextOpen(currentContext, nextClass);
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(currentContext, "Authentication failed", Toast.LENGTH_SHORT).show();
            }
        });

        // setup auth dialog
        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle("Tittle | Biometric authentication")
                .setSubtitle("SubTittle | Fingerprint or face")
                .setDescription("Description")
                .setNegativeButtonText("Cancel")
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}