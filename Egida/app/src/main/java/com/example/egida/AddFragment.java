package com.example.egida;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class AddFragment extends Fragment {

    //private static final int REQUEST_FILES = -1;
    Button addFile;
    public static String fileAbsolutePath;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        addFile = view.findViewById(R.id.drawing);

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePicker1();
            }
        });

        return view;
    }

    private void filePicker1(){
        Intent openFileManager = new Intent(Intent.ACTION_PICK);
        openFileManager.setType("*/*");
        someActivityResultLauncher1.launch(openFileManager);
        Toast.makeText(getContext(), "Start file picker", Toast.LENGTH_SHORT).show();
    }

    ActivityResultLauncher<Intent> someActivityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String filePath = getRealPathOfImageFromUri(result.getData().getData(), getActivity());
                        fileAbsolutePath = filePath;
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.container_for_fragments, DrawingFragment.newInstance(fileAbsolutePath))
                                .addToBackStack(null)
                                .commit();
                        Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Error!", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    public String getRealPathOfImageFromUri(Uri uri, Activity activity){
        Cursor cursor = activity.getContentResolver().query(uri,null,null,null,null);
        if(cursor == null){
            return uri.getPath();
        }
        else{
            cursor.moveToFirst();
            int id = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(id);
        }
    }
}