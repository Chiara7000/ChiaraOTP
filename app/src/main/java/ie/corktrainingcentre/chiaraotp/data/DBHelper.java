package ie.corktrainingcentre.chiaraotp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import ie.corktrainingcentre.chiaraotp.Helper.Constants;
import ie.corktrainingcentre.chiaraotp.Helper.RandomString;
import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;

import static android.content.Context.MODE_PRIVATE;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 10;
    private static final String DATABASE_NAME = "otp.db";

    private static Context appContext = null;
    private static DBHelper dbInstance = null;

    private static final String TABLE_OTP_CREATE = "CREATE TABLE IF NOT EXISTS OTP ("+
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

    private static final String TABLE_OTP_CONFIGS = "CREATE TABLE IF NOT EXISTS CONFIGS ("+
            "KEY TEXT PRIMARY KEY, " +
            "VALUE TEXT NOT NULL" +
            ");";

    public static DBHelper getInstance(Context context)
    {
        if(DBHelper.dbInstance == null && context!=null)
        {
            DBHelper.appContext = context.getApplicationContext();
            DBHelper.dbInstance = new DBHelper();

            SQLiteDatabase db = DBHelper.dbInstance.getWritableDatabase();
            db.close();

            CreateTables();
        }
        return DBHelper.dbInstance;
    }

    private static boolean doesDatabaseExist(Context context) {
        File dbFile = context.getApplicationContext().getDatabasePath(DATABASE_NAME);
        return dbFile.exists();
    }

    private DBHelper ()
    {
        super(DBHelper.appContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    private static void CreateTables()
    {
        SQLiteDatabase db = DBHelper.dbInstance.getWritableDatabase();

        db.execSQL(TABLE_OTP_CREATE);
        db.execSQL(TABLE_OTP_CONFIGS);

        //only if there is no key in the database
        if(!CheckKeyExists())
            CreateNewKey();
    }

    private static Boolean CheckKeyExists(){

        SQLiteDatabase db = DBHelper.dbInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT COUNT(*) FROM CONFIGS WHERE KEY=?", new String[]{ Constants.DATABASE_KEY_NAME });

        if(!cursor.moveToFirst())
        {
            cursor.close();
            return false;
        }

        int count = cursor.getInt(0);
        cursor.close();
        return count > 0;
    }

    public static String readAESKey(){

        SQLiteDatabase db = DBHelper.dbInstance.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT VALUE FROM CONFIGS WHERE KEY=?", new String[]{ Constants.DATABASE_KEY_NAME });

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
        catch(Exception e)
        {
            Log.e("DBHelper",e.getMessage());
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
        if(Constants.DEBUG){

            //TODO: EXPORT DATA, TO RE-IMPORT AFTER THE NEW STRUCTURE IS IN PLACE

            db.execSQL("DROP TABLE IF EXISTS OTP");
            db.execSQL("DROP TABLE IF EXISTS CONFIGS");
        }
    }
}
