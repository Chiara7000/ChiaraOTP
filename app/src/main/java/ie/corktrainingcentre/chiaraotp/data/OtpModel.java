package ie.corktrainingcentre.chiaraotp.data;

import android.provider.BaseColumns;
import android.util.Log;

import org.json.JSONObject;

import ie.corktrainingcentre.chiaraotp.Activities.MainActivityOTP;


public final class OtpModel implements BaseColumns{

    private static final String TAG = MainActivityOTP.class.getName();

    private String secret;
    private String appname;
    private int interval;
    private int digits;
    private int id;
    private String apiUrl;
    private int offset;

    public OtpModel()
    {}

    public static OtpModel GetOTBContract(String json){

        OtpModel c = null;

        try {
            JSONObject mainObject = new JSONObject(json);
            c = new OtpModel();
            c.secret = mainObject.getString("Secret");
            c.appname = mainObject.getString("AppName");
            c.interval = mainObject.getInt("Interval");
            c.digits = mainObject.getInt("Digits");
            c.apiUrl = mainObject.getString("TimeApi");
            c.offset = 0;
            //c.type = "OTP";
        }
        catch(Exception e)
        {
            c = new OtpModel();
        }
        return c;
    }

    public int getOffset(){
        return offset;
    }
    public void setOffset(int offset){
        this.offset=offset;
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

}
