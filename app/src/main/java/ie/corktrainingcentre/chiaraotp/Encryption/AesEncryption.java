package ie.corktrainingcentre.chiaraotp.Encryption;

import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.util.UUID;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import ie.corktrainingcentre.chiaraotp.Helpers.Constants;
import ie.corktrainingcentre.chiaraotp.Activities.MainActivityOTP;
import ie.corktrainingcentre.chiaraotp.data.DBHelper;


/**
 * Created by Chiara on 20/05/2017.
 */

public class AesEncryption {

    private static final String TAG = MainActivityOTP.class.getName();

    private static String getSalt(){
        RSAManager rsa = new RSAManager();
        return rsa.Decrypt(DBHelper.readSalt());
    }

    private static String getCipherKey(){
        RSAManager rsa = new RSAManager();
        return rsa.Decrypt(DBHelper.readCipherKey());
    }

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

            cipher.init(Cipher.ENCRYPT_MODE, skeySpec, new IvParameterSpec(GetAESArray(getCipherKey())));
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
            cipher.init(Cipher.DECRYPT_MODE, skeySpec, new IvParameterSpec(GetAESArray(getCipherKey())));
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
        return digest.digest((password+getSalt()).getBytes());
    }

}
