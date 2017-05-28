package ie.corktrainingcentre.chiaraotp;

import android.util.Log;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import java.util.Random;

import ie.corktrainingcentre.chiaraotp.Helper.RandomString;
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
