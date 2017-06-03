package ie.corktrainingcentre.chiaraotp.Activities;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.view.View;

/**
 * Created by Chiara on 03/06/2017.
 */

public class Box extends View {
    private Paint paint = new Paint();
    Box(Context context) {
        super(context);
    }

    @Override
    protected void onDraw(Canvas canvas) { // Override the onDraw() Method
        super.onDraw(canvas);

        Paint eraser = new Paint();
        eraser.setColor(0xFFFFFFFF);
        eraser.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(Color.BLUE);
        paint.setStrokeWidth(10);
        canvas.drawColor(0x40444444);


        //center
        int x0 = canvas.getWidth()/2;
        int y0 = canvas.getHeight()/2;
        int dx = canvas.getHeight()/5;
        int dy = canvas.getHeight()/5;
        //draw guide box
        canvas.drawRect(x0-dx, y0-dy, x0+dx, y0+dy, eraser);
        canvas.drawRect(x0-dx, y0-dy, x0+dx, y0+dy, paint);



    }


}