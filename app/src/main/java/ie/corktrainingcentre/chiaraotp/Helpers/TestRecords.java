package ie.corktrainingcentre.chiaraotp.Helpers;

import java.util.Random;
import java.util.UUID;

import ie.corktrainingcentre.chiaraotp.Encryption.AesEncryption;
import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;
import ie.corktrainingcentre.chiaraotp.data.DBHelper;
import ie.corktrainingcentre.chiaraotp.data.DbManager;
import ie.corktrainingcentre.chiaraotp.data.OtpModel;

/**
 * Created by Chiara on 30/05/2017.
 */

public class TestRecords {

    public static void InsertTestingRecords()
    {
        DbManager db = new DbManager();
        db.DeleteAll();

        Random r=new Random();
        for (int i = 0; i < 3; i++)
        {
            OtpModel m = new OtpModel();
            m.setAppName("Company " + Integer.toString(i));
            m.setSecret(UUID.randomUUID().toString());

            m.setDigits(RandomHelper.RandomAmongGiven(5,6));
            m.setApiUrl("http://192.168.1.6:81/api/time");
            m.setInterval(RandomHelper.RandomAmongGiven(20,30));
            m.setOffset(0);

            db.Save(m);

        }
    }
}
