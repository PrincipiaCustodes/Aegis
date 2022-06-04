package com.example.egida;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
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

    private File appDirectory;
    private File[] filesAndFolders;

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
            appDirectory = new File(getArguments().getString(ARG_PARAM1));
            filesAndFolders = appDirectory.listFiles();
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_decode, container, false);

        filesList = view.findViewById(R.id.files_list);
        currentDirectory = view.findViewById(R.id.currentFolder);

        currentDirectory.setText(getArguments().getString(ARG_PARAM1));

        filesList.setLayoutManager(new LinearLayoutManager(getContext()));
        filesList.setAdapter(new FileListAdapter(getActivity(), filesAndFolders,  "open"));

        try {
            AesEncoder.decodeFile(filesAndFolders[0].getName(), Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString());
        } catch (NoSuchPaddingException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (InvalidKeyException e) {
            e.printStackTrace();
        } catch (IllegalBlockSizeException e) {
            e.printStackTrace();
        } catch (BadPaddingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }
}