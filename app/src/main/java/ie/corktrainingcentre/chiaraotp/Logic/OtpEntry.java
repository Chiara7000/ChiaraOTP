package ie.corktrainingcentre.chiaraotp.Logic;

import ie.corktrainingcentre.chiaraotp.Activities.MainActivityOTP;
import ie.corktrainingcentre.chiaraotp.Fragments.OtpFragment;
import ie.corktrainingcentre.chiaraotp.Interfaces.ICalendar;

/**
 * Created by Chiara on 29/05/2017.
 */

public class OtpEntry {

    private OtpFragment fragment = null;
    private String appName = "";
    private String secret = "";
    private OneTimePasswordAlgorithm otp = null;
    private ICalendar customClock;
    private int offSet;
    private MainActivityOTP parent=null;
    private int id=0;
    private int interval = 30;

    public OtpEntry(){
        customClock = new CustomCalendar();
        otp = new OneTimePasswordAlgorithm(customClock);
    }

    public int GetRemainingSeconds()
    {
        int sec = this.customClock.getSeconds();

        while(sec>interval)
            sec-=interval;

        return (interval-sec)==interval?interval:interval-sec;
    }

    public void setInterval(int interval)
    {
        if(interval>60) interval = 60;
        if(interval<20) interval = 20;

        this.interval = interval;
    }

    public MainActivityOTP getParent() {
        return this.parent;
    }

    public void setParent(MainActivityOTP mainAct) {
        this.parent = mainAct;
    }

    public void setOffSet(int offSet) {
        this.offSet = offSet;
        customClock.setOffsetSeconds(this.offSet);
    }

    public String getOtp (){
        return otp.generateOTP(getSecret());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }

    public OtpFragment getFragment() {
        return fragment;
    }

    public void setFragment(OtpFragment fragment) {
        this.fragment = fragment;
        this.fragment.setEntry(this);
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }


    private ICalendar getCustomClock() {
        return customClock;
    }

    private void setCustomClock(ICalendar customClock) {
        this.customClock = customClock;
    }

    private int getOffSet() {
        return offSet;
    }

    private String getSecret() {
        return secret;
    }
}
