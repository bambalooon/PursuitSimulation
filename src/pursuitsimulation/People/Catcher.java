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
        gatherClues();
    }

    public void gatherClues() {
        ListIterator<Clue> it = curr.look().listIterator();
        Clue c = null;

        while(it.hasNext()) {
            c = it.next();
            process.getClueList().add(c);

//            System.out.println(this + " has found a clue with timestamp " + c.getTime().getTimeStamp());
//            if(process.getClueList().getFreshClue().equals(c))
//                System.out.println("It's the best clue!");
        }
    }

    public void addTeammate(Catcher c) {
        team.add(c);
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
