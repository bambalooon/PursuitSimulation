package pursuitsimulation.People;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.Simulation.SimulationProcess;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */
public class Runner extends Person {
    private LinkedList<Runner> team;
    private LinkedList<Crossing> path = null;
    private Random rand = new Random();

    public Runner(Crossing current, SimulationProcess process, String name) {
        super(current, process, name);
        team = new LinkedList<Runner>();
    }
    synchronized public void move() {
        curr.calcualteClue(this, process.getTime());
        leaveClue();
        super.move();
    }
    public void addTeammate(Runner r) {
        team.add(r);
    }
    public void leaveClue() {
        if(rand.nextDouble() <= 0.1) { //an arbitrary chance to leave clue (testing purposes)
            Clue clue = new Clue(
                    process.getTime(),
                    this.getCurr(),
                    this.getNext(),
                    this
            );
            process.getClueList().add(clue);

//            System.out.println(this + " has left a clue with timestamp " + clue.getTime().getTimeStamp());
//            if(process.getClueList().getFreshClue().equals(clue))
//                System.out.println("It's the best clue!");
        }
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
