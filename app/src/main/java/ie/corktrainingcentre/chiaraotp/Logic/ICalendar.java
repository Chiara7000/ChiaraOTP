package ie.corktrainingcentre.chiaraotp.Logic;

import java.util.Date;

/**
 * Created by Chiara on 29/05/2017.
 */

public interface ICalendar {

    public void setOffsetSeconds(int offsetSeconds);
    public Date getDate();
    public int getSeconds();
}
