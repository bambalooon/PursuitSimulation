package pursuitsimulation.People;

import pursuitsimulation.Crossing;
import pursuitsimulation.util.Position;
import pursuitsimulation.Strategies.Strategy;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    private Crossing prev, curr, next;
    private Position pos;
    private int waiting = 0; //0 = not, else num of iteration
    public Person(Crossing current) {
        prev = null;
        curr = current;
        next = null;
        pos = current.getPos();
    }
    public void getDestination(Strategy s) {
        next = s.getDestination(this);
    }
    synchronized public void move() {
        prev = curr;
        curr = next;
        next = null;
        pos = curr.getPos();
    }
    private void wait(int timestamp) {}
    public Position getPos() {
        return pos;
    }
    public Crossing getPrev() {
        return prev;
    }
    public Crossing getCurr() {
        return curr;
    }
    public Crossing getNext() {
        return next;
    }
}
