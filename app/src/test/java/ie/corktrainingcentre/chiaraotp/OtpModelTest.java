package ie.corktrainingcentre.chiaraotp;

import org.junit.Test;

import ie.corktrainingcentre.chiaraotp.data.OtpModel;

import static org.junit.Assert.assertTrue;

/**
 * Created by Chiara on 27/05/2017.
 */

public class OtpModelTest {

    @Test
    public void GetOTBContract_isCorrect(){
        try {
            OtpModel otp = OtpModel.GetOTBContract("");
            assertTrue(otp.getSecret().equals(null));
        }
        catch(Exception e)
        {
            assertTrue(true);
        }
    }

    @Test
    public void GetOTBContract_isCorrect_With_JSON(){
        OtpModel otp = OtpModel.GetOTBContract("{\"Secret\":\"cfe77627-da64-4f51-a142-46d05f59444d\",\"AppName\":\"TOTP_30_6\",\"Interval\":30,\"Digits\":6,\"TimeApi\":\"http://192.168.1.6:5000/Api/Time?id=6ebacb6f-0916-40f5-af9b-bc31f95bb792\",\"Type\":\"TOTP\"}");

        assertTrue(otp.getSecret().equals("cfe77627-da64-4f51-a142-46d05f59444d"));
        assertTrue(otp.getAppName().equals("TOTP_30_6"));
        assertTrue(otp.getInterval()==30);
        assertTrue(otp.getDigits()==6);
        assertTrue(otp.getApiUrl().equals("http://192.168.1.6:5000/Api/Time?id=6ebacb6f-0916-40f5-af9b-bc31f95bb792"));
    }

}
