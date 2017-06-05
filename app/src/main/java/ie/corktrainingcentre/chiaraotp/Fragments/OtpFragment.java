package ie.corktrainingcentre.chiaraotp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import ie.corktrainingcentre.chiaraotp.Helpers.Constants;
import ie.corktrainingcentre.chiaraotp.Logic.OtpEntry;
import ie.corktrainingcentre.chiaraotp.R;

/**
 * Created by Chiara on 24/05/2017.
 */

public class OtpFragment extends Fragment implements View.OnTouchListener{

    //controls id
    private int idAppName = 0;
    private int idTime = 0;
    private int idOTP = 0;
    private int idBar = 0;
    private View localCopy=null;
    private OtpEntry entry=null;

    private Handler handler = new Handler();
    private Runnable timeRunnable = new Runnable(){
        @Override
        public void run()
        {
            PopUp p = PopUp.getInstance();
            p.setEntry(entry);
            p.show(getActivity().getFragmentManager(), "Delete OTP");
        }
    };


    public OtpFragment(){}

    public void setEntry(OtpEntry entry) {
        this.entry = entry;
    }

    public void setOtp(String otp) {
        //find the otp textview
        TextView t = ((TextView)this.getView().findViewById(this.idOTP));
        //set new value
        t.setText(otp);
    }

    public void setBarMax(int max) {
        ProgressBar bar = ((ProgressBar) this.getView().findViewById(this.idBar));
        bar.setMax(max);
    }

    public void setBar(int value) {
        ProgressBar bar = ((ProgressBar)this.getView().findViewById(this.idBar));
        bar.setProgress(value);
    }

    public void setTime(int time) {
        TextView t = ((TextView)this.getView().findViewById(this.idTime));
        t.setText(Integer.toString(time));
    }

    public void setAppName(String appName) {
        TextView t = ((TextView)this.getView().findViewById(this.idAppName));
        t.setText(appName);
    }


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){

        View temp = inflater.inflate(R.layout.otp_fragment,container, false);

        ProgressBar pb = (ProgressBar) temp.findViewById(R.id.progressBarToday);
        this.idBar = View.generateViewId();
        pb.setId(this.idBar);

        TextView otpTextView = (TextView)temp.findViewById(R.id.otp);
        this.idOTP = View.generateViewId();
        otpTextView.setId(this.idOTP);
        otpTextView.setText("");

        TextView timeTextView = (TextView)temp.findViewById(R.id.time);
        this.idTime = View.generateViewId();
        timeTextView.setId(this.idTime);
        timeTextView.setText("");

        TextView appNameTextView = (TextView)temp.findViewById(R.id.appName);
        this.idAppName = View.generateViewId();
        appNameTextView.setId(this.idAppName);
        appNameTextView.setText("");

        LinearLayout fragmentContainer = (LinearLayout)temp.findViewById(R.id.fragmentContainer);
        fragmentContainer.setId(View.generateViewId());

        fragmentContainer.setOnTouchListener(this);
        otpTextView.setOnTouchListener(this);
        timeTextView.setOnTouchListener(this);
        appNameTextView.setOnTouchListener(this);
        pb.setOnTouchListener(this);

        localCopy=temp;
        return temp;
    }

     @Override
     public boolean onTouch(View v,MotionEvent event) {
         //handler
         if(event.getAction() == MotionEvent.ACTION_DOWN )
         {
             handler.postDelayed(timeRunnable, Constants.WAIT_TIME_DELETE);
         }
         else if(event.getAction() == MotionEvent.ACTION_UP )
         {
             handler.removeCallbacks(timeRunnable);
         }
         return true;
     }


}
