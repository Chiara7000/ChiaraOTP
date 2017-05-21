package ie.corktrainingcentre.chiaraotp.data;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

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

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //db.execSQL("DROP TABLE OTP;");
        //db.execSQL(TABLE_OTP_CREATE);
    }

    private static void CreateTables()
    {
        SQLiteDatabase db = DBHelper.dbInstance.getWritableDatabase();

        /*
        //if table structure changes

        if(CheckTableExistance("OTP")) {

            db.execSQL("DROP TABLE OTP;");
        }
        */
        if(!CheckTableExistance("OTP"))
            db.execSQL(TABLE_OTP_CREATE);
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
}
