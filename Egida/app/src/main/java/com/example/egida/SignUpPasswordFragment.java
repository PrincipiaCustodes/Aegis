package com.example.egida;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.security.NoSuchAlgorithmException;

public class SignUpPasswordFragment extends Fragment {

    private ImageView nextButton;
    private EditText password;

    private SharedPreferences sharedPref;
    final private String PREF_TAG = "password";

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_password, container, false);

        nextButton = view.findViewById(R.id.next_btn);
        password = view.findViewById(R.id.password_input_textField);

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
        editor.putString(PREF_TAG, new ShaEncoder(password.getText().toString()).sha256EncodeInput());
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(), "password saved", Toast.LENGTH_SHORT).show();
    }
}