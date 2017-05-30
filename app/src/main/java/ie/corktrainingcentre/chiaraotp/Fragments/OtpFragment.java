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

        temp.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                if(event.getAction() == MotionEvent.ACTION_MOVE){
                    PopUp dialogFragment = new PopUp();
                    dialogFragment.show(getActivity().getFragmentManager(),"Alert Message");
                }
                return true;
            }
        });

        return temp;
    }


}
