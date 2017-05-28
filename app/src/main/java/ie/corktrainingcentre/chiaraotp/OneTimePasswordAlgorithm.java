package ie.corktrainingcentre.chiaraotp;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static android.R.attr.digits;
import static android.os.Build.VERSION_CODES.M;

/**
 * Created by Chiara on 28/05/2017.
 */

public class OneTimePasswordAlgorithm {
    private OneTimePasswordAlgorithm() {}

    private static final int[] doubleDigits = {0, 2, 4, 6, 8, 1, 3, 5, 7, 9};

    public static int calcChecksum(long num, int digits) {
        boolean doubleDigit = true;
        int total = 0;
        while (0 < digits--) {
            int digit = (int) (num % 10);
            num /= 10;
            if (doubleDigit) {
                digit = doubleDigits[digit];
            }
            total += digit;
            doubleDigit = !doubleDigit;
        }
        int result = total % 10;
        if (result > 0) {
            result = 10 - result;
        }
        return result;
    }

    public static byte[] hmac_sha1(byte[] keyBytes, byte[] text)
            throws NoSuchAlgorithmException, InvalidKeyException {
        Mac hmacSha1;
        try {
            hmacSha1 = Mac.getInstance("HmacSHA1");
        } catch (NoSuchAlgorithmException nsae) {
            hmacSha1 = Mac.getInstance("HMAC-SHA-1");
        }
        SecretKeySpec macKey =
                new SecretKeySpec(keyBytes, "RAW");
        hmacSha1.init(macKey);
        return hmacSha1.doFinal(text);
    }

    private static final int[] DIGITS_POWER
            // 0 1  2   3    4     5      6       7        8
            = {1, 10, 100, 1000, 10000, 100000, 1000000, 10000000, 100000000};

    static public String generateOTP(String secretKey){
        String result = null;
        try {
        byte[] secret = secretKey.getBytes();
        int codeDigits = 6;

        byte[] hash = hmac_sha1(secret, GetUTCdatetimeAsString().getBytes());

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

    private static String GetUTCdatetimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy MM dd hh:mm:");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        Calendar calendar = Calendar.getInstance();
        int seconds = calendar.get(Calendar.SECOND)/30;

        return utcTime + Integer.toString(seconds);
    }
}