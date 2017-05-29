package ie.corktrainingcentre.chiaraotp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import ie.corktrainingcentre.chiaraotp.Helper.Constants;
import ie.corktrainingcentre.chiaraotp.Helper.RandomString;
import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "otp.db";

    private static Context appContext = null;
    private static DBHelper dbInstance = null;

    private static final String TABLE_OTP_CREATE = "CREATE TABLE OTP ("+
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "SECRET TEXT NOT NULL, " +
            "APPNAME TEXT NOT NULL UNIQUE, " +
            "INTERVAL INTEGER NOT NULL, " +
            "DIGITS INTEGER NOT NULL, " +
            "APIURL TEXT NOT NULL, " +
            "TIMESTAMP TEXT NOT NULL, "+
            "TYPE TEXT NOT NULL, " +
            "OFFSET INTEGER" + //int offset for seconds
            ");";

    private static final String TABLE_OTP_CONFIGS = "CREATE TABLE CONFIGS ("+
            "KEY TEXT PRIMARY KEY, " +
            "VALUE TEXT NOT NULL" +
            ");";

    public static DBHelper getInstance(Context context)
    {
        if(DBHelper.dbInstance == null && context!=null)
        {
            DBHelper.appContext = context.getApplicationContext();
            DBHelper.dbInstance = new DBHelper();
            CreateTables();
        }
        return DBHelper.dbInstance;
    }

    private DBHelper ()
    {
        super(DBHelper.appContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static void CreateTables()
    {
        SQLiteDatabase db = DBHelper.dbInstance.getWritableDatabase();

        if(Constants.DEBUG){
            db.execSQL("DROP TABLE OTP");
            db.execSQL("DROP TABLE CONFIGS");
        }

        if(!CheckTableExistance("OTP"))
            db.execSQL(TABLE_OTP_CREATE);

        if(!CheckTableExistance("CONFIGS"))
            db.execSQL(TABLE_OTP_CONFIGS);

        //only if there is no key in the database
        if(!CheckKeyExists())
            CreateNewKey();
    }

    private static boolean CheckTableExistance(String table)
    {
        SQLiteDatabase db = DBHelper.dbInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM sqlite_master WHERE type = 'table' and name=?", new String[]{ table });

        if(!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }

        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    private static Boolean CheckKeyExists(){

        SQLiteDatabase db = DBHelper.dbInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM CONFIGS WHERE KEY=?", new String[]{ Constants.APPKEY });

        if(!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }

        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    private static String readAESKey(){

        SQLiteDatabase db = DBHelper.dbInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT VALUE FROM CONFIGS WHERE KEY=?", new String[]{ Constants.APPKEY });

        if(!cursor.moveToFirst())
        {
            cursor.close();
            return null;
        }

        String v = cursor.getString(0);
        cursor.close();
        return v;
    }

    private static void CreateNewKey()
    {
        SQLiteDatabase db = DBHelper.dbInstance.getWritableDatabase();
        List<String> pars = new ArrayList<String>();
        String tempKey = RandomString.RandomString(30);
        RSAManager rsa = RSAManager.GetInstance(null);

        pars.add(Constants.DATABASE_KEY_NAME);
        pars.add(rsa.Encrypt(tempKey));

        try {
            db.execSQL("INSERT INTO CONFIGS(KEY,VALUE) VALUES(?,?)", pars.toArray());
        }
        finally{
            if(db.isOpen())
                db.close();
        }
    }



    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
