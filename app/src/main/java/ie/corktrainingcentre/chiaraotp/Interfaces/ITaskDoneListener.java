package ie.corktrainingcentre.chiaraotp.Interfaces;

import ie.corktrainingcentre.chiaraotp.Helpers.SynchroResponse;

/**
 * Created by Chiara on 03/06/2017.
 */

public interface ITaskDoneListener {
    void success(SynchroResponse response);
    void error();
}




















