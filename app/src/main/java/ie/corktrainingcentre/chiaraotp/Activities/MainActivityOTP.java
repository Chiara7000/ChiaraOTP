package ie.corktrainingcentre.chiaraotp.Activities;

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
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ie.corktrainingcentre.chiaraotp.Encryption.AesEncryption;
import ie.corktrainingcentre.chiaraotp.Encryption.RSAManager;
import ie.corktrainingcentre.chiaraotp.Fragments.OtpFragment;
import ie.corktrainingcentre.chiaraotp.Helpers.Constants;
import ie.corktrainingcentre.chiaraotp.Helpers.SynchroApiHelper;
import ie.corktrainingcentre.chiaraotp.Helpers.TestRecords;
import ie.corktrainingcentre.chiaraotp.Interfaces.ITaskDoneListener;
import ie.corktrainingcentre.chiaraotp.Logic.OtpEntry;
import ie.corktrainingcentre.chiaraotp.R;
import ie.corktrainingcentre.chiaraotp.data.DBHelper;
import ie.corktrainingcentre.chiaraotp.data.DbManager;
import ie.corktrainingcentre.chiaraotp.data.OtpModel;

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

        new SynchroApiHelper(new ITaskDoneListener(){
            public void success(int response){
                toast(Integer.toString(response));
            }
            public void error(){
                toast("an error occurred");
            }
        }).execute("http://192.168.1.6:81/api/time");

        init();
    }

    public void refreshEntries(){

        Log.i("refreshEntries","start");

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //remove previous fragments
        for (OtpEntry otp : list){
            otp.getFragment();
            transaction.remove(otp.getFragment());
        }

        list.clear();

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
            o.setInterval(otp.getInterval());

            //o.getFragment()
            list.add(o);
            transaction.add(R.id.otpContainer, t);
        }

        transaction.commit();
    }

    public void restart(){

        Log.i("restart","timer start");

        if(timer!=null) {
            timer.cancel();
            timer = null;
        }
        timer = new Timer();
        TimerTask t = new TimerTask() {
            int sec = 0;

            @Override
            public void run() {

                Log.i("restart","refresh");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        for (OtpEntry o : list) {
                            o.getFragment().setOtp(o.getOtp());
                            o.getFragment().setAppName(o.getAppName());
                            o.getFragment().setTime(o.GetRemainingSeconds());
                            o.getFragment().setBarMax(30);
                            o.getFragment().setBar(o.GetRemainingSeconds());

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
        Log.i("","onResume start");
        super.onResume();
        restart();
    }

    protected void onPause(){
        Log.i("","onPause start");
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

                Log.i("","back from the camera");

                Bundle res = data.getExtras();
                String result = res.getString("code");

                OtpModel model = OtpModel.GetOTBContract(result);

                //encrypt
                String key =RSAManager.GetInstance(null).Decrypt(DBHelper.getInstance(null).readAESKey());
                model.setSecret(AesEncryption.Encrypt(key, model.getSecret()));
                (new DbManager()).Save(model);

                Log.i("","new entry saved");
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
