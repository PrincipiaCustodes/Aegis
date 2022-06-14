package com.example.egida;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

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
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

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
    private String file;
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_get_from_server, container, false);

        ipText = view.findViewById(R.id.gotIP);
        passwordText = view.findViewById(R.id.gotPassword);
        textFromServer = view.findViewById(R.id.text_from_server);

        data = message.split("\\|");

        ip = data[0];
        file = data[1];
        key = data[2];

        ipText.setText(ip);
        passwordText.setText(file);
        textFromServer.setText(key);

        downloadFile(ip + "/" + file);
        receiveFile();

        return view;
    }

    private void downloadFile(String url){
        Retrofit.Builder builder = new Retrofit.Builder().baseUrl(ip + "/");

        Retrofit retrofit = builder.build();
        RetrofitServerAPI jsonServerAPI = retrofit.create(RetrofitServerAPI.class);
        Call<ResponseBody> call = jsonServerAPI.downloadFile(url);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                assert response.body() != null;
                boolean success = writeResponseBodyToDisk(response.body());

                Toast.makeText(getContext(), "Connection successful!" + success, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Connection failure!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void receiveFile(){
        try {
            //AesEncoder.decodeFile("/data/data/com.example.egida/shared_files/" + file, "/data/data/com.example.egida/shared_files/" + file, key);
            Aes256Encoder.decodeFile(new File(Check.receivedFilesPath + file), Check.receivedFilesPath + file, key);
        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException | IllegalBlockSizeException | BadPaddingException | IOException e) {
            e.printStackTrace();
        }

        ((FragmentActivity)getContext()).getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container_for_fragments, DrawingFragment.newInstance(Check.receivedFilesPath))
                .addToBackStack(null)
                .commit();
    }

    private boolean writeResponseBodyToDisk(ResponseBody body) {
        try {
            //File futureStudioIconFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), file);
            File fileIn = new File(Check.receivedFilesPath, file);

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];

                long fileSize = body.contentLength();
                long fileSizeDownloaded = 0;

                inputStream = body.byteStream();
                outputStream = new FileOutputStream(fileIn);

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