package ie.corktrainingcentre.chiaraotp.Helper;

import java.util.UUID;

import ie.corktrainingcentre.chiaraotp.Encryption.AesEncryption;
import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;
import ie.corktrainingcentre.chiaraotp.Fragments.OtpFragment;
import ie.corktrainingcentre.chiaraotp.OtpEntry;
import ie.corktrainingcentre.chiaraotp.R;
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
            OtpModel m = new OtpModel();
            m.setAppName("Company " + Integer.toString(i));
            m.setSecret(AesEncryption.Encrypt(key,UUID.randomUUID().toString()));
            m.setDigits(6);
            m.setApiUrl("www.something.com/api");
            m.setInterval(30);
            m.setOffset(0);

            db.Save(m);
        }
    }
}
