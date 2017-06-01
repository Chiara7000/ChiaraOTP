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
            assertTrue(otp == null);
        }
        catch(Exception e)
        {
            assertTrue(true);
        }
    }


}
