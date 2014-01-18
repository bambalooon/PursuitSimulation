package pursuitsimulation.People;

import pursuitsimulation.Crossing;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.util.Position;
import pursuitsimulation.Strategies.Strategy;
import pursuitsimulation.util.Vector;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    protected SimulationProcess process;
    protected Crossing prev, curr, next;
    protected LinkedList<Crossing> route;
    protected Position pos;
    protected int waiting = 0; //0 = not, else num of iteration
    protected String name;

    public Person(Crossing current, SimulationProcess process, String name) {
        this.process = process;
        this.name = name;
        prev = null;
        curr = current;
        next = null;
        pos = current.getPos();
        route = new LinkedList<Crossing>();
        route.add(curr);
    }
    public void getDestination(Strategy s) {
        next = s.getDestination(this);
    }
    synchronized public void move() {
        prev = curr;
        curr = next;
        next = null;
        pos = curr.getPos();
        route.add(curr);
    }
    protected void wait(int timestamp) {}
    public Vector getVector() { return new Vector(pos.getX(), pos.getY()); }
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
    public String toString() { return name; }
}
