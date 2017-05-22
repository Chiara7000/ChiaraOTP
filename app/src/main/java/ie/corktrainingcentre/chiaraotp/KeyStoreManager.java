package ie.corktrainingcentre.chiaraotp;

import android.content.Context;
import android.security.KeyPairGeneratorSpec;
import android.util.Log;

import java.io.FileOutputStream;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;

import javax.security.auth.x500.X500Principal;

/**
 * Created by Ciro on 21/05/2017.
 */

public class KeyStoreManager {

    private KeyStore keyStore;
    private Context c;

    public KeyStoreManager(Context c) {
        try {
            this.c=c;
            keyStore = KeyStore.getInstance("AndroidKeyStore");
            keyStore .load(null, null);
        }
        catch(Exception e)
        {

        }
    }

    private ArrayList GetKeys() {
        ArrayList keyAliases = new ArrayList<>();
        try {
            Enumeration<String> aliases = KeyStore.getInstance("AndroidKeyStore").aliases();
            while (aliases.hasMoreElements()) {
                keyAliases.add(aliases.nextElement());
            }
        }
        catch(Exception e) {}
        return keyAliases;
    }

    public void createNewKey(String key) throws Exception {

        Calendar start = Calendar.getInstance();
        Calendar end = Calendar.getInstance();
        end.add(Calendar.YEAR, 100);
        KeyPairGeneratorSpec spec = new KeyPairGeneratorSpec.Builder(this.c.getApplicationContext())
                .setAlias(key)
                .setSubject(
                        new X500Principal(String.format("CN=%s, OU=%s", key,this.c.getApplicationContext().getPackageName())))
                .setSerialNumber(BigInteger.ONE).setStartDate(start.getTime())
                .setEndDate(end.getTime())
                .build();

        KeyPairGenerator kpGenerator = KeyPairGenerator.getInstance("RSA", "AndroidKeyStore");
        kpGenerator.initialize(spec);
        kpGenerator.generateKeyPair();
    }

    public String GetPrivateKey(String key){
        try {
            KeyStore.PrivateKeyEntry keyEntry = (KeyStore.PrivateKeyEntry) keyStore.getEntry(key, null);

            if (keyEntry != null) {
                return new String(keyEntry.getPrivateKey().getEncoded(),"UTF-8");
            }
        }
        catch(Exception e){
            Log.e("",e.getMessage());
        }
        return "";
    }

    public void test(String input){
        try {
            createNewKey("something");
            String p =GetPrivateKey("something");
            Log.e("",p);
        }
        catch(Exception e){
            Log.e("",e.getMessage());
        }
    }
}
