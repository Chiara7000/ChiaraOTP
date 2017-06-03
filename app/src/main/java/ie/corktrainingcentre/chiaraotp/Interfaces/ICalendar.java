package ie.corktrainingcentre.chiaraotp.Interfaces;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by Chiara on 29/05/2017.
 */

public interface ICalendar {

    public void setOffsetMilliseconds(int offsetSeconds);
    public Calendar getCurrentCalendar();
}
