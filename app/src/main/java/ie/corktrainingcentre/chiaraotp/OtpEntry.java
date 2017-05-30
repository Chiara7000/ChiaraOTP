package ie.corktrainingcentre.chiaraotp;

import android.app.Fragment;

import java.util.Calendar;

import ie.corktrainingcentre.chiaraotp.Fragments.OtpFragment;

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

    public OtpEntry(){
        customClock = new CustomCalendar();
        otp = new OneTimePasswordAlgorithm(customClock);
    }

    public MainActivityOTP getParent() {
        return this.parent;
    }
    public void setParent(MainActivityOTP mainAct) {
        this.parent = mainAct;
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

    public void setOffSet(int offSet) {
        this.offSet = offSet;
    }

    public String getOtp (){
        return otp.generateOTP(getSecret());
    }

    private String getSecret() {
        return secret;
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


}
