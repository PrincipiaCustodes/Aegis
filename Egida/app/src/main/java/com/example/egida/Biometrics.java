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

    // for open next activity
    private void nextOpen(Context currentContext, Class nextClass){
        Intent intent = new Intent(currentContext, nextClass);
        currentContext.startActivity(intent);
    }

    // for open next fragment
    private void nextOpen(Activity currentActivity, int id, Fragment nextFragment){
        ((FragmentActivity)currentActivity).getSupportFragmentManager()
                .beginTransaction()
                .replace(id, nextFragment)
                .commit();
    }

    public void biometricsPrompt(Context currentContext, Class nextClass, Activity currentActivity, int id, Fragment nextFragment, String type){
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

                // what open next? activity or fragment
                if(type.equals("activity")){
                    nextOpen(currentContext, nextClass);
                } else if (type.equals("fragment")){
                    nextOpen(currentActivity, id, nextFragment);
                }
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
