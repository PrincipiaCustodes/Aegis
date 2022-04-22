package com.example.egida;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class AesEncoder {
    @RequiresApi(api = Build.VERSION_CODES.O)
    byte[] encodeFile (String filePath) throws IOException, NoSuchPaddingException, NoSuchAlgorithmException, IllegalBlockSizeException, BadPaddingException, InvalidKeyException {
        File file = new File(filePath);
        byte[] bytes = Files.readAllBytes(file.toPath());

        String keyString = "EgidaIsTheBest606"; // TODO: add the custom string generating function

        Cipher cipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, key);

        byte[] encryptedBytes = cipher.doFinal(bytes);
        return encryptedBytes;
    }

    byte[] decodeBytes (byte[] encryptedBytes) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        String keyString = "EgidaIsTheBest606"; // TODO: add the custom string generating function

        Cipher decryprCipher = Cipher.getInstance("AES");
        SecretKeySpec key = new SecretKeySpec(keyString.getBytes(), "AES");
        decryprCipher.init(Cipher.DECRYPT_MODE, key);
        byte[] decryptedBytes = decryprCipher.doFinal(encryptedBytes);

        return decryptedBytes;
    }
}
