package ie.corktrainingcentre.chiaraotp;

import android.graphics.Bitmap;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
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

    private static final String TAG = MainActivityOTP.class.getName();

    public static void Init()
    {

    }


    public static String Encrypt(String password, String clearText){
        String ret = null;
        try {
            byte[] clear = clearText.getBytes();
            byte[] raw = GetKey(password);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALG);

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(GetAESArray(Constants.CIPHER_ALG)));
            byte[] encrypted = cipher.doFinal(clear);
            ret = new String(Base64.encode(encrypted,Base64.DEFAULT));
        }
        catch(Exception ex)
        {
            Log.e(TAG,"Error while encrypting: ", ex);
        }
        return ret;
    }

    public static String Decrypt(String password, String encryptedText){
        String ret = null;
        try {
            byte[] encrypted = Base64.decode(encryptedText.getBytes(),Base64.DEFAULT);
            byte[] raw = GetKey(password);
            SecretKeySpec skeySpec = new SecretKeySpec(raw, "AES");
            Cipher cipher = Cipher.getInstance(Constants.CIPHER_ALG);
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(GetAESArray(Constants.CIPHER_ALG)));
            byte[] decrypted = cipher.doFinal(encrypted);
            ret = new String(decrypted);
        }
        catch(Exception ex)
        {
            Log.e(TAG,"Error while decrypting: ", ex);
        }
        return ret;
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
        String encryptedData = Encrypt(password,"something");
// decrypt
        String exit = Decrypt(password,encryptedData);
    }
}
