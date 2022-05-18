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

        /*
        * Сейчас массив digest - это массив байтов, чтобы получить из них шестнадцатиричный хеш,
        * мы должны каждый байт преобразовывать в шестнадцатиричный формат и в строку, чтобы "склеить" один большой хеш.
        *
        * Форматирование.
        * "%02x". '%' - флаг указывает, как будет форматироваться строка; '0' - дополнить нулями, если нужно
        * '2' - ширина строки результата; 'x' - 16-ти ричный формат
        *
        * "b & 0xff". 8-разрядный byte, который подписан в Java, расширяется до 32-битного int,
        * чтобы эффективно отменить это расширение знака, можно скрыть byte с помощью 0xff.
        */

        for(byte b : digest){
            output.append(String.format("%02x", b & 0xff));
        }

        // при выводе StringBuffer нужно преобразовать в просто String т.к. таким образом будет проще сравнивать хеши
        return output.toString();
    }
}
