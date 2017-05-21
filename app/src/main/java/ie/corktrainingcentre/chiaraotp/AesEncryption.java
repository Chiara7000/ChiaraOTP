package ie.corktrainingcentre.chiaraotp;

import android.graphics.Bitmap;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Chiara on 20/05/2017.
 */

public class AesEncryption {

    public static byte[] encrypt(String password, String clearText) throws Exception {
        byte[] clear = clearText.getBytes();
        byte[] raw = GetKey(password);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALG);

        cipher.init(Cipher.ENCRYPT_MODE, skeySpec,new IvParameterSpec(GetAESArray(Constants.CIPHER_ALG)));
        byte[] encrypted = cipher.doFinal(clear);
        return encrypted;
    }

    public static String decrypt(String password, byte[] encrypted) throws Exception {
        byte[] raw = GetKey(password);
        SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
        Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALG);
        cipher.init(Cipher.DECRYPT_MODE, skeySpec,new IvParameterSpec(GetAESArray(Constants.CIPHER_ALG)));
        byte[] decrypted = cipher.doFinal(encrypted);
        return new String(decrypted,"UTF-8");
    }

    private static byte[] GetAESArray(String cipherArrayPassword) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("MD5");
        digest.reset();
        return digest.digest(cipherArrayPassword.getBytes());
    }

    private static byte[] GetKey(String password) throws Exception{
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        digest.reset();
        return digest.digest((password+Constants.SALT).getBytes());
    }

    public static void Test() throws Exception{
        String password = UUID.randomUUID().toString();
// encrypt
        byte[] encryptedData = encrypt(password,"something");
// decrypt
        String exit = decrypt(password,encryptedData);
    }



}
