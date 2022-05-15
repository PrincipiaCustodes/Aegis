package com.example.egida;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ShaEncoder {
    private String input;

    public ShaEncoder(String input){
        this.input = input;
    }

    public String sha256EncodeInput() throws NoSuchAlgorithmException {
        MessageDigest messageDigest;
        StringBuffer output = new StringBuffer();

        messageDigest = MessageDigest.getInstance("SHA-256");
        messageDigest.update(input.getBytes());
        byte[] digest = messageDigest.digest();
        for(byte b : digest){
            output.append(String.format("%02x", b & 0xff));
        }

        return output.toString();
    }
}
