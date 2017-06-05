package ie.corktrainingcentre.chiaraotp.Fragments;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import ie.corktrainingcentre.chiaraotp.Logic.OtpEntry;
import ie.corktrainingcentre.chiaraotp.R;
import ie.corktrainingcentre.chiaraotp.data.DbManager;

/**
 * Created by Chiara on 30/05/2017.
 */

public class PopUp extends DialogFragment implements View.OnClickListener{

    private Button yesButton;
    private Button noButton;
    private TextView popupText;
    private OtpEntry entry=null;
    private Boolean opened = false;

    private static PopUp instance = null;
    private static Object locker = new Object();

    private void PopUp(){}

    public static PopUp getInstance(){
        if(instance==null)
            synchronized (locker) {
                if (instance == null) {
                    instance=new PopUp();
                }
            }
        return instance;
    }

    public void setEntry(OtpEntry entry) {
        this.entry = entry;
    }

    @Override
    public void show(FragmentManager f, String message) {
        if (!opened) {
           opened = true;
            super.show(f, message);
        }
    }

    @Override
    public void dismiss() {
        super.dismiss();
        opened = false;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_layout, null);

        //popupText = (TextView) rootView.findViewById(R.id.popup);


        yesButton = (Button)rootView.findViewById(R.id.yesButton);
        noButton = (Button)rootView.findViewById(R.id.noButton);

        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);

        setCancelable(false);

        getDialog().setTitle("Confirmation");

        return rootView;
    }

    @Override
    public void onClick(View view){
        if (view.getId()== R.id.yesButton) {
            if((new DbManager()).Delete(this.entry.getId())) //delete the record from the database
            {
                this.entry.getParent().timer.cancel();
                getActivity().getFragmentManager().beginTransaction().remove(this.entry.getFragment()).commit();
                this.entry.getParent().list.remove(this.entry);
                this.entry.getParent().restart();
            }
        }
        dismiss();
    }
}

