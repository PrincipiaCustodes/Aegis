package com.example.egida;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.biometric.BiometricManager;
import androidx.biometric.BiometricPrompt;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import java.util.concurrent.Executor;

public class BiometricsFragment extends DialogFragment {
    private Executor executor;
    private BiometricPrompt biometricPrompt;
    private BiometricPrompt.PromptInfo promptInfo;

    private boolean authResult = false;

    TextView stat;

    Fragment nextFragment;

    BiometricsFragment(Fragment nextFragment){
        this.nextFragment = nextFragment;
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // check hardware
        BiometricManager biometricManager = BiometricManager.from(getActivity().getApplicationContext());
        switch (biometricManager.canAuthenticate()) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // TODO: функция если всё ок
                Toast.makeText(getActivity().getApplicationContext(),
                        "You can use biometrics", Toast.LENGTH_SHORT).show();

                // make biometrics dialog
                executor = ContextCompat.getMainExecutor(getActivity().getApplicationContext());
                biometricPrompt = new BiometricPrompt(getActivity(), executor, new BiometricPrompt.AuthenticationCallback() {
                    @Override
                    public void onAuthenticationError(int errorCode, @NonNull CharSequence errString) {
                        super.onAuthenticationError(errorCode, errString);
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Authentication error: " + errString, Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onAuthenticationSucceeded(@NonNull BiometricPrompt.AuthenticationResult result) {
                        super.onAuthenticationSucceeded(result);
                        Toast.makeText(getActivity().getApplicationContext(),
                                "Authentication succeeded!", Toast.LENGTH_SHORT).show();
                        stat.setText("OK");
                        authResult = true;

                        Bundle bundle = new Bundle();
                        bundle.putBoolean("auth", authResult);
                        nextFragment.setArguments(bundle);
                    }

                    @Override
                    public void onAuthenticationFailed() {
                        super.onAuthenticationFailed();
                        Toast.makeText(getActivity().getApplicationContext(), "Authentication failed", Toast.LENGTH_SHORT).show();
                    }
                });

                // setup auth dialog
                promptInfo = new BiometricPrompt.PromptInfo.Builder()
                        .setTitle("Tittle | Biometric authentication")
                        .setSubtitle("SubTittle | Fingerprint or face")
                        .setDescription("Description")
                        .setNegativeButtonText("Cancel")
                        .build();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                // TODO: функция для тех, у кого нет датчиков
                Toast.makeText(getActivity().getApplicationContext(),
                        "This device don't have sensors", Toast.LENGTH_SHORT).show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                // TODO: функция для тех, у кого не настроены биометрические данные
                Toast.makeText(getActivity().getApplicationContext(),
                        "You don't have saved fingerprints or face, check your security settings", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_biometrics, container, false);
        stat = view.findViewById(R.id.stat);

        biometricPrompt.authenticate(promptInfo);

        return view;
    }
}
