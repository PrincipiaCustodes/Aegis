package com.example.egida;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AesEncoder {
    @RequiresApi(api = Build.VERSION_CODES.O)
    static boolean encodeFile (String filePath) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        File file = new File(filePath);
        byte[] bytes = Files.readAllBytes(file.toPath());

        String keyString = "EgidaIsTheBest606"; // TODO: add the custom string generating function

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(bytes);

        try {
            OutputStream os = new FileOutputStream(file);
            os.write(encryptedBytes);
            os.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    static boolean decodeFile (String encryptedFilePath, String decryptedFilePath) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        File decryptedFile = new File(decryptedFilePath);
        byte[] encryptedBytes = Files.readAllBytes(Paths.get(encryptedFilePath));

        String keyString = "EgidaIsTheBest606"; // TODO: add the custom string generating function

        Cipher decryptCipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
        decryptCipher.init(Cipher.DECRYPT_MODE, key);

        byte[] decryptedBytes = decryptCipher.doFinal(encryptedBytes);

        try {
            OutputStream os = new FileOutputStream(decryptedFile);
            os.write(decryptedBytes);
            os.close();
            return true;
        }
        catch (Exception e) {
            return false;
        }
    }
}
