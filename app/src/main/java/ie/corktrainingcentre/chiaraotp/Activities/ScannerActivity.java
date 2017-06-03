package ie.corktrainingcentre.chiaraotp.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

import com.google.android.gms.vision.CameraSource;
        import com.google.android.gms.vision.Detector;
        import com.google.android.gms.vision.barcode.Barcode;
        import com.google.android.gms.vision.barcode.BarcodeDetector;

import android.util.Log;
import android.util.SparseArray;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import ie.corktrainingcentre.chiaraotp.Helpers.Constants;
import ie.corktrainingcentre.chiaraotp.R;

public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = ScannerActivity.class.getName();
    private SurfaceView cameraPreview;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;

    private static final int CAMERA_PERMISSION_CAMERA = 0x000000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Box box = new Box(this);

        this.setContentView(R.layout.activity_scanner);


        addContentView(box, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);

        createCameraSource();
    }

    private void createCameraSource() {

        if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ScannerActivity.this,new String[]{Manifest.permission.CAMERA},0x000000);
            return;
        }

        if(Constants.BYPASS_CAMERA)
        {
            String res = "{\"Secret\":\"505ac90f-4b9f-412b-9132-d9eb0f9b2521\",\"AppName\":\"TestApp\",\"Interval\":30,\"Digits\":6,\"TimeApi\":\"http://192.168.1.6:81/Api/Time\",\"Type\":\"TOTP\"}";

            Bundle data = new Bundle();
            data.putString("code",res);

            Intent inte = new Intent();
            inte.putExtras(data);

            setResult(Activity.RESULT_OK, inte);
            finish();
            return;
        }
        else {
            barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

            cameraSource = new CameraSource.Builder(this, barcodeDetector)
                    .setAutoFocusEnabled(true)
                    .setRequestedPreviewSize(300, 300)
                    .build();

            cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {

                @Override
                public void surfaceCreated(SurfaceHolder holder) {

                    try {
                        cameraSource.start(cameraPreview.getHolder());
                    } catch (IOException e) {
                        Log.e(TAG, "Failed to to start the camera", e);
                    }
                }

                @Override
                public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

                }

                @Override
                public void surfaceDestroyed(SurfaceHolder holder) {
                    cameraSource.stop();
                }
            });

            barcodeDetector.setProcessor(new Detector.Processor<Barcode>() {

                @Override
                public void release() {

                }

                @Override
                public void receiveDetections(Detector.Detections<Barcode> detections) {

                    final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                    if (barcodes.size() != 0) {
                        String res = barcodes.valueAt(0).displayValue;

                        Bundle data = new Bundle();
                        data.putString("code", res);

                        Intent inte = new Intent();
                        inte.putExtras(data);

                        setResult(Activity.RESULT_OK, inte);
                        finish();
                    }
                }
            });
        }
    }

    public void toast(String msg){
        Context context = getApplicationContext();
        CharSequence text = msg;
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }


    @Override
    protected void onDestroy(){

        if(cameraSource!=null)
            cameraSource.release();
        if(barcodeDetector!=null)
            barcodeDetector.release();

        super.onDestroy();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,String permissions[],int[] grantResults)
    {
        switch (requestCode) {
            case CAMERA_PERMISSION_CAMERA: {
                if (grantResults.length > 0 && grantResults[0]== PackageManager.PERMISSION_GRANTED) {

                    Intent startMain = new Intent(ScannerActivity.this, ScannerActivity.class);
                    startActivity(startMain);
                }
                return;
            }
        }
        return;
    }
}
