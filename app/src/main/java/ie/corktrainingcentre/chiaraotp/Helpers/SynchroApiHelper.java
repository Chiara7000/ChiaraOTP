package ie.corktrainingcentre.chiaraotp.Helpers;

import android.os.AsyncTask;
import android.util.Log;

import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.PeriodType;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

import ie.corktrainingcentre.chiaraotp.Interfaces.ITaskDoneListener;

/**
 * Created by Chiara on 02/06/2017.
 */

public class SynchroApiHelper extends AsyncTask<String,String,SynchroResponse> {

    ITaskDoneListener callback = null;
    int idEntry;

    public SynchroApiHelper(int idEntry, ITaskDoneListener callback){
        this.callback=callback;
        this.idEntry = idEntry;
    }

    @Override
    protected SynchroResponse doInBackground(String...urls)
    {
        if(urls.length==0 || urls[0]==null)
            return null;

        DateTime dt2 = null;
        Integer milliseconds = Integer.MIN_VALUE;
        String ret ="";
        HttpURLConnection urlConnection = null;
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
                dt2 = new DateTime(Calendar.getInstance(TimeZone.getTimeZone("UTC")));
                Date c= Calendar.getInstance().getTime();
                milliseconds =  new Period(dt2,d, PeriodType.millis()).getMillis();
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

        return new SynchroResponse(this.idEntry,milliseconds);
    }

    @Override
    protected void onPostExecute(SynchroResponse res) {
        super.onPostExecute(res);

        if (callback != null && res != null && res.getOffset()!=Integer.MIN_VALUE) {
            callback.success(res);
        } else
            callback.error();
    }

}
