package ie.corktrainingcentre.chiaraotp.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Seconds;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import ie.corktrainingcentre.chiaraotp.Interfaces.ITaskDoneListener;

/**
 * Created by Chiara on 02/06/2017.
 */

public class SynchroApiHelper extends AsyncTask<String,String,Integer> {

    ITaskDoneListener callback = null;

    public SynchroApiHelper(ITaskDoneListener callback){
        this.callback=callback;
    }

    @Override
    protected Integer doInBackground(String...urls)
    {
        DateTime dt2 = null;
        Integer seconds = Integer.MIN_VALUE;
        String ret ="";
        HttpURLConnection urlConnection = null;

        JSONObject object = null;
        InputStream inStream = null;
        try {
            urlConnection = (HttpURLConnection) new URL(urls[0]).openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if(urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {

                inStream = urlConnection.getInputStream();

                BufferedReader bReader = new BufferedReader(new InputStreamReader(inStream));
                String temp, response = "";
                while ((temp = bReader.readLine()) != null) {
                    response += temp;
                }
                DateTime d = new DateTime(response.toString().replace("\"", ""));
                dt2 = DateTime.now();

                seconds = Seconds.secondsBetween(dt2, d).getSeconds();
            }
        } catch (Exception e) {
            Log.e("",e.getMessage());
        } finally {
            if (inStream != null) {
                try {
                    // this will close the bReader as well
                    inStream.close();
                } catch (IOException ignored) {
                }
            }
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return seconds;
    }

    @Override
    protected void onPostExecute(Integer res) {
        super.onPostExecute(res);

        if (callback != null && res != null) {
            callback.success(res);
        } else
            callback.error();
    }

}
