package com.example.egida;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricManager;
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

    // hardware and settings check
    public void checkConditionals(Context currentContext, Activity currentActivity){
        BiometricManager biometricManager = BiometricManager.from(currentContext);
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                Toast.makeText(currentContext, "You can use biometrics", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                AlertDialogFragment alertDialog1 = new AlertDialogFragment("Warning!",
                        "This device don't have sensors", "OK", "NO");
                alertDialog1.show(((FragmentActivity)currentActivity).getSupportFragmentManager(), "alertDialog1");
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                Toast.makeText(currentContext,
                        "You don't have saved fingerprints or face, check your security settings", Toast.LENGTH_SHORT).show();
                AlertDialogFragment alertDialog2 = new AlertDialogFragment("Warning!",
                        "You don't have saved fingerprints or face, check your security settings",
                        "OK", "NO");
                alertDialog2.show(((FragmentActivity)currentActivity).getSupportFragmentManager(), "alertDialog1");
                break;
        }
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
