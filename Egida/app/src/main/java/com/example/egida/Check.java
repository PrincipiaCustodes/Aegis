package com.example.egida;

import android.annotation.SuppressLint;
import android.os.Environment;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

public class Check {

    @SuppressLint("SdCardPath")
    public static final String encryptedFilesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Aegis/EncryptedFiles/";

    @SuppressLint("SdCardPath")
    public static final String decryptedFilesPath = "/data/data/com.example.egida/decrypted_files/";

    @SuppressLint("SdCardPath")
    public static final String filesInfoPath = "/data/data/com.example.egida/files_info/";

    @SuppressLint("SdCardPath")
    public static final String receivedFilesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Aegis/ReceivedFiles/";

    @SuppressLint("SdCardPath")
    public static final String openFilesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS) + "/Aegis/OpenFiles/";

    @SuppressLint("SdCardPath")
    public static final String extractedFilesPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS) + "/Aegis/";

    public static void mainDirectories(){
        File encryptedFiles = new File(encryptedFilesPath);
        if (!encryptedFiles.exists()) {
            encryptedFiles.mkdirs();
        }

        File decryptedFiles = new File(decryptedFilesPath);
        if (!decryptedFiles.exists()) {
            decryptedFiles.mkdirs();
        }

        File filesInfo = new File(filesInfoPath);
        if (!filesInfo.exists()) {
            filesInfo.mkdirs();
        }

        File receivedFiles = new File(receivedFilesPath);
        if (!receivedFiles.exists()) {
            receivedFiles.mkdirs();
        }

        File openFiles = new File(openFilesPath);
        if (!openFiles.exists()) {
            openFiles.mkdirs();
        }

        File extractedFiles = new File(extractedFilesPath);
        if (!extractedFiles.exists()) {
            extractedFiles.mkdirs();
        }
    }

    public static void clearDecryptedFilesDir() throws IOException {
        File decryptedFiles = new File(decryptedFilesPath);
        FileUtils.cleanDirectory(decryptedFiles);
    }

    public static void clearExtractedFilesDir() throws IOException {
        File extractedFiles = new File(extractedFilesPath);
        FileUtils.cleanDirectory(extractedFiles);
    }
}
