package com.example.egida;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class GetFromServerFragment extends Fragment {

    TextView ipText;
    TextView passwordText;

    private String ip;
    private String password;
    private String[] data;

    private static final String ARG_PARAM1 = "param1";
    private String message;

    public GetFromServerFragment() {}

    public static GetFromServerFragment newInstance(String param1) {
        GetFromServerFragment fragment = new GetFromServerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_from_server, container, false);

        ipText = view.findViewById(R.id.gotIP);
        passwordText = view.findViewById(R.id.gotPassword);

        data = message.split("\\|");

        ip = data[0];
        password = data[1];

        ipText.setText(ip);
        passwordText.setText(password);

        return view;
    }
}