package com.example.egida;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.security.NoSuchAlgorithmException;

public class LauncherFragment extends Fragment {

    Button testButton;
    TextView testText;

    private SharedPreferences sharedPref;
    private static final String NICKNAME_PREF_TAG = "nickname";
    private static final String SECURITY_STATUS_PREF_TAG = "security_status";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        testButton = view.findViewById(R.id.test_btn);
        testText = view.findViewById(R.id.test_text);

        testText.setText(getNickname());

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(getSecurityStatus().equals("use biometrics")){
                    Biometrics biometrics = new Biometrics() {
                        @Override
                        public void nextAction() {
                            testText.setBackgroundColor(getActivity().getResources().getColor(R.color.my_red));
                        }
                    };
                    biometrics.biometricsPrompt(getContext());
                } else {
                    Password password = new Password(getContext()) {
                        @Override
                        public void nextAction() {
                            testText.setBackgroundColor(getActivity().getResources().getColor(R.color.teal_700));
                        }
                    };
                }
            }
        });

        return view;
    }

    private String getNickname(){
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        return sharedPref.getString(NICKNAME_PREF_TAG, "error");
    }

    private String getSecurityStatus(){
        SharedPreferences sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        return sharedPref.getString(SECURITY_STATUS_PREF_TAG, "security_status_error");
    }
}