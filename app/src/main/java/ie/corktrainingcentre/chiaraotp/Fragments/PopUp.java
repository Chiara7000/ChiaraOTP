package ie.corktrainingcentre.chiaraotp.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import ie.corktrainingcentre.chiaraotp.OtpEntry;
import ie.corktrainingcentre.chiaraotp.R;

/**
 * Created by AppDeveloper on 30/05/2017.
 */

public class PopUp extends DialogFragment implements View.OnClickListener{

    private Button yesButton;
    private Button noButton;
    private TextView popupText;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.popup_layout, null);
        popupText = (TextView) rootView.findViewById(R.id.popup);
        yesButton = (Button)rootView.findViewById(R.id.yesButton);
        noButton = (Button)rootView.findViewById(R.id.noButton);
        yesButton.setOnClickListener(this);
        noButton.setOnClickListener(this);
        setCancelable(false);

        getDialog().setTitle("Simple Dialog");
        return rootView;
    }

    @Override
    public void onClick(View view){
        if (view.getId()== R.id.yesButton){
            getActivity().getFragmentManager().beginTransaction().remove(this).commit();
            dismiss();
        }
        else if (view.getId() == R.id.noButton){
            dismiss();
        }
    }
}

