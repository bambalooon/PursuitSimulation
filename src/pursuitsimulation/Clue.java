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
public class Clue {
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

    public Time getTime() {
        return time;
    }

    public String toString() {
        return new String("Jest w:" + currentLocation.getID() + "; Idzie do: " + destination.getID());
    }
}
