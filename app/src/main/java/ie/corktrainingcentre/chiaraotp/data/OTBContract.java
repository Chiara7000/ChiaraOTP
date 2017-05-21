package ie.corktrainingcentre.chiaraotp.data;

import android.provider.BaseColumns;


public final class OTBContract implements BaseColumns{

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
