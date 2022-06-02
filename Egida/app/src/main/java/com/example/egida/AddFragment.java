package com.example.egida;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

/*import com.jaiselrahman.filepicker.activity.FilePickerActivity;
import com.jaiselrahman.filepicker.config.Configurations;*/

public class AddFragment extends Fragment {

    //private static final int REQUEST_FILES = -1;
    Button addFile, button;

    @Override
    public void onCreate(Bundle savedInstanceState) { super.onCreate(savedInstanceState); }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);

        addFile = view.findViewById(R.id.drawing);
        button = view.findViewById(R.id.button);

        addFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.container_for_fragments, new DrawingFragment())
                        .addToBackStack(null)
                        .commit();*/

                filePicker1();

                /*Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("application/pdf");

                someActivityResultLauncher.launch(intent);*/
            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filePicker2();
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
                        Log.d("FilePath: ", " " + filePath);
                        Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();

                        //String filePath = result.getData().getData().getPath();
                        Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Beeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
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

    private void filePicker2(){
        Intent openFileManager = new Intent(Intent.ACTION_GET_CONTENT);
        openFileManager.setType("*/*");
        someActivityResultLauncher2.launch(openFileManager);
        Toast.makeText(getContext(), "Start file picker", Toast.LENGTH_SHORT).show();
    }


    ActivityResultLauncher<Intent> someActivityResultLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK && result.getData() != null) {
                        String filePath = getRealPathOfPdfFromUri(result.getData().getData(), getActivity());
                        Log.d("FilePath: ", " " + filePath);
                        Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();

                        //String filePath = result.getData().getData().getPath();
                        Toast.makeText(getContext(), filePath, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getContext(), "Beeeeeeeeeeeee", Toast.LENGTH_SHORT).show();
                    }
                }
            });

    public String getRealPathOfPdfFromUri(Uri uri, Activity activity){
        ContentResolver cr = activity.getContentResolver();
        uri = MediaStore.Files.getContentUri("external");

        String[] projection = null;

        String selection = MediaStore.Files.FileColumns.MEDIA_TYPE + "=" + MediaStore.Files.FileColumns.DATA;
        String[] selectionArgs = null;

        String sortOrder = null;
        Cursor allNonMediaFiles = cr.query(uri, projection, selection, selectionArgs, sortOrder);
        if(allNonMediaFiles == null){
            return uri.getPath();
        }
        else{
            allNonMediaFiles.moveToFirst();
            int id = allNonMediaFiles.getColumnIndex(MediaStore.Files.FileColumns.DATA);
            return allNonMediaFiles.getString(id);
        }
    }
}