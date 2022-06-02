package com.example.egida;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

public class DeleteFragment extends Fragment {

    RecyclerView filesList;
    TextView currentDirectory;

    private File appDirectory;
    private File[] filesAndFolders;

    private static final String ARG_PARAM1 = "param1";

    public DeleteFragment() {}

    public static DeleteFragment newInstance(String param1) {
        DeleteFragment fragment = new DeleteFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_delete, container, false);

        filesList = view.findViewById(R.id.files_list);
        currentDirectory = view.findViewById(R.id.currentFolder);

        currentDirectory.setText(getArguments().getString(ARG_PARAM1));

        filesList.setLayoutManager(new LinearLayoutManager(getContext()));
        filesList.setAdapter(new FileListAdapter(getActivity(), filesAndFolders,  "del"));

        return view;
    }
}