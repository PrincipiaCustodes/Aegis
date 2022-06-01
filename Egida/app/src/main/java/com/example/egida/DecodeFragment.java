package com.example.egida;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class DecodeFragment extends Fragment {

    TextView fileName;

    private static final String ARG_PARAM1 = "param1";
    private String name;

    public DecodeFragment() {}

    public static DecodeFragment newInstance(String param1) {
        DecodeFragment fragment = new DecodeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            name = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decode, container, false);

        fileName = view.findViewById(R.id.fileName);

        fileName.setText(name);

        return view;
    }
}