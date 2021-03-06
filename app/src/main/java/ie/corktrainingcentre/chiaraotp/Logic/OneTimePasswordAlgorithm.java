package ie.corktrainingcentre.chiaraotp.Logic;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import ie.corktrainingcentre.chiaraotp.Interfaces.ICalendar;

/**
 * Created by Chiara on 28/05/2017.
 */

public class OneTimePasswordAlgorithm {

    private ICalendar calendar = null;
    private static final int[] DIGITS_POWER = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    public OneTimePasswordAlgorithm(ICalendar cal) {
        this.calendar = cal;
    }

    private static final int[] doubleDigits = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

    private byte[] hmac_sha1(byte[] keyBytes, byte[] text)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha1;
        try {
            hmacSha1 = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException nsae) {
            hmacSha1 = Mac.getInstance("HMAC-SHA-1");
        }
        SecretKeySpec macKey = new SecretKeySpec(keyBytes, "RAW");
        hmacSha1.init(macKey);
        return hmacSha1.doFinal(text);
    }


    public String generateOTP(String secretKey, int codeDigits, int interval){
        String result = null;
        try {
        byte[] secret = secretKey.getBytes();

        if(codeDigits !=5 && codeDigits!=6) //only valid values
            codeDigits = 6;

        byte[] hash = hmac_sha1(secret, GetUTCdatetimeAsString(interval).getBytes());

        int offset = hash[hash.length - 1] & 0xf;
        int binary =
                ((hash[offset] & 0x7f) << 24)
                        | ((hash[offset + 1] & 0xff) << 16)
                        | ((hash[offset + 2] & 0xff) << 8)
                        | (hash[offset + 3] & 0xff);

        int otp = binary % DIGITS_POWER[codeDigits];

        result = Integer.toString(otp);
        while (result.length() < codeDigits) {
            result = "0" + result;
        }}
        catch (Exception e){

        }
        return result;
    }

    private String GetUTCdatetimeAsString(int interval)
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd hh:mm:");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));

        Calendar ct=calendar.getCurrentCalendar();
        final String utcTime = sdf.format(ct.getTime());
        int seconds = ct.get(Calendar.SECOND)/interval;

        return utcTime + Integer.toString(seconds);
    }
}