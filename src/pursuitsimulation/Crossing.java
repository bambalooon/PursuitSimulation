package pursuitsimulation;

import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.util.ClueList;
import pursuitsimulation.util.Position;
import pursuitsimulation.util.Time;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class Crossing extends Vertex {
    private ClueList clues = new ClueList();
    private Random rand = new Random();
    //-------------------------------------------------------------------------------
    public Crossing(long ID, Position pos) {
        super(ID, pos);    //Position=lat+lon
    }

    public void calcualteClue(Person person, Time time) {
        if(rand.nextDouble() <= Runner.lcp) {
            System.out.println("Left clue on Crossing");
            leaveTrace(new Clue(
                time,
                person.getCurr().cloneForClue()
            ));
        }
    }

    //get
    public ClueList look() { return clues; }
    //set
    public void leaveTrace(Clue c) { clues.add(c); }

    public long getId() { return ID; }

    public boolean equals(Crossing crossing) {
        if( crossing == null )
            return false;

        return ID == crossing.getId();
    }

    public Crossing cloneForClue() {
        return new Crossing(ID, pos);
    }
}

