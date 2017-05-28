package ie.corktrainingcentre.chiaraotp;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;
import ie.corktrainingcentre.chiaraotp.Fragments.OtpFragment;
import ie.corktrainingcentre.chiaraotp.Helper.RandomString;
import ie.corktrainingcentre.chiaraotp.data.DBHelper;
import ie.corktrainingcentre.chiaraotp.data.OtpModel;

public class MainActivityOTP extends AppCompatActivity {
    List<OtpFragment> list = new ArrayList<OtpFragment>();
    public FloatingActionButton goScanner;
    Timer timer;

    public void init() {
        goScanner = (FloatingActionButton) findViewById(R.id.goScanner);
        goScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent app = new Intent(MainActivityOTP.this, ScannerActivity.class);
                startActivityForResult(app, 1);
            }

        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_otp);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        goScanner = (FloatingActionButton) findViewById(R.id.goScanner);
        goScanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
            }
        });

        RSAManager.GetInstance(this); //initialize
        DBHelper.getInstance(this);

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        int c = 123456;

        for (int i = 0; i < 10; i++) {
            OtpFragment t = new OtpFragment();

            list.add(t);
            transaction.add(R.id.otpContainer, t);
            // t.SetText(Integer.toString(c++));
        }

        transaction.commit();
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onResume(){
        super.onResume();
        timer = new Timer();
        TimerTask t = new TimerTask() {
            int sec = 0;

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Random r = new Random ();
                        String temp = OneTimePasswordAlgorithm.generateOTP("chiave");

                        for (OtpFragment t : list) {
                            //t.setOtp(RandomString.RandomStringOnlyNumbers(6));
                            t.setOtp(temp);
                            t.setAppName("ciao");
                            t.setTime(r.nextInt(30)+1);
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(t, 1000, 1000);

    }

    protected void onPause(){
        super.onPause();
        timer.cancel();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Vibrator v = (Vibrator) this.getApplicationContext().getSystemService(Context.VIBRATOR_SERVICE);
        // Vibrate for 500 milliseconds
        v.vibrate(500);

        // Check which request we're responding to
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == Activity.RESULT_OK) {
                Bundle res = data.getExtras();
                String result = res.getString("code");

                OtpModel model = OtpModel.GetOTBContract(result);

                toast("Ricevuto: " + model.test());
                //encrypt

                //save in the database

                //start generating TOTP
            }
        }
    }

    private void toast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    //----------------------------------------------------------------------------------------------

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_activity_ot, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
