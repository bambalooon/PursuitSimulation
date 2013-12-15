package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.Vertex;

import java.util.LinkedList;
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
    public Crossing getDestination(Person r) {
        Crossing v = r.getCurr();
        LinkedList<Vertex> nhood = v.getNeighbours();
        /*
        if(nhood.size()==0)
            System.out.println("R: ślepy zaułek: "+v.getID());
        nhood.remove(r.getPrev());
        if(nhood.size()==0) {
            System.out.println("R: powrót");
            return r.getPrev();
        }
        if(nhood.size()==1)
            return (Crossing) nhood.get(0);
            */

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
