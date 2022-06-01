package com.example.egida;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.File;


public class LauncherFragment extends Fragment {

    RecyclerView filesList;
    TextView currentDirectory;
    Button openAppDir;

    private File appDirectory;
    private File[] filesAndFolders;

    private static final String ARG_PARAM1 = "param1";

    public static LauncherFragment newInstance(String param1) {
        LauncherFragment fragment = new LauncherFragment();
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
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);

        filesList = view.findViewById(R.id.files_list);
        currentDirectory = view.findViewById(R.id.currentFolder);
        openAppDir = view.findViewById(R.id.openAppDir);
        
        currentDirectory.setText(getArguments().getString(ARG_PARAM1));

        filesList.setLayoutManager(new LinearLayoutManager(getContext()));
        filesList.setAdapter(new FileListAdapter(getActivity(), filesAndFolders));

        return view;
    }

    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        return result == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermission(){
        if(ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)){
            Toast.makeText(getActivity(), "Storage permission is requires,please allow from settings", Toast.LENGTH_SHORT).show();
        }else
            ActivityCompat.requestPermissions(getActivity(), new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE},111);
    }
}