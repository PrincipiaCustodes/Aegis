package com.example.egida;

import android.annotation.SuppressLint;
import android.util.Log;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

public class Aes256Encoder {
    public static void encodeFile(File inFile, String keyString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");

        @SuppressLint("GetInstance")
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        File outFile = new File(Check.encryptedFilesPath + inFile.getName());

        long startTime = System.currentTimeMillis();

        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while((bytesRead = fis.read(buffer)) != -1){
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                fos.write(output);
            }
        }

        byte[] encryptedBytes = cipher.doFinal();
        if (encryptedBytes != null){
            fos.write(encryptedBytes);
        }

        fis.close();
        fos.close();


        long finishTime = System.currentTimeMillis();
        Log.i("TIME", (finishTime-startTime) + " ms.");
    }

    public static void decodeFile (File inFile, String keyString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");

        @SuppressLint("GetInstance")
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        File outFile = new File(Check.decryptedFilesPath + inFile.getName());

        long startTime = System.currentTimeMillis();

        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while((bytesRead = fis.read(buffer)) != -1){
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                fos.write(output);
            }
        }

        byte[] decryptedBytes = cipher.doFinal();
        if (decryptedBytes != null){
            fos.write(decryptedBytes);
        }

        fis.close();
        fos.close();

        long finishTime = System.currentTimeMillis();
        Log.i("TIME", (finishTime-startTime) + " ms.");
    }

    // encode to custom directory
    public static void decodeFile (File inFile, String outDirectory, String keyString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {

        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");

        @SuppressLint("GetInstance")
        Cipher cipher = Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, key);

        File outFile = new File(outDirectory + "/" + inFile.getName());

        long startTime = System.currentTimeMillis();

        FileInputStream fis = new FileInputStream(inFile);
        FileOutputStream fos = new FileOutputStream(outFile);

        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int bytesRead;
        while((bytesRead = fis.read(buffer)) != -1){
            byte[] output = cipher.update(buffer, 0, bytesRead);
            if (output != null) {
                fos.write(output);
            }
        }

        byte[] decryptedBytes = cipher.doFinal();
        if (decryptedBytes != null){
            fos.write(decryptedBytes);
        }

        fis.close();
        fos.close();

        long finishTime = System.currentTimeMillis();
        Log.i("TIME", (finishTime-startTime) + " ms.");
    }
}
