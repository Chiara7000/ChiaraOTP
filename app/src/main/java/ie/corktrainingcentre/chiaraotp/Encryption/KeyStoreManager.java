package ie.corktrainingcentre.chiaraotp.Encryption;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import javax.security.auth.x500.X500Principal;

import ie.corktrainingcentre.chiaraotp.MainActivityOTP;

/**
 * Created by Chiara on 21/05/2017.
 */

public class KeyStoreManager {

    private KeyStore keyStore;
    private Context c;
    private static final String TAG = MainActivityOTP.class.getName();

    public KeyStoreManager(Context c) {
        try {
            this.c=c;
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore .load(null, null);
        }
        catch (KeyStoreException  | IOException | NoSuchAlgorithmException | CertificateException exception) {
            throw new RuntimeException("Failed to get an instance of KeyStore", exception);
        }

    }

    private ArrayList getKeys() {
        ArrayList keyAliases = new ArrayList<>();
        try {
            Enumeration<String> aliases = KeyStore.getInstance("AndroidKeyStore").aliases();
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement());
            }
        }
        catch(Exception e) {Log.e(TAG, "Failed to get an instance of KeyStore", e);}
        return keyAliases;
    }

    public void createNewKey(String key) throws Exception {

        try {
            Calendar start = Calendar.getInstance();
            Calendar end = Calendar.getInstance();
            end.add(Calendar.YEAR, 100);
            KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this.c.getApplicationContext())
                    .setAlias(key)
                    .setSubject(
                            new X500Principal(String.format("CN=%s, OU=%s", key, this.c.getApplicationContext().getPackageName())))
                    .setSerialNumber(BigInteger.ONE).setStartDate(start.getTime())
                    .setEndDate(end.getTime())
                    .build();

            KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
            kpGenerator.initialize(spec);
            kpGenerator.generateKeyPair();
            }
            catch (InvalidAlgorithmParameterException exception) {
                throw new RuntimeException("Failed to generate new key", exception);
            }
    }

    public String getPrivateKey(String key){
        try {
            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(key, null);

            if (keyEntry != null) {
                return new String(keyEntry.getPrivateKey().getEncoded(),"UTF-8");
            }
        }
        catch(Exception e){
            Log.e(TAG, "unexpected getPrivateKey exception", e);
        }
        return "";
    }

    public void test(String input){
        try {
            createNewKey("something");
            String p =getPrivateKey("something");
            Log.e("",p);
        }
        catch(Exception e){
            Log.e("",e.getMessage());
        }
    }
}
