package com.example.egida;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DeleteFunction {

    static Keys keys;

    public static void deleteFile(String fileName) {
            try {
                keys = new Keys();
            } catch (IOException e) {
                e.printStackTrace();
            }
            keys.remove(fileName);
        File file = new File("/data/data/com.example.egida/encrypted_files/", fileName);
        file.delete();
    }
}
