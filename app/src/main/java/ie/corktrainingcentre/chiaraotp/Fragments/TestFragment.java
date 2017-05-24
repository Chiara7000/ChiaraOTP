package ie.corktrainingcentre.chiaraotp.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Random;

import ie.corktrainingcentre.chiaraotp.R;

/**
 * Created by Chiara on 24/05/2017.
 */

public class TestFragment extends Fragment {
    public int id=0;

    public TestFragment(){}

    public TestFragment(int id){
        this.id=id;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View temp = inflater.inflate(R.layout.test_fragment,container, false);

        TextView idText = (TextView)temp.findViewById(R.id.topText);
        idText.setId(this.id);
        idText.setText(Integer.toString(this.id));
        return temp;
    }

    public void SetText(String text)
    {
        ((TextView)this.getView().findViewById(this.id)).setText(text);
    }
}
