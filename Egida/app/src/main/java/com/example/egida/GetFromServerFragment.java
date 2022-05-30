package com.example.egida;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.text.format.Formatter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class GetFromServerFragment extends Fragment {

    TextView ipText;
    TextView passwordText;
    TextView textFromServer;

    private String ip;
    private String password;
    private String[] data;

    private static final String ARG_PARAM1 = "param1";
    private String message;

    public GetFromServerFragment() {}

    public static GetFromServerFragment newInstance(String param1) {
        GetFromServerFragment fragment = new GetFromServerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            message = getArguments().getString(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_from_server, container, false);

        ipText = view.findViewById(R.id.gotIP);
        passwordText = view.findViewById(R.id.gotPassword);
        textFromServer = view.findViewById(R.id.text_from_server);

        data = message.split("\\|");

        ip = data[0];
        password = data[1];

        ipText.setText(ip);
        passwordText.setText(password);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        JsonServerAPI jsonServerAPI = retrofit.create(JsonServerAPI.class);

        Call<List<JsonFile>> call = jsonServerAPI.getPosts();

        call.enqueue(new Callback<List<JsonFile>>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(Call<List<JsonFile>> call, Response<List<JsonFile>> response) {
                if(!response.isSuccessful()){
                    textFromServer.setText("Code: " + response.code());
                    return;
                }

                List<JsonFile> file = response.body();

                String text = file.get(0).getText();
                textFromServer.setText("Text::: " + text);
            }

            @Override
            public void onFailure(Call<List<JsonFile>> call, Throwable t) {
                textFromServer.setText(t.getMessage());
            }
        });

        downloadFile();

        return view;
    }

    private void downloadFile(){
        Retrofit.Builder builder = new Retrofit.Builder()
                //.baseUrl(Formatter.formatIpAddress(((WifiManager)getContext().getApplicationContext().getSystemService(Context.WIFI_SERVICE)).getConnectionInfo().getIpAddress()));
                .baseUrl("https://futurestud.io/");

        Retrofit retrofit = builder.build();

        JsonServerAPI jsonServerAPI = retrofit.create(JsonServerAPI.class);

        Call<ResponseBody> call = jsonServerAPI.downloadFile();

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean success = writeResponseBodyToDisk(response.body());

                Toast.makeText(getContext(), "Yeeeees!" + success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "fuck!", Toast.LENGTH_SHORT).show();
            }
        });
    }
    // File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "hahah.json");
    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "file1.png");

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);

                    if (read == -1) {
                        break;
                    }

                    outputStream.write(fileReader, 0, read);

                    fileSizeDownloaded += read;
                }

                outputStream.flush();

                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }
}