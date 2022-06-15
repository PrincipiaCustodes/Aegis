package com.example.egida;


import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;


import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DrawingFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    Button confirm;
    private String decryptedFilePath;

    public static DrawingFragment newInstance(String param1) {
        DrawingFragment fragment = new DrawingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            decryptedFilePath = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_drawing, container, false);

        confirm = view.findViewById(R.id.confirm_btn);

        confirm.setText(decryptedFilePath);

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                SharedPrefs.setFILE_ID(getContext(), SharedPrefs.getFILE_ID(getContext()) + 1);

                try {
                    Toast.makeText(getContext(), "/sdcard" + decryptedFilePath.substring(19, decryptedFilePath.length()), Toast.LENGTH_SHORT).show();
                    File clearFile = new File("/sdcard" + decryptedFilePath.substring(19, decryptedFilePath.length()));

                    Keys keys = new Keys();
                    keys.setValues(clearFile.getName(), DrawingView.getKey());

                    Aes256Encoder.encodeFile(clearFile, DrawingView.getKey());

                    FilesInfo filesInfo = new FilesInfo();
                    FilesInfo.File inFileInfo = new FilesInfo.File();
                    inFileInfo.setId(SharedPrefs.getFILE_ID(getContext()));
                    inFileInfo.setName(clearFile.getName());
                    inFileInfo.setExtension(clearFile.getName().split("\\.")[1]);
                    inFileInfo.setSize(clearFile.length());
                    inFileInfo.setKey(DrawingView.getKey());
                    inFileInfo.setDate(String.valueOf(System.currentTimeMillis()));
                    filesInfo.toJson(inFileInfo);

                } catch (IOException | NoSuchPaddingException | NoSuchAlgorithmException | IllegalBlockSizeException | BadPaddingException | InvalidKeyException e) {
                    e.printStackTrace();
                }

                ((FragmentActivity)getContext()).getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.container_for_fragments, new AddFragment())
                        .commit();
            }
        });

        return view;
    }
}
