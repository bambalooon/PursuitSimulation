package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Vector;

import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 11.12.13
 * Time: 00:30
 * To change this template use File | Settings | File Templates.
 */
public class StandardRunningStrategy extends RunningStrategy {
    public StandardRunningStrategy(SimulationProcess process) {
        super(process);
    }
    public Crossing getDestination(Person p) {
        Runner r = (Runner) p;

        Crossing v = r.getCurr();
        LinkedList<Vertex> nhood = v.getNeighbours();

        if(nhood.size() < 2) {
            return (Crossing) nhood.get(0);
        }

        if(nhood.size() == 2 && r.getPrev() != null) {
            nhood.remove(r.getPrev());
            return (Crossing) nhood.get(0);
        }
        return (Crossing) (nhood.get(SimulationProgram.randomGenerator.nextInt(nhood.size() - 1)));
    }
}
