package ie.corktrainingcentre.chiaraotp.Helpers;

/**
 * Created by Ciro on 03/06/2017.
 */

public class SynchroResponse {

    int id, offset;

    public int getId() {
        return id;
    }

    public int getOffset() {
        return offset;
    }

    public SynchroResponse(int id, int offset)
    {
        this.id= id;
        this.offset = offset;
    }
}
