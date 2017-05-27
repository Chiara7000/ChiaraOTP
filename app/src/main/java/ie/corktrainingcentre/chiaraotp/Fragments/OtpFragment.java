package ie.corktrainingcentre.chiaraotp.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import ie.corktrainingcentre.chiaraotp.R;

/**
 * Created by Chiara on 24/05/2017.
 */

public class OtpFragment extends Fragment {

    //controls id
    private int idAppName = 0;
    private int idTime = 0;
    private int idOTP = 0;

    public OtpFragment(){}

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

        return temp;
    }

}
