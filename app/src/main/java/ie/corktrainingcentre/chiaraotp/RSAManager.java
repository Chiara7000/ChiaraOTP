package ie.corktrainingcentre.chiaraotp;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.security.keystore.KeyProperties;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.security.auth.x500.X500Principal;

/**
 * Created by Chiara on 21/05/2017.
 */

public class RSAManager {
    private KeyStore keyStore;
    private Context c;
    private static RSAManager instance=null;
    private static Object locker=new Object();
    private RSAManager(){}

    public static RSAManager GetInstance(Context c) {
        try {
            if(instance==null)
                synchronized (locker) {
                    if (instance == null) {
                        RSAManager i= new RSAManager();
                        i.c = c;
                        i.keyStore = KeyStore.getInstance("AndroidKeyStore");
                        i.keyStore.load(null, null);
                        i.CreateNewKey();
                        instance = i;
                    }
                }
        }
        catch(Exception e)
        {

        }
        return instance;
    }

    public String Encrypt(String secret){
        String ret=null;
        try {
            // Encrypt the text
            Cipher inputCipher = Cipher.getInstance(Constants.RSA_MODE, "AndroidOpenSSL");
            inputCipher.init(Cipher.ENCRYPT_MODE, GetPublicKey());

            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            CipherOutputStream cipherOutputStream = new CipherOutputStream(outputStream, inputCipher);
            cipherOutputStream.write(secret.getBytes());
            cipherOutputStream.close();

            ret = new String(Base64.encode(outputStream.toByteArray(), Base64.DEFAULT));
        }
        catch(Exception e)
        {

        }
        return ret;
    }

    public String Decrypt(String encr) {

        String ret = null;
        try {
            byte[] encrypted = Base64.decode(encr.getBytes(), Base64.DEFAULT);

            Cipher output = Cipher.getInstance(Constants.RSA_MODE);

            output.init(Cipher.DECRYPT_MODE, GetPrivateKey());

            CipherInputStream cipherInputStream = new CipherInputStream(new ByteArrayInputStream(encrypted), output);
            ArrayList<Byte> values = new ArrayList<>();
            int nextByte;
            while ((nextByte = cipherInputStream.read()) != -1) {
                values.add((byte) nextByte);
            }

            byte[] bytes = new byte[values.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = values.get(i).byteValue();
            }
            ret = new String(bytes, "UTF-8");
        }
        catch(Exception e)
        {

        }
        return ret;
    }

    //private methods

    private void CreateNewKey(){
        try {

            if (!keyStore.containsAlias(Constants.APPKEY)) {
                // Generate a key pair for encryption
                Calendar start = Calendar.getInstance();
                Calendar end = Calendar.getInstance();
                end.add(Calendar.YEAR, 30);
                KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this.c.getApplicationContext())
                        .setAlias(Constants.APPKEY)
                        .setSubject(new X500Principal("CN=" + Constants.APP_NAME))
                        .setSerialNumber(BigInteger.TEN)
                        .setStartDate(start.getTime())
                        .setEndDate(end.getTime())
                        .build();
                KeyPairGenerator kpg = KeyPairGenerator.getInstance(KeyProperties.KEY_ALGORITHM_RSA, "AndroidKeyStore");
                kpg.initialize(spec);
                kpg.generateKeyPair();
            }
        }
        catch(Exception e)
        {
            Log.e("",e.getMessage());
        }
    }

    private PrivateKey GetPrivateKey(){
        PrivateKey k = null;
        try {
            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(Constants.APPKEY, null);

            if (keyEntry != null) {
                k=keyEntry.getPrivateKey();
            }
        }
        catch(Exception e){
            Log.e("",e.getMessage());
        }
        return k;
    }

    private PublicKey GetPublicKey(){
        PublicKey k = null;
        try {
            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(Constants.APPKEY, null);

            if (keyEntry != null) {
                k = keyEntry.getCertificate().getPublicKey();
            }
        }
        catch(Exception e){
            Log.e("",e.getMessage());
        }
        return k;
    }
}