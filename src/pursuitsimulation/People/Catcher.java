package pursuitsimulation.People;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.Simulation.SimulationProcess;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by mike on 12/15/13.
 */
public class Catcher extends Person {
    private LinkedList<Catcher> team;
    private LinkedList<Crossing> path = null;

    public Catcher(Crossing current, SimulationProcess process, String name) {
        super(current, process, name);
        team = new LinkedList<Catcher>();
    }

    synchronized public void move() {
        super.move();

        if( getCurr().equals( process.getClue().getDestination() ) ) {
            process.clearClue();
        }

        gatherClues();
    }

    public void gatherClues() {
        ListIterator<Clue> it = curr.look().listIterator();

        while(it.hasNext())
            process.setClue(it.next());
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
