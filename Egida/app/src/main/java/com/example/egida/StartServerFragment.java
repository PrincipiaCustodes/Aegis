package com.example.egida;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

public class StartServerFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private String mParam1;

    Button genButton;
    ImageView qrCode;

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch serverState;

    private String fileName;
    private String extension;


    public StartServerFragment() {}

    public static StartServerFragment newInstance(String param1) {
        StartServerFragment fragment = new StartServerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            if(getArguments().getString(ARG_PARAM1).lastIndexOf("/") == -1){
                fileName = "";
            } else {
                fileName = getArguments().getString(ARG_PARAM1).substring(getArguments().getString(ARG_PARAM1).lastIndexOf("/") + 1);
            }

            if(getArguments().getString(ARG_PARAM1).lastIndexOf(".") == -1){
                extension = "";
            } else {
                extension = getArguments().getString(ARG_PARAM1).substring(getArguments().getString(ARG_PARAM1).lastIndexOf(".") + 1);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_start_server, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        genButton = view.findViewById(R.id.gen_qr_code_btn);
        qrCode = view.findViewById(R.id.qr_code);
        serverState = view.findViewById(R.id.start_server_btn);

        genButton.setVisibility(View.GONE);
        genButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), Keys.getDecipherKey(fileName), Toast.LENGTH_SHORT).show();
                createQrCode();
            }
        });


        Server server = new Server("/data/data/com.example.egida/encrypted_files/");
        serverState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Thread thread = new Thread(server);
                if(isChecked){
                    thread.start();
                    genButton.setVisibility(View.VISIBLE);
                } else {
                    server.stopServer();
                }
            }
        });

        return view;
    }

    private void createQrCode(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode(createQrCodeMessage(), BarcodeFormat.QR_CODE, 350, 350);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    private String createQrCodeMessage(){
        StringBuffer qrCodeMessage = new StringBuffer();
        qrCodeMessage.append("http://")
                .append(getIP())
                .append(":")
                .append(Server.getPort())
                .append("|")
                .append(fileName)
                .append("|")
                .append(Keys.getDecipherKey(fileName));

        return qrCodeMessage.toString();
    }

    private String getIP(){
        //WifiManager wifiManager = ;
        return Formatter.formatIpAddress(((WifiManager)getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
    }


    private String getKey(){
        // TODO: Миша, нужно передать сюда ключ для расшифровки
        String key = "UKYQ74fBMd+nq9SyUUBrCw==";
        return key;
    }
}