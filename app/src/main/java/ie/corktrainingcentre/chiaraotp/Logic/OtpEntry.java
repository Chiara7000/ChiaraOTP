package ie.corktrainingcentre.chiaraotp.Logic;

import java.util.Calendar;

import ie.corktrainingcentre.chiaraotp.Activities.MainActivityOTP;
import ie.corktrainingcentre.chiaraotp.Fragments.OtpFragment;
import ie.corktrainingcentre.chiaraotp.Interfaces.ICalendar;
import ie.corktrainingcentre.chiaraotp.data.OtpModel;

/**
 * Created by Chiara on 29/05/2017.
 */

public class OtpEntry {

    private OtpFragment fragment = null;
    private OneTimePasswordAlgorithm otp = null;
    private ICalendar customClock;
    private MainActivityOTP parent=null;
    private int interval = 30;
    private OtpModel model=null;

    public OtpEntry(){
        customClock = new CustomCalendar();
        otp = new OneTimePasswordAlgorithm(customClock);
    }

    public String getApi() {
        return this.model.getApiUrl();
    }

    public OtpModel getModel() {
        return model;
    }

    public void setModel(OtpModel model) {
        this.model = model;
        customClock.setOffsetMilliseconds(model.getOffset());
    }

    public int GetRemainingSeconds()
    {
        int sec = this.customClock.getCurrentCalendar().get(Calendar.SECOND);

        while(sec>interval)
            sec-=interval;

        return (interval-sec)==interval?interval:interval-sec;
    }


    public MainActivityOTP getParent() {
        return this.parent;
    }

    public void setParent(MainActivityOTP mainAct) {
        this.parent = mainAct;
    }


    public String getOtp (){
        return otp.generateOTP(this.getSecret());
    }

    public int getId() {
        return this.getModel().getId();
    }

    public OtpFragment getFragment() {
        return fragment;
    }

    public void setFragment(OtpFragment fragment) {
        this.fragment = fragment;
        this.fragment.setEntry(this);
    }

    public String getAppName() {
        return this.getModel().getAppName();
    }

    public void setOffSet(int offSet) {
        this.getModel().setOffset(offSet);
        customClock.setOffsetMilliseconds(offSet);
    }

    private int getOffSet() {
        return this.getModel().getOffset();
    }

    private String getSecret() {
        return this.getModel().getSecret();
    }
}
