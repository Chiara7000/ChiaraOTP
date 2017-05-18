package ie.corktrainingcentre.chiaraotp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.vision.barcode.Barcode;

public class MainActivityOTP extends AppCompatActivity {

    //private TextView barcodeResult;

    //private static final int RC_BARCODE_CAPTURE = 9001;
    //private static final String TAG = "BarcodeMain";

    public FloatingActionButton goScanner;

    public void init(){
        goScanner=(FloatingActionButton)findViewById(R.id.goScanner);
        goScanner.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent app = new Intent(MainActivityOTP.this,ScannerActivity.class);
                startActivity(app);
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
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        init(); //prima era sotto setContentView
        //barcodeResult=(TextView)findViewById(R.id.barcode_result);
    }

    /*public void scanBarcode(View v){
        Intent intent = new Intent(this, ScannerActivity.class);
        startActivityForResult(intent,0);
    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode, Intent data){

        if(requestCode==0){
            if (resultCode == CommonStatusCodes.SUCCESS){
                if(data!=null){
                    Barcode barcode = data.getParcelableExtra("barcode");
                    barcodeResult.setText("Barcode value : " + barcode.displayValue);
                }else{
                    barcodeResult.setText("No barcode found");
                }
            }
        }else{
            super.onActivityResult(requestCode,resultCode,data);
        }

    }*/

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
