package ie.corktrainingcentre.chiaraotp.Logic;

import java.util.Calendar;
import java.util.Date;

import ie.corktrainingcentre.chiaraotp.Interfaces.ICalendar;

/**
 * Created by Chiara on 29/05/2017.
 */

public class CustomCalendar implements ICalendar {

    int offsetMilliseconds=15;

    public int getOffsetMilliseconds() {
        return offsetMilliseconds;
    }

    public void setOffsetMilliseconds(int offsetSeconds) {
        this.offsetMilliseconds = offsetSeconds;
    }

    public Calendar getCurrentCalendar(){
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MILLISECOND,offsetMilliseconds);
        return c;
    }
}
