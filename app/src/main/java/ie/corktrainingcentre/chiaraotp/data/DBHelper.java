package ie.corktrainingcentre.chiaraotp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import ie.corktrainingcentre.chiaraotp.Constants;
import ie.corktrainingcentre.chiaraotp.RSAManager;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "otp.db";

    private static Context appContext = null;
    private static DBHelper dbInstance = null;

    private static final String TABLE_OTP_CREATE = "CREATE TABLE OTP ("+
            "ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "SECRET TEXT, " +
            "APPNAME TEXT, " +
            "INTERVAL INTEGER, " +
            "DIGITS INTEGER, " +
            "APIURL TEXT, " +
            "TIMESTAMP TEXT, "+
            "TYPE TEXT" +
            ");";

    private static final String TABLE_OTP_CONFIGS = "CREATE TABLE CONFIGS ("+
            "KEY TEXT PRIMARY KEY, " +
            "VALUE TEXT " +
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

        //read the database to check if the key already exists
        //SELECT COUNT(*) FROM CONFIGS WHERE KEY=Constants.APPKEY

        //return count(*)>0
        return false;
    }

    private static void CreateNewKey()
    {
        SQLiteDatabase db = DBHelper.dbInstance.getWritableDatabase();
        List<String> pars = new ArrayList<String>();
        String tempKey = RandomString(30);
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

    private static String RandomString(int length)
    {
        String validChars = "QWERTYUIOPLKJHGFDSAZXCVBNMqwertyuioplkjhgfdsazxcvbnm0123456789!_òç@#°à'ù§+*èé'£$%&/()=?^ì\\|<>,;.:-";
        Random r= new Random();

        if(length<6) length = 6;

        String ret="";
        for(int i=0; i<length; i++)
        {
            ret += validChars.charAt(r.nextInt(validChars.length()));
        }
        return ret;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
