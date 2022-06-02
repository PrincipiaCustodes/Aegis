package com.example.egida;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.File;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class ShareFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";

    RecyclerView filesList;
    TextView currentDirectory;
    Button openAppDir;

    private File appDirectory;
    private File[] filesAndFolders;

    public ShareFragment(){}

    public static ShareFragment newInstance(String param1) {
        ShareFragment fragment = new ShareFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        filesList = view.findViewById(R.id.files_list);
        currentDirectory = view.findViewById(R.id.currentFolder);
        openAppDir = view.findViewById(R.id.openAppDir);

        currentDirectory.setText(getArguments().getString(ARG_PARAM1));

        filesList.setLayoutManager(new LinearLayoutManager(getContext()));
        filesList.setAdapter(new FileListAdapter(getActivity(), filesAndFolders, "share"));

        return view;
    }
}