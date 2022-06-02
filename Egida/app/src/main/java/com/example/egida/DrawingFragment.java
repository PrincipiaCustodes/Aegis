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

        confirm.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    AesEncoder.keyString = DrawingView.getKey();
                    Toast.makeText(getContext(), "/sdcard" + decryptedFilePath.substring(19, decryptedFilePath.length()), Toast.LENGTH_SHORT).show();
                    File decryptedFile = new File("/sdcard" + decryptedFilePath.substring(19, decryptedFilePath.length()));
                    AesEncoder.encodeFile("/sdcard" + decryptedFilePath.substring(19, decryptedFilePath.length()),
                            "/data/data/com.example.egida/encrypted_files/" + Keys.getFileNameWithoutExtension(decryptedFile));
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (NoSuchPaddingException e) {
                    e.printStackTrace();
                } catch (NoSuchAlgorithmException e) {
                    e.printStackTrace();
                } catch (IllegalBlockSizeException e) {
                    e.printStackTrace();
                } catch (BadPaddingException e) {
                    e.printStackTrace();
                } catch (InvalidKeyException e) {
                    e.printStackTrace();
                }
            }
        });

        return view;
    }
}
