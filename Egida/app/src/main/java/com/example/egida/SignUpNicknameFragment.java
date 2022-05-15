package com.example.egida;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class SignUpNicknameFragment extends Fragment {

    private ImageView nextButton;
    private EditText nickname;

    private SharedPreferences sharedPref;
    final private String PREF_TAG = "nickname";

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_sign_up_nickname, container, false);

        nextButton = view.findViewById(R.id.next_btn_nickname);
        nickname = view.findViewById(R.id.nickname_input_textField);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveNickname();
                Toast.makeText(getActivity().getApplicationContext(), "text: " + nickname.getText().toString(), Toast.LENGTH_SHORT).show();
                SignUpPasswordFragment signUpPasswordActivity = new SignUpPasswordFragment();
                getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.signup_fragment_container, signUpPasswordActivity).commit();
            }
        });

        return view;
    }

    private void saveNickname() {
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(PREF_TAG, nickname.getText().toString());
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(), "nickname: " + nickname.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}