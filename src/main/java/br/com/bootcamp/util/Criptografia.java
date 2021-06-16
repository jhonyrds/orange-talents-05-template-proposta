package br.com.bootcamp.util;

import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

public class Criptografia {

    private static String password = "criptografia";
    private static String salt = "A307Z8468Y855Z36W522";

    private static TextEncryptor textEncryptor = Encryptors.text(password, salt);

    public static String encrypt(String dado) {
        return textEncryptor.encrypt(dado);
    }

    public static String decrypt(String dado) {
        return textEncryptor.decrypt(dado);
    }
}
