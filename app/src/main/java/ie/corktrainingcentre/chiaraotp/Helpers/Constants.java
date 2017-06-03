package ie.corktrainingcentre.chiaraotp.Helpers;

/**
 * Created by Chiara on 21/05/2017.
 */
public class Constants {
    public final static int DATABASE_VERSION = 2;
    public final static String DATABASE_NAME = "otp.db";

    public final static String CIPHER_ALG = "AES/CBC/PKCS5Padding";
    public final static String APP_NAME = "ChiaraOTP";

    public final static String DATABASE_KEY_NAME = "AesDbKey";
    public final static String SALTKEY = "SaltDbKey";
    public final static String CIPHERKEY = "CipherDbKey";
    //RSA
    public final static String APPKEY = "ChiaraOTPKey1";  //name of the key in the secure storage
    public final static String RSA_MODE =  "RSA/ECB/PKCS1Padding";

    public final static boolean DEBUG = false;
    public final static boolean BYPASS_CAMERA = false;

}
