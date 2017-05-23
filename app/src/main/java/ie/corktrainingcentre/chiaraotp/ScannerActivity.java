package ie.corktrainingcentre.chiaraotp;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
        import android.content.pm.PackageManager;
        import android.support.v4.app.ActivityCompat;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;

        import com.google.android.gms.common.api.CommonStatusCodes;
        import com.google.android.gms.vision.CameraSource;
        import com.google.android.gms.vision.Detector;
        import com.google.android.gms.vision.barcode.Barcode;
        import com.google.android.gms.vision.barcode.BarcodeDetector;

import android.util.Log;
import android.util.SparseArray;
        import android.view.SurfaceHolder;
        import android.view.SurfaceView;
        import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class ScannerActivity extends AppCompatActivity {

    private static final String TAG = ScannerActivity.class.getName();
    private SurfaceView cameraPreview;
    private CameraSource cameraSource;
    private BarcodeDetector barcodeDetector;
    private TextView barcodeResult;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        cameraPreview = (SurfaceView) findViewById(R.id.camera_preview);
        barcodeResult=(TextView)findViewById(R.id.barcode_result);
        createCameraSource();
    }

    private void createCameraSource() {

        barcodeDetector = new BarcodeDetector.Builder(this).setBarcodeFormats(Barcode.QR_CODE).build();

        cameraSource = new CameraSource.Builder(this, barcodeDetector)
                                       .setAutoFocusEnabled(true)
                                       .setRequestedPreviewSize(300, 300)
                                       .build();

        cameraPreview.getHolder().addCallback(new SurfaceHolder.Callback() {

            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (ActivityCompat.checkSelfPermission(ScannerActivity.this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
                {
                    return;
                }
                try {
                    cameraSource.start(cameraPreview.getHolder());
                } catch (IOException e) {
                    Log.e(TAG, "Failed to to start the camera", e);
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height){

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder){
                cameraSource.stop();
            }
        });

        barcodeDetector.setProcessor(new Detector.Processor<Barcode>(){

            @Override
            public void release(){

            }

            @Override
            public void receiveDetections (Detector.Detections<Barcode>detections){

                final SparseArray<Barcode> barcodes = detections.getDetectedItems();

                if(barcodes.size()!= 0)
                {
                    String res = barcodes.valueAt(0).displayValue;

                    Bundle data = new Bundle();
                    data.putString("code",res);

                    Intent inte = new Intent();
                    inte.putExtras(data);

                    setResult(Activity.RESULT_OK, inte);
                    finish();
                }
            }
        });
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
        super.onDestroy();
        cameraSource.release();
        barcodeDetector.release();
    }
}
