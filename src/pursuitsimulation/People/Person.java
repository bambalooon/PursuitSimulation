package pursuitsimulation.People;

import javafx.util.Pair;
import pursuitsimulation.Crossing;
import pursuitsimulation.GUI.SimulationPlayer;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.util.Position;
import pursuitsimulation.Strategies.Strategy;
import pursuitsimulation.util.Vector;

import java.util.LinkedList;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    protected SimulationProcess process;
    private LinkedList<Crossing> path = null;
    protected Crossing prev, curr, next;
    protected LinkedList< Pair<Crossing, Crossing> > route;
    protected Lock lock = new ReentrantLock();
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
//        route = new LinkedList<Crossing>();
        //Key = Current, Value = Destination
        route = new LinkedList< Pair<Crossing, Crossing> >();
        SimulationPlayer.lock.lock();
        route.add( new Pair(curr, getDestination()) );
        SimulationPlayer.lock.unlock();
    }
    public void getDestination(Strategy s) {
        next = s.getDestination(this);
    }
    public void move() {
        prev = curr;
        curr = next;
        next = null;
        pos = curr.getPos();
        SimulationPlayer.lock.lock();
        route.add( new Pair(curr, getDestination()) );
        SimulationPlayer.lock.unlock();
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
    public LinkedList<Pair<Crossing,Crossing>> getRoute() {
        return route;
    }

    public void setPath(LinkedList<Crossing> path) {
        this.path = path;
    }

    public Crossing getNextPathStep() {
        if(path == null)
            return null;

        return path.poll();
    }

    public Crossing peekNextPathStep() {
        if(path == null)
            return null;

        return path.peekFirst();
    }

    public Crossing getDestination() {
        if(path == null)
            return null;

        return path.peekLast();
    }
}
