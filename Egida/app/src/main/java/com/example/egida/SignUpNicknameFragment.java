package com.example.egida;

import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class SignUpNicknameFragment extends Fragment {

    private ImageView nextButton;
    private EditText nickname;

    private SharedPreferences sharedPref;
    private static final String NICKNAME_PREF_TAG = "nickname";

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sign_up_nickname, container, false);

        nextButton = view.findViewById(R.id.next_btn_nickname);
        nickname = view.findViewById(R.id.nickname_input_textField);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!nickname.getText().toString().equals("")) {
                    saveNickname();
                    SignUpPasswordFragment signUpPasswordActivity = new SignUpPasswordFragment();
                    getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.signup_fragment_container, signUpPasswordActivity).commit();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "Enter the nickname!", Toast.LENGTH_SHORT)
                            .show();
                }
            }
        });

        return view;
    }

    private void saveNickname() {
        sharedPref = getActivity().getSharedPreferences("PreferencesFile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(NICKNAME_PREF_TAG, nickname.getText().toString());
        editor.commit();
        Toast.makeText(getActivity().getApplicationContext(), "Nickname: " + nickname.getText().toString(), Toast.LENGTH_SHORT).show();
    }
}