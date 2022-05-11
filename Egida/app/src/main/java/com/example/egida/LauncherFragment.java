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
                biometrics.checkConditionals(getActivity().getApplicationContext(), getActivity());
                biometrics.biometricsPrompt(getActivity(), TestActivity.class, getActivity(),
                        R.id.container_for_fragments, new AddFragment(), "activity");
            }
        });

        return view;
    }
}