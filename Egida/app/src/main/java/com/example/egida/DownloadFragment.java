package com.example.egida;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

public class DownloadFragment extends Fragment {final int REQUEST_CODE = 11;

    Button startScan;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_download, container, false);

        startScan = view.findViewById(R.id.startScan);

        startScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                QrCodeScannerFragment qrCodeScannerFragment = new QrCodeScannerFragment();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_for_fragments, qrCodeScannerFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }
}