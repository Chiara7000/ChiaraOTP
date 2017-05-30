package ie.corktrainingcentre.chiaraotp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ie.corktrainingcentre.chiaraotp.Encryption.AesEncryption;
import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;
import ie.corktrainingcentre.chiaraotp.Helper.Constants;

public class DbManager {

    private DBHelper dbHelper;
    private final String InsertOTP = "INSERT INTO OTP(TIMESTAMP,SECRET,INTERVAL,DIGITS,APIURL,TYPE,APPNAME, OFFSET) VALUES(?,?,?,?,?,?,?,?)";
    private final String UpdateOTP = "UPDATE OTP SET TIMESTAMP=?,SECRET=?,INTERVAL=?,DIGITS=?,APIURL=? WHERE ID=?";

    public DbManager ()
    {
        dbHelper = DBHelper.getInstance(null);
    }

    public void DeleteAll()
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM OTP",new String[]{});
        db.close();
    }

    public Boolean Delete(int id)
    {
        Boolean ret = false;
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        try {
            db.execSQL("DELETE FROM OTP WHERE ID=?", new String[]{Integer.toString(id)});
            ret = true;
        }
        finally {
            if(db.isOpen())
                db.close();
        }
        return ret;
    }

    public void Save(OtpModel model){

        int id = GetAppId(model.getAppName());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<String> pars2 = new ArrayList<String>();
        pars2.add(GetUTCdatetimeAsString());
        pars2.add(model.getSecret());
        pars2.add(Integer.toString(model.getInterval()));
        pars2.add(Integer.toString(model.getDigits()));
        pars2.add(model.getApiUrl());

        String query;
        if(id == -1)
        {
            query = InsertOTP;
            pars2.add("TOTP");
            pars2.add(model.getAppName());
            pars2.add(Integer.toString(model.getOffset()));
        }
        else
        {
            query = UpdateOTP;
            pars2.add(Integer.toString(id));
        }

        try {
            db.execSQL(query, pars2.toArray());
        }
        finally{
            if(db.isOpen())
                db.close();
        }
    }


    public List<OtpModel> ReadAll(){

        String key =RSAManager.GetInstance(null).Decrypt(DBHelper.getInstance(null).readAESKey());

        List<OtpModel> ret = new ArrayList<OtpModel>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        try
        {
            Cursor cursor = db.rawQuery("SELECT * FROM OTP", new String[]{  });

            cursor.moveToFirst();
            for(int i=0;i<cursor.getCount();i++)
            {
                OtpModel m= new OtpModel();
                m.setId(cursor.getInt(cursor.getColumnIndex("ID")));
                m.setSecret(AesEncryption.Decrypt(key,cursor.getString(cursor.getColumnIndex("SECRET"))));
                m.setAppName(cursor.getString(cursor.getColumnIndex("APPNAME")));
                m.setInterval(cursor.getInt(cursor.getColumnIndex("INTERVAL")));
                m.setDigits(cursor.getInt(cursor.getColumnIndex("DIGITS")));
                m.setApiUrl(cursor.getString(cursor.getColumnIndex("APIURL")));
                m.setOffset(cursor.getInt(cursor.getColumnIndex("OFFSET")));

                ret.add(m);
                cursor.moveToNext();
            }

            cursor.close();
        }
        catch(Exception ex)
        {
            Log.e("DbManager",ex.getMessage());
        }
        finally {
            if(db.isOpen())
                db.close();
        }

        return ret;
    }

    //returns the timestamp of the current utc datetime
    private static String GetUTCdatetimeAsString()
    {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss a");
        sdf.setTimeZone(java.util.TimeZone.getTimeZone("UTC"));
        final String utcTime = sdf.format(new Date());

        return utcTime;
    }

    //Gets Id given appName if exists or -1
    private int GetAppId(String AppName){
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        String selectQuery = "SELECT ID FROM OTP WHERE APPNAME=?";

        Cursor c = db.rawQuery(selectQuery, new String[] {
                AppName
        });

        try
        {
            if(c.getCount()>0) {
                c.moveToFirst();

                do {
                    return c.getInt(c.getColumnIndex("ID"));
                }while (c.moveToNext());
            }
        }
        finally {
            c.close();
            db.close();
        }

        return -1;
    }

}
