package com.example.egida;

import android.net.Uri;
import android.os.Build;
import android.os.FileUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AesEncoder {
    public static String keyString;

    static void writeBytesToFile(String fileOutput, byte[] bytes) throws IOException {
        FileOutputStream out = new FileOutputStream(fileOutput);
        out.write(bytes);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static void encodeFile (String decryptedFilePath, String encryptedFilePath) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        File decryptedFile = new File(decryptedFilePath);
        byte[] tmp_bytes = Files.readAllBytes(decryptedFile.toPath());

        Keys keys = new Keys();

        //String keyString = "fd7c32e39427549e"; // TODO: add DrawingFragment call to return the key string

        keys.setValues(Keys.getFileNameWithoutExtension(decryptedFile),
                keyString,
                decryptedFile.getName().substring(Keys.getFileNameWithoutExtension(decryptedFile).length() + 1,
                decryptedFile.getName().length()));

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(tmp_bytes);

        Log.i("bruh", Arrays.toString(encryptedBytes));

        FileOutputStream out = new FileOutputStream(encryptedFilePath);
        out.write(encryptedBytes);
        out.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static void decodeFile (String encryptedFilePath, String decryptedFilePath) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        File decryptedFile = new File(decryptedFilePath);
        File encryptedFile = new File(encryptedFilePath);
        byte[] encryptedBytes = Files.readAllBytes(Paths.get(encryptedFilePath));

        Keys keys = new Keys();
        String keyString = keys.getDecipherKey(encryptedFile.getName());

        Cipher decryptCipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);

        writeBytesToFile(decryptedFilePath, decryptedBytes);
    }
}
