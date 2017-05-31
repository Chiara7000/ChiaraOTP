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
import java.util.UUID;

import ie.corktrainingcentre.chiaraotp.Encryption.AesEncryption;
import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;
import ie.corktrainingcentre.chiaraotp.Fragments.OtpFragment;
import ie.corktrainingcentre.chiaraotp.Helper.Constants;
import ie.corktrainingcentre.chiaraotp.Helper.RandomString;
import ie.corktrainingcentre.chiaraotp.Helper.TestRecords;
import ie.corktrainingcentre.chiaraotp.data.DBHelper;
import ie.corktrainingcentre.chiaraotp.data.DbManager;
import ie.corktrainingcentre.chiaraotp.data.OtpModel;

import static ie.corktrainingcentre.chiaraotp.R.id.otp;

public class MainActivityOTP extends AppCompatActivity {
    public List<OtpEntry> list = new ArrayList<OtpEntry>();
    public FloatingActionButton goScanner;
    public Timer timer;

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

        //populate otp with test records when in debug mode
        if(Constants.DEBUG)
            TestRecords.InsertTestingRecords();

        refreshEntries();

        init();
    }

    public void refreshEntries(){

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //remove previous fragments
        for (OtpEntry otp : list){
            otp.getFragment();
            transaction.remove(otp.getFragment());
        }

        DbManager m=new DbManager();
        for (OtpModel otp : m.ReadAll())
        {
            OtpFragment t = new OtpFragment();
            OtpEntry o = new OtpEntry();
            o.setFragment(t);
            o.setAppName(otp.getAppName());
            o.setSecret(otp.getSecret());
            o.setParent(this);
            o.setId(otp.getId());
            o.setOffSet(otp.getOffset());

            list.add(o);
            transaction.add(R.id.otpContainer, t);
        }

        transaction.commit();
    }

    public void restart(){
        timer = new Timer();
        TimerTask t = new TimerTask() {
            int sec = 0;

            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        Random r = new Random ();

                        for (OtpEntry o : list) {

                            o.getFragment().setOtp(o.getOtp());
                            o.getFragment().setAppName(o.getAppName());
                            o.getFragment().setTime(r.nextInt(30)+1);
                        }
                    }
                });
            }
        };
        timer.scheduleAtFixedRate(t, 1000, 1000);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    protected void onResume(){
        super.onResume();
        restart();
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

                //encrypt
                String key =RSAManager.GetInstance(null).Decrypt(DBHelper.getInstance(null).readAESKey());
                model.setSecret(AesEncryption.Encrypt(key, model.getSecret()));
                (new DbManager()).Save(model);

                timer.cancel();
                refreshEntries();
                restart();
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
