package ie.corktrainingcentre.chiaraotp.Fragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;


import ie.corktrainingcentre.chiaraotp.OtpEntry;
import ie.corktrainingcentre.chiaraotp.R;

/**
 * Created by Chiara on 24/05/2017.
 */

public class OtpFragment extends Fragment implements View.OnTouchListener{

    //controls id
    private int idAppName = 0;
    private int idTime = 0;
    private int idOTP = 0;
    private View localCopy=null;
    private OtpEntry entry=null;

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

       /* temp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                PopUp dialogFragment = new PopUp();
                dialogFragment.show(getActivity().getFragmentManager(),"Alert Message");

                return true;
            }
        });*/
        localCopy=temp;
        return temp;
    }

     @Override
     public boolean onTouch(View v,MotionEvent event) {
        //PopUp dialogFragment = new PopUp();
        //dialogFragment.show(getActivity().getFragmentManager(),"Alert Message");
         if(event.getPressure()>0.5f) {
             PopUp p = PopUp.getInstance();
             p.setEntry(this.entry);
             p.show(getActivity().getFragmentManager(), "Delete OTP");
         }
         return true;
     }
}
