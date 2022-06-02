package com.example.egida;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class GetFromServerFragment extends Fragment {

    TextView ipText;
    TextView passwordText;
    TextView textFromServer;

    private String ip;
    private String password;
    private String key;
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_from_server, container, false);

        ipText = view.findViewById(R.id.gotIP);
        passwordText = view.findViewById(R.id.gotPassword);
        textFromServer = view.findViewById(R.id.text_from_server);

        data = message.split("\\|");

        ip = data[0];
        key = data[1];

        ipText.setText(ip);
        passwordText.setText(key);

        downloadFile("http://192.168.100.9:8080/image.jpg");

        return view;
    }



    private void downloadFile(String url){
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl("http://192.168.100.9:8080/");

        Retrofit retrofit = builder.build();
        RetrofitServerAPI jsonServerAPI = retrofit.create(RetrofitServerAPI.class);
        Call<ResponseBody> call = jsonServerAPI.downloadFile(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                boolean success = writeResponseBodyToDisk(response.body());

                Toast.makeText(getContext(), "Connection successful!" + success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Connection failure!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "bla2.jpg");

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