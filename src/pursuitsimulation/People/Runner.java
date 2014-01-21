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
    public static double lcp = SimulationProcess.LCP_INIT;
    public static double gcp = SimulationProcess.GCP_INIT;
    private LinkedList<Runner> team;
    private Random rand = new Random();

    public static void changeLCP(double nLCP) {
        Runner.lcp = nLCP;
    }

    public static void changeGCP(double nGCP) {
        Runner.gcp = nGCP;
    }

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
        if(rand.nextDouble() <= Runner.gcp) {
            process.setClue(new Clue(
                    process.getTime(),
                    this.getCurr(),
                    this.getNext(),
                    this
            ));
        }
    }
}
