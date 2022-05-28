package com.example.egida;

import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.os.StrictMode;
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
import java.util.concurrent.atomic.AtomicBoolean;

public class ShareFragment extends Fragment {

    Button genButton;
    ImageView qrCode;
    Switch serverState;
    EditText edTxt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        Server server = new Server(8080, "/storage/emulated/Test");
        serverState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Thread thread = new Thread(server);
                if(isChecked){

                    thread.start();
                } else {
                    server.stop1();
                }
            }
        });

        return view;
    }

    private void createQrCode(){
        MultiFormatWriter multiFormatWriter = new MultiFormatWriter();
        try {
            BitMatrix bitMatrix = multiFormatWriter.encode("https://github.com/journeyapps/zxing-android-embedded",
                    BarcodeFormat.QR_CODE, 350, 350);

            BarcodeEncoder barcodeEncoder = new BarcodeEncoder();
            Bitmap bitmap = barcodeEncoder.createBitmap(bitMatrix);
            qrCode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            e.printStackTrace();
        }
    }

    public class Server implements Runnable {
        private boolean exit;

        private final int port;
        private final String directory;

        public Server(int port, String directory) {
            this.port = port;
            this.directory = directory;
        }

        @Override
        public void run() {
            while(!exit) {
                try (ServerSocket server = new ServerSocket(this.port)) {
                    while (true) {
                        Socket socket = server.accept();
                        Thread thread = new Thread(new MyHandler(socket, this.directory));
                        thread.start();
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void stop1()
        {
            exit = true;
        }
    }
}