package com.example.egida;

import android.app.Dialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class AddFragment extends Fragment {

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);

        {
            CustomAlertDialog customAlertDialog = new CustomAlertDialog(getActivity()) {
                @Override
                public void positiveAction() {
                    getDialog().dismiss();
                }

                @Override
                public void negativeAction() {
                    getDialog().dismiss();
                }
            };
            customAlertDialog.setAlertDialogImageId(R.drawable.icon);
            customAlertDialog.setNewAlertDialogTittle("Test dialog");
            customAlertDialog.setNewAlertDialogDescription("Some description");
            customAlertDialog.setNewAlertDialogQuestion("Are you ready?");
            customAlertDialog.setNewAlertDialogOkButton("Yep");
            customAlertDialog.setNewAlertDialogNoButton("Nope");
            customAlertDialog.setupAlertDialogSettings();
            customAlertDialog.getDialog().show();
        }

        return view;
    }
}