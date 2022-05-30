package com.example.egida;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

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

        return view;
    }

    
}