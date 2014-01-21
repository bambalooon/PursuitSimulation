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
    private static int clueCounter;
    private int id;
    Time time;
    Crossing crossing;

    public Clue(Time t, Crossing crossing) {
        time = t;
        this.crossing = crossing;
        id = clueCounter++;
        System.out.println(this);
    }

    public Crossing getCrossing() { return crossing; }

    public Time getTime() {
        return time;
    }

    public String toString() {
        return new String("Clue #"+id +": Runner is going to: " + crossing);
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
