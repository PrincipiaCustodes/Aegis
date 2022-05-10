package com.example.egida;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class LauncherFragment extends Fragment {

    Button testButton;
    TextView testText;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        testButton = view.findViewById(R.id.test_btn);
        testText = view.findViewById(R.id.test_text);

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Biometrics biometrics = new Biometrics();
                biometrics.biometricsPrompt(getActivity(), TestActivity.class);
            }
        });

        return view;
    }
}