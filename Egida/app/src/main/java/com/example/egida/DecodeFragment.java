package com.example.egida;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class DecodeFragment extends Fragment {

    RecyclerView filesList;
    TextView currentDirectory;

    private File[] filesAndFolders;

    private String selectedFile;

    private static final String ARG_PARAM1 = "param1";

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
            selectedFile = getArguments().getString(ARG_PARAM1);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decode, container, false);

        filesList = view.findViewById(R.id.decode_files_list);
        currentDirectory = view.findViewById(R.id.currentDecodeFolder);

        currentDirectory.setText(Check.decryptedFilesPath);

        filesList.setLayoutManager(new LinearLayoutManager(getContext()));
        filesList.setAdapter(new FileListAdapter(getActivity(), new File(Check.decryptedFilesPath).listFiles(),  "open"));

        try {
            Keys keys = new Keys();
            File file = new File(selectedFile);
            Aes256Encoder.decodeFile(file, keys.getDecipherKey(file.getName()));
            Aes256Encoder.decodeFile(file, Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString(), keys.getDecipherKey(file.getName()));
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }

        return view;
    }
}