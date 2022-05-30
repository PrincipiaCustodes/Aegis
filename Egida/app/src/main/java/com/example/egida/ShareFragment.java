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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;

public class ShareFragment extends Fragment {

    Button genButton;
    ImageView qrCode;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    Switch serverState;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_share, container, false);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        genButton = view.findViewById(R.id.gen_qr_code_btn);
        qrCode = view.findViewById(R.id.qr_code);
        serverState = view.findViewById(R.id.switch1);

        genButton.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                createQrCode();
            }
        });

        Server server = new Server("/data/data/com.example.egida/encrypted_files");
        serverState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Thread thread = new Thread(server);
                if(isChecked){
                    thread.start();
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
                .append(genSecretCode())
                .append("|")
                .append(getKey());

        return qrCodeMessage.toString();
    }

    private String getIP(){
        //WifiManager wifiManager = ;
        return Formatter.formatIpAddress(((WifiManager)getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress());
    }

    private String genSecretCode(){
        String secretCode = null;
        try {
            // каждый раз генерируется разный код, т.к. меняются входные данные для хеширования (время, возможно ip сети)
            secretCode = new ShaEncoder((Calendar.getInstance().getTime() + getIP())).sha256EncodeInput();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return secretCode;
    }

    private String getKey(){
        // TODO: Миша, нужно передать сюда ключ для расшифровки
        String key = "UKYQ74fBMd+nq9SyUUBrCw==";
        return key;
    }
}