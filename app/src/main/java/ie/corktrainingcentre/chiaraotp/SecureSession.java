package ie.corktrainingcentre.chiaraotp;

import java.io.ObjectStreamException;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * Created by Chiara on 20/05/2017.
 */

public class SecureSession {

    private static SecureSession instance = null;
    private static Object locker = new Object();

    private Map<String,byte[]> keys;
    private String localPassword;

    private SecureSession(){
        keys = new HashMap<String,byte[]>();
        localPassword = UUID.randomUUID().toString();
    }

    public static SecureSession GetInstance()
    {
        if(instance == null)
            synchronized (locker) {
                if (instance == null) {
                    instance = new SecureSession();
                }
            }
        return instance;
    }

    public String GetValue(String key){

        if(keys.containsKey(key))
            try {
            //    return AesEncryption.Decrypt(localPassword, keys.get(key));
            }
            catch(Exception ex)
            {}

        return "";
    }

    public void SetValue(String key, String value){

        byte[] val = new byte[0];

        try {
           // val = AesEncryption.Encrypt(localPassword,value);
        }
        catch(Exception ex)
        {
        }

        keys.put(key,val);
    }
}
