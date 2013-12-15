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

    public Catcher(Crossing current, SimulationProcess process) {
        super(current, process);
        team = new LinkedList<Catcher>();
    }

    synchronized public void move() {
        super.move();
        gatherClues();
    }

    public void gatherClues() {
        ListIterator<Clue> it = curr.look().listIterator();
        Clue c = null;

        while(it.hasNext()) {
            c = it.next();
            process.getClueList((Runner)c.getPerson()).add(c);
        }
    }

    public void addTeammate(Catcher c) {
        team.add(c);
    }
}
