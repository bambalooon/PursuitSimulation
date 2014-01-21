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
public class
        Clue implements Comparable<Clue> {
    Time time;
    Crossing currentLocation;
    Crossing destination;
    Person person;


    public Clue(Time t, Crossing current, Crossing next, Person p) {
        time = t;
        currentLocation = current;
        destination = next;
        person = p;
    }

    public Person getPerson() {
        return person;
    }

    public Crossing getDestination() { return destination; }
    public Crossing getCurrent() { return currentLocation; }

    public Time getTime() {
        return time;
    }

    public String toString() {
        return new String("Jest w:" + currentLocation.getID() + "; Idzie do: " + destination.getID());
    }

    public Boolean equals(Clue c) {
        if(c.time == time && c.currentLocation == currentLocation && c.destination == destination && c.person == person)
            return true;

        return false;
    }

    /* reversed comparison in order to sort ClueList by timestamp, descending */
    @Override
    public int compareTo(Clue c) {
        return c.time.getTimeStamp() - time.getTimeStamp();
    }
}
