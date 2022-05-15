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
    final private String PREF_TAG = "nickname";

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
                Biometrics biometrics = new Biometrics();
                biometrics.checkConditionals(getActivity().getApplicationContext(), getActivity());
                biometrics.biometricsPrompt(getActivity(), TestActivity.class, getActivity(),
                        R.id.container_for_fragments, new AddFragment(), "activity");
            }
        });

        return view;
    }

    private String getNickname(){
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        return sharedPref.getString(PREF_TAG, "error");
    }
}