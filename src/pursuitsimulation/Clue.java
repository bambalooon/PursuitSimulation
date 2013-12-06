package pursuitsimulation;

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
    Clue(Time t, Crossing c1, Crossing c2, Person p) {
        time = t;
        currentLocation = c1;
        destination = c2;
        person = p;
    }

}
