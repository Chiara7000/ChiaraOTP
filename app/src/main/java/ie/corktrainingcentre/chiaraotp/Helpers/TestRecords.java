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
        String key =RSAManager.GetInstance(null).Decrypt(DBHelper.getInstance(null).readAESKey());
        DbManager db = new DbManager();
        db.DeleteAll();

        for (int i = 0; i < 3; i++)
        {
            Random r=new Random();
            OtpModel m = new OtpModel();
            m.setAppName("Company " + Integer.toString(i));
            m.setSecret(AesEncryption.Encrypt(key,UUID.randomUUID().toString()));
            m.setDigits(6);
            m.setApiUrl("http://192.168.1.6:81/api/time");
            m.setInterval(30);
            m.setOffset(r.nextInt(10)-5);

            db.Save(m);

        }
    }
}
