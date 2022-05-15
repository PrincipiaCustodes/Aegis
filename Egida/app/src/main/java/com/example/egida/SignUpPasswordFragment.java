package com.example.egida;

import static androidx.biometric.BiometricManager.Authenticators.BIOMETRIC_STRONG;
import static androidx.biometric.BiometricManager.Authenticators.DEVICE_CREDENTIAL;

import androidx.biometric.BiometricManager;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
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
    private Button alertDialogOkButton;
    private Button alertDialogNoButton;

    private SharedPreferences sharedPref;
    final private String PASSWORD_PREF_TAG = "password";
    final private String BIOMETRICS_PREF_TAG = "biometrics_status";

    Dialog dialog;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_password, container, false);

        nextButton = view.findViewById(R.id.next_btn);
        password = view.findViewById(R.id.password_input_textField);

        // alert dialog creation block
        {
            dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.custom_alertdialog_layout);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                dialog.getWindow().setBackgroundDrawable(getActivity().getDrawable(R.drawable.custom_alertdialog_background_inset));
            }
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.setCancelable(false);   // устанавливает можно ли отменить диалог клавишей "назад"
            dialog.getWindow().getAttributes().windowAnimations = R.style.custom_alertdialog_animation; // анимация для диалога

            alertDialogOkButton = dialog.findViewById(R.id.alertDialog_ok);
            alertDialogNoButton = dialog.findViewById(R.id.alertDialog_no);

            alertDialogOkButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Biometrics biometrics = new Biometrics();
                    biometrics.biometricsPrompt(getActivity(), MainActivity.class, getActivity(),
                            R.id.container_for_fragments, new AddFragment(), "activity");
                }
            });

            alertDialogNoButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                }
            });
        }

        dialog.show();

        BiometricManager biometricManager = BiometricManager.from(getActivity().getApplicationContext());
        switch (biometricManager.canAuthenticate(BIOMETRIC_STRONG | DEVICE_CREDENTIAL)) {
            case BiometricManager.BIOMETRIC_SUCCESS:
                dialog.show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED:
                TextView dialogTittle = dialog.findViewById(R.id.alertDialog_tittle);
                dialogTittle.setText("You can use biometrics, but you don't have saved fingerprint or face. " +
                        "Please, check security settings of your phone");

                alertDialogOkButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        getActivity().finish();
                    }
                });

                dialog.show();
                break;
            case BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE:
                break;
        }

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    savePassword();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(getActivity().getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void savePassword() throws NoSuchAlgorithmException {
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PASSWORD_PREF_TAG, new ShaEncoder(password.getText().toString()).sha256EncodeInput());
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(), "password saved", Toast.LENGTH_SHORT).show();
    }
}