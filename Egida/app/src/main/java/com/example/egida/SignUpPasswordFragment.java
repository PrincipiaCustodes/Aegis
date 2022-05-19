package com.example.egida;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class SignUpPasswordFragment extends Fragment {

    private ImageView nextButton;
    private EditText password;

    private CustomAlertDialog customAlertDialog1;
    private CustomAlertDialog customAlertDialog2;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_password, container, false);

        nextButton = view.findViewById(R.id.next_btn);
        password = view.findViewById(R.id.password_input_textField);

        BiometricManager biometricManager = BiometricManager.from(getActivity().getApplicationContext());
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                // alertDialog, о том, что ПРЯМО СЕЙЧАС пользователь может использовать билметрические данные
                customAlertDialog1 = new CustomAlertDialog(getActivity()) {
                    @Override
                    public void positiveAction() {
                        SharedPrefs.setBIOMETRICS_STATUS(getContext(), getString(R.string.biometrics_status_use));
                        Biometrics biometrics = new Biometrics() {
                            @Override
                            public void nextAction() { }
                        };
                        getDialog().dismiss();
                        biometrics.biometricsPrompt(getContext());
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
                SharedPrefs.setBIOMETRICS_STATUS(getContext(), getString(R.string.biometrics_status_none_enrolled));

                // alertDialog, о том, что пользователь может использовать билметрические данные, НО ДОЛЖЕН НАСТРОИТЬ ИХ
                customAlertDialog2 = new CustomAlertDialog(getActivity()) {
                    @Override
                    public void positiveAction() {
                        // открываем найтроки телефона, чтобы пользователь настроил биометрические данные
                        startActivityForResult(new Intent(android.provider.Settings.ACTION_SECURITY_SETTINGS), 0);
                        getActivity().finish();
                    }

                    @Override
                    public void negativeAction() {
                        getDialog().dismiss();
                    }
                };

                customAlertDialog2.setAlertDialogImageId(R.drawable.icon);
                customAlertDialog2.setNewAlertDialogTittle(getString(R.string.about_biometrics_alertDialog_status_none_enrolled));
                customAlertDialog2.setNewAlertDialogDescription(getString(R.string.about_biometrics_alertDialog_status_description));
                customAlertDialog2.setNewAlertDialogQuestion(getString(R.string.about_biometrics_alertDialog_status_question));
                customAlertDialog2.setNewAlertDialogOkButton(getString(R.string.about_biometrics_alertDialog_status_check_settings));
                customAlertDialog2.setNewAlertDialogNoButton(getString(R.string.about_biometrics_alertDialog_status_no));
                customAlertDialog2.setupAlertDialogSettings();
                customAlertDialog2.getDialog().show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                SharedPrefs.setBIOMETRICS_STATUS(getContext(), getString(R.string.biometrics_status_no_hardware));
                break;
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!password.getText().toString().equals("")) {
                    try {
                        Log.d("Password", "pass1: " + password.getText().toString());
                        Log.d("Password", "pass2: " + new ShaEncoder(password.getText().toString()).sha256EncodeInput());
                        SharedPrefs.setPASSWORD(getContext(), password.getText().toString());
                    } catch (NoSuchAlgorithmException e) {
                        e.printStackTrace();
                    }

                    SharedPrefs.setSIGNUP_STATUS(getContext(), getString(R.string.signup_status_created));

                    Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), getString(R.string.enter_password), Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return view;
    }
}