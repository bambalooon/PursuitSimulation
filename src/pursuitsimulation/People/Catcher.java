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
        gatherClues();

        Clue clue = process.getClue();

        if( clue != null && getCurr().equals( clue.getCrossing() ) ) {
            process.clearClue();
        }
    }

    public void gatherClues() {
        Clue clue = curr.getClue();

        if( clue != null ) {
            process.setClue( clue );
        }
    }
}
