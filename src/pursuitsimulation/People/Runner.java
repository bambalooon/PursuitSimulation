package pursuitsimulation.People;

import pursuitsimulation.Crossing;
import pursuitsimulation.Simulation.SimulationProcess;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 23:09
 * To change this template use File | Settings | File Templates.
 */
public class Runner extends Person {
    private LinkedList<Runner> team;
    //database; ????
    public Runner(Crossing current, SimulationProcess process) {
        super(current, process);
        team = new LinkedList<Runner>();
    }
    synchronized public void move() {
        curr.calcualteClue(this, process.getTime());
        super.move();
    }
    public void addTeammate(Runner r) {
        team.add(r);
    }
}
