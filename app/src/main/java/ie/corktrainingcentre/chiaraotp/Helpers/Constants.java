package ie.corktrainingcentre.chiaraotp.Helpers;

/**
 * Created by Chiara on 21/05/2017.
 */
public class Constants {
    public final static String SALT = "e.szV6x%bh`Xq8?a";
    public final static String CIPHER_SALT = "[43s$5DM'W5d+9q#";

    public final static String CIPHER_ALG = "AES/CBC/PKCS5Padding";
    public final static String APP_NAME = "ChiaraOTP";
    public final static String DATABASE_KEY_NAME = "ChiaraOTPKey";
    //RSA
    public final static String APPKEY = "ChiaraOTPKey1";  //name of the key in the secure storage
    public final static String RSA_MODE =  "RSA/ECB/PKCS1Padding";

    public final static boolean DEBUG = true;

    public final static boolean BYPASS_CAMERA = false;
}