package com.jvc.factunet.security;

import java.security.spec.KeySpec;
import java.util.Arrays;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import org.apache.commons.codec.binary.Base64;

/**
 * Esta clase implementa los metodos para encriptacion y desencriptacion
 * utilizando el algoritmo TripleDES
 *
 * @version 1.0.0.1
 */
public class TripleDES {

    // Algoritmo utilizado para la encriptacion TripleDES
    private final String ALGORITHM = "DESEde";
    // Valor para completar la clave a 3 bytes
    private final byte PADDING_VALUE = 0x00;
    // Tamaño de la clave privada
    private final byte KEY_LENGTH = 24;
    // Clave
    private final String DEFAULT_STRING_KEY = "!@#!$!@$FUNDACION_+)(**TIERRA{}:|.?";

    /**
     * Este metodo permite encriptar una clave o mensaje
     *
     * @param password dato a ser encriptado
     * @return Cadena encriptada
     * @throws Exception
     * @throws Exception si algun problema ocurre en el proceso
     */
    public String encrypt(String password) throws Exception {
// StandardPBEStringEncryptor encryptor = new StandardPBEStringEncryptor();
//        StrongTextEncryptor encryptor = new StrongTextEncryptor();
////        encryptor.setProvider(new BouncyCastleProvider());
////        encryptor.setAlgorithm(ALGORITHM);
//        encryptor.setPassword(DEFAULT_STRING_KEY);
//        return encryptor.encrypt(password);
        byte[] plainBytes = password.getBytes("UTF8");
        byte[] encryptionKey = DEFAULT_STRING_KEY.getBytes("UTF8");
        if (plainBytes == null) {
            return null;
        }
        if (encryptionKey == null) {
            throw new IllegalArgumentException("La clave no puede ser null");
        }

        byte[] cipherBytes = null;
        try {
            encryptionKey = paddingKey(encryptionKey, KEY_LENGTH, PADDING_VALUE);

            KeySpec keySpec = new DESedeKeySpec(encryptionKey);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance(ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.ENCRYPT_MODE, key);

            cipherBytes = cipher.doFinal(plainBytes);
            cipherBytes = Base64.encodeBase64(cipherBytes);
        } catch (Exception e) {
            throw new Exception("Error al desencriptar " + e.getMessage());
        }

        return new String(cipherBytes);
    }

    /**
     * Este metodo permite desencriptar una contraseña o un dato
     *
     * @param passwordEncrypted cadena encriptada
     * @return Cadena desencriptada
     * @throws Exception si algo sale mal en le procesos
     */
    public String decrypt(String passwordEncrypted) throws Exception {

        byte[] password = Base64.decodeBase64(passwordEncrypted.getBytes("UTF8"));
        byte[] encryptionKey = DEFAULT_STRING_KEY.getBytes("UTF8");
        if (password == null) {
            return null;
        }
        if (encryptionKey == null) {
            throw new IllegalArgumentException("La clave no puede ser null");
        }
        byte[] cipherBytes = null;
        try {
            encryptionKey = paddingKey(encryptionKey, KEY_LENGTH, PADDING_VALUE);

            KeySpec keySpec = new DESedeKeySpec(encryptionKey);
            SecretKeyFactory keyFactory = SecretKeyFactory
                    .getInstance(ALGORITHM);
            Cipher cipher = Cipher.getInstance(ALGORITHM);

            SecretKey key = keyFactory.generateSecret(keySpec);
            cipher.init(Cipher.DECRYPT_MODE, key);

            cipherBytes = cipher.doFinal(password);

        } catch (Exception e) {
            throw new Exception("Error al desencriptar " + e.getMessage());
        }
        return new String(cipherBytes, "UTF-8");
    }

    private byte[] paddingKey(byte[] b, int len, byte paddingValue) {

        byte[] newValue = new byte[len];
        if (b == null) {

            return newValue;
        }
        if (b.length >= len) {
            System.arraycopy(b, 0, newValue, 0, len);
            return newValue;
        }
        System.arraycopy(b, 0, newValue, 0, b.length);
        Arrays.fill(newValue, b.length, len, paddingValue);
        return newValue;
    }
}
