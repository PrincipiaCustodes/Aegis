package com.example.egida;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;

@RequiresApi(api = Build.VERSION_CODES.O)
public class DeleteFunction {

    public static void deleteFile(String fileName) {
        File fileInfo = new File(Check.filesInfoPath + fileName + ".json");
        File file = new File(Check.encryptedFilesPath, fileName);
        file.delete();
        fileInfo.delete();
    }
}
