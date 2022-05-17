package com.example.egida;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class SignUpPasswordFragment extends Fragment {

    private ImageView nextButton;
    private EditText password;
    private CustomAlertDialog customAlertDialog;

    private SharedPreferences sharedPref;
    private static final String PASSWORD_PREF_TAG = "password";
    private static final String SECURITY_STATUS_PREF_TAG = "security_status";
    private static final String SIGNUP_STATUS_PREF_TAG = "signup_status";

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_password, container, false);

        nextButton = view.findViewById(R.id.next_btn);
        password = view.findViewById(R.id.password_input_textField);

        {
            customAlertDialog = new CustomAlertDialog(getActivity()) {
                @Override
                public void positiveAction() {
                    saveSecurityStatus("use biometrics");
                    savePassword();
                    saveSignUpStatus("account_created");
                    Biometrics biometrics = new Biometrics() {
                        @Override
                        public void nextAction() {
                            Intent intent = new Intent(getActivity(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    };

                    biometrics.biometricsPrompt(getContext());
                }

                @Override
                public void negativeAction() {
                    getDialog().dismiss();
                }
            };
            customAlertDialog.setAlertDialogImageId(R.drawable.icon);
            customAlertDialog.setNewAlertDialogTittle("You can use biometrics");
            customAlertDialog.setNewAlertDialogDescription("This data will use for confirm action and login in app");
            customAlertDialog.setNewAlertDialogQuestion("Do you want use your biometrics data?");
            customAlertDialog.setNewAlertDialogOkButton("Yes, I do");
            customAlertDialog.setNewAlertDialogNoButton("No, use password");
            customAlertDialog.setupAlertDialogSettings();
        }

        BiometricManager biometricManager = BiometricManager.from(getActivity().getApplicationContext());
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                customAlertDialog.getDialog().show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                customAlertDialog.setNewAlertDialogTittle("You can use biometrics, but you don't have saved fingerprint or face. " +
                        "Please, check security settings of your phone");
                customAlertDialog.getDialog().show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                break;
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().equals("")) {
                    try {
                        saveSecurityStatus("use password");
                        savePassword(password.getText().toString());
                        saveSignUpStatus("account_created");
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter the password!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return view;
    }

    // сохраняем пароль, если он будет использоваться
    private void savePassword(String password) throws NoSuchAlgorithmException {
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("Test", "password: " + password);
        editor.putString(PASSWORD_PREF_TAG, new ShaEncoder(password).sha256EncodeInput());
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(), "Password saved", Toast.LENGTH_SHORT).show();
    }

    // сохраняем "" в поле пароля, если он не будет использоваться
    private void savePassword() {
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        Log.d("Test", "password: " + password);
        editor.putString(PASSWORD_PREF_TAG, "");
        editor.commit();
    }

    // сохранение статуса (уровня / состояния) безопасности
    private void saveSecurityStatus(String status){
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SECURITY_STATUS_PREF_TAG, status);
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(), "Security is done!", Toast.LENGTH_SHORT).show();
    }

    private void saveSignUpStatus(String status){
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(SIGNUP_STATUS_PREF_TAG, status);
        editor.commit();
    }
}