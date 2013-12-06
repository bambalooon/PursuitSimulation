package pursuitsimulation;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    private Crossing prev, next=null;
    private Position pos;
    private boolean waiting = false;
    public Person(Crossing current) {
        prev = current;
        pos = current.getPos();
    }
    public Crossing getNextDestination(Strategy s) { //private?
        return new Crossing(0, 0);
    }
    void move() {}
    void wait(int timestamp) {}

}
