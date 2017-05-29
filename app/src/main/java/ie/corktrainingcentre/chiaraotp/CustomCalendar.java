package ie.corktrainingcentre.chiaraotp;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chiara on 29/05/2017.
 */

public class CustomCalendar implements ICalendar {

    int offsetSeconds=15;

    public int getOffsetSeconds() {
        return offsetSeconds;
    }

    public void setOffsetSeconds(int offsetSeconds) {
        this.offsetSeconds = offsetSeconds;
    }


    public Date getDate(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND,offsetSeconds);
        return c.getTime();
    }
    public int getSeconds(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.SECOND,offsetSeconds);
        return c.get(Calendar.SECOND);
    }
}
