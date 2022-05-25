package com.example.egida;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class LauncherFragment extends Fragment {

    Button testButton;
    TextView testText;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);
        testButton = view.findViewById(R.id.test_btn);
        testText = view.findViewById(R.id.test_text);

        testText.setText(SharedPrefs.getNICKNAME(getContext()));

        testButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(SharedPrefs.getBIOMETRICS_STATUS(getContext()).equals(getString(R.string.biometrics_status_use))){
                    Biometrics biometrics = new Biometrics() {
                        @Override
                        public void nextAction() {
                            testText.setBackgroundColor(getActivity().getResources().getColor(R.color.my_red));
                        }
                    };
                    biometrics.biometricsPrompt(getContext());
                } else {
                    new Password(getContext()) {
                        @Override
                        public void passwordCorrectAction() {
                            testText.setBackgroundColor(getActivity().getResources().getColor(R.color.teal_700));
                        }
                    };
                }
            }
        });

        return view;
    }
}