package ie.corktrainingcentre.chiaraotp.data;

import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import ie.corktrainingcentre.chiaraotp.MainActivityOTP;


public final class OTBContract implements BaseColumns{

    private static final String TAG = MainActivityOTP.class.getName();
    private String secret;
    private String appname;
    private int interval;
    private int digits;
    private int id;
    private String apiUrl;


    public OTBContract(String secret,String appname,int interval,int digits,String apiUrl)
    {
        this.secret = secret;
        this.appname = appname;
        this.interval = interval;
        this.digits = digits;
        this.apiUrl = apiUrl;
    }

    public OTBContract(int id)
    {
        this.id =id;
    }

    private OTBContract()
    {}

    public static OTBContract GetOTBContract(String json){
        try {
            JSONObject mainObject = new JSONObject(json);

            OTBContract c = new OTBContract();
            c.secret = mainObject.getString("Secret");
            c.appname = mainObject.getString("AppName");
            c.interval = mainObject.getInt("Interval");
            c.digits = mainObject.getInt("Digits");
            c.apiUrl = mainObject.getString("ApiUrl");
            //c.type = mainObject.getString("Type");
            return c;

        }
        catch(JSONException e)
        {
            Log.e(TAG, "unexpected JSON exception", e);
            // Do something to recover ... or kill the app.
        }
        return null;
    }

    public int getId(){
        return id;
    }
    public void setId(int value){
        id=value;
    }

    public String getSecret(){
        return secret;
    }
    public void setSecret(String value){
        secret=value;
    }

    public String getAppName(){
        return appname;
    }
    public void setAppName(String value){
        appname=value;
    }

    public int getDigits(){
        return digits;
    }
    public void setDigits(int value){
        digits=value;
    }

    public int getInterval(){
        return interval;
    }
    public void setInterval(int value){
        interval=value;
    }

    public String getApiUrl(){
        return apiUrl;
    }
    public void setApiUrl(String value){
        apiUrl=value;
    }

    public String ToString(){
        return "Secret:"+this.getSecret();
    }
}
