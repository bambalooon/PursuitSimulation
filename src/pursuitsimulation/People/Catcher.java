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

    public Catcher(Crossing current, SimulationProcess process, String name) {
        super(current, process, name);
        team = new LinkedList<Catcher>();
    }

    synchronized public void move() {
        super.move();

        if( getCurr().equals( process.getClue().getCrossing() ) ) {
            process.clearClue();
        }

        gatherClues();
    }

    public void gatherClues() {
        ListIterator<Clue> it = curr.look().listIterator();

        while(it.hasNext())
            process.setClue(it.next());
    }
}
