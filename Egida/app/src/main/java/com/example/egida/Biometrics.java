package com.example.egida;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import java.util.concurrent.Executor;

public abstract class Biometrics {

    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    public abstract void nextAction();

    public void biometricsPrompt(Context currentContext){
        executor = ContextCompat.getMainExecutor(currentContext);
        biometricPrompt = new BiometricPrompt((FragmentActivity) currentContext, executor, new BiometricPrompt.AuthenticationCallback() {
            @Override
            public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                super.onAuthenticationError(errorCode, errString);
                Toast.makeText(currentContext, currentContext.getString(R.string.authentication_error) + errString,
                        Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                super.onAuthenticationSucceeded(result);
                Toast.makeText(currentContext, currentContext.getString(R.string.authentication_successful),
                        Toast.LENGTH_SHORT).show();

                nextAction();
            }

            @Override
            public void onAuthenticationFailed() {
                super.onAuthenticationFailed();
                Toast.makeText(currentContext, currentContext.getString(R.string.authentication_failure),
                        Toast.LENGTH_SHORT).show();
            }
        });

        promptInfo = new BiometricPrompt.PromptInfo.Builder()
                .setTitle(currentContext.getString(R.string.biometrics_dialog_title))
                .setSubtitle(currentContext.getString(R.string.biometrics_dialog_subtitle))
                .setDescription(currentContext.getString(R.string.biometrics_dialog_description))
                .setNegativeButtonText(currentContext.getText(R.string.biometrics_dialog_negative_button_text))
                .build();

        biometricPrompt.authenticate(promptInfo);
    }
}
