package pursuitsimulation;

import pursuitsimulation.People.Person;
import pursuitsimulation.util.Time;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:50
 * To change this template use File | Settings | File Templates.
 */
public class Clue implements Comparable<Clue> {
    Time time;
    Crossing crossing;


    public Clue(Time t, Crossing crossing) {
        time = t;
        this.crossing = crossing;
    }

    public Crossing getCrossing() { return crossing; }

    public Time getTime() {
        return time;
    }

    public String toString() {
        return new String("Runner is going to: " + crossing);
    }

    public Boolean equals(Clue c) {
        if(c.time == time && c.crossing == crossing)
            return true;

        return false;
    }

    /* reversed comparison in order to sort ClueList by timestamp, descending */
    @Override
    public int compareTo(Clue c) {
        return c.time.getTimeStamp() - time.getTimeStamp();
    }
}
