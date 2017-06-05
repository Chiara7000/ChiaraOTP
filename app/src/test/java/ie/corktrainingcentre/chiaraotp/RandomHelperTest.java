package ie.corktrainingcentre.chiaraotp;

import org.junit.Test;

import java.util.Random;

import ie.corktrainingcentre.chiaraotp.Helpers.RandomHelper;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 */
public class RandomHelperTest {

    @Test
    public void RandomString_isCorrect() throws Exception {
        Random r = new Random();
        int index = r.nextInt();
        int length = RandomHelper.RandomHelper(index).length();
        assertTrue(length>=6 && length<=100);

        if(index>= 6 && index<=100)
            assertTrue(length == index);
    }

    @Test
    public void RandomNumbers_isCorrect() throws Exception {
        String temp = RandomHelper.RandomStringOnlyNumbers(10);
        assertTrue(temp.length() == 10);

        for (int i=0; i<temp.length();i++)
            if(!"0123456789".contains(""+temp.charAt(i)))
            {
                assertTrue(false);
            }
    }
}