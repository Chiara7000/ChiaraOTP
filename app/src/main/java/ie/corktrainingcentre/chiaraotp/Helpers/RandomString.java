package ie.corktrainingcentre.chiaraotp.Helpers;

import java.util.Random;

/**
 * Created by Ciro on 24/05/2017.
 */

public class RandomString {

    static final String validChars = "QWERTYUIOPLKJHGFDSAZXCVBNMqwertyuioplkjhgfdsazxcvbnm0123456789!_òç@#°à'ù§+*èé'£$%&/()=?^ì\\|<>,;.:-";
    static final String numbers = "0123456789";

    public static String RandomString(int length)
    {
        Random r= new Random();

        if(length<6) length = 6;
        if(length>100) length = 100;

        String ret="";
        for(int i=0; i<length; i++)
        {
            ret += validChars.charAt(r.nextInt(validChars.length()));
        }
        return ret;
    }

    public static String RandomStringOnlyNumbers(int length)
    {
        Random r= new Random();
        if(length<6) length = 6;
        if(length>100) length = 100;

        String ret="";
        for(int i=0; i<length; i++)
        {
            ret += numbers.charAt(r.nextInt(numbers.length()));
        }
        return ret;
    }
}
