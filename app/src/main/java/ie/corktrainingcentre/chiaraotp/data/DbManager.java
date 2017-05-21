package ie.corktrainingcentre.chiaraotp.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import java.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.provider.ContactsContract;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class DbManager {

    private DBHelper dbHelper;
    private final String InsertOTP = "INSERT INTO OTP(TIMESTAMP,SECRET,INTERVAL,DIGITS,APIURL,TYPE,APPNAME) VALUES(?,?,?,?,?,?,?)";
    private final String UpdateOTP = "UPDATE OTP SET TIMESTAMP=?,SECRET=?,INTERVAL=?,DIGITS=?,APIURL=? WHERE ID=?";

    public DbManager (Context ctx)
    {
        dbHelper = DBHelper.getInstance(ctx);
    }

    public String InserisciQualcosa()
    {
        this.Save(new OTBContract("123","test",0,0,""));
        int id = GetAppId("test");
        if(id>-1) {
            //Delete(new OTBContract(id));
            return "deleted";
        }
        return "not found";
    }

    public void Delete(OTBContract model)
    {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.execSQL("DELETE FROM OTP WHERE ID=?",new String[]{Integer.toString(model.getId())});
        db.close();
    }

    public void Save(OTBContract model){

        int id = GetAppId(model.getAppName());

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        List<String> pars2 = new ArrayList<String>();
        pars2.add(GetUTCdatetimeAsString());
        pars2.add(model.getSecret());
        pars2.add(Integer.toString(model.getInterval()));
        pars2.add(Integer.toString(model.getDigits()));
        pars2.add(model.getApiUrl());
        String[] pars;

        String query;
        if(id == -1)
        {
            query = InsertOTP;
            pars2.add("TOTP");
            pars2.add(model.getAppName());
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
