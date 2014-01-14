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

        findEscapeNode(r);

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

    public void findEscapeNode(Runner r) {
        Vector shortest, v = new Vector();
        ListIterator<Catcher> it = process.getCatchers().listIterator();

        while(it.hasNext()) {
            v.add( computeVectorToCatcher( it.next(), r ) );
        }

        shortest = findVectorToClosestCatcher(r);

        v.negate().add( shortest.negate() );

        System.out.println("Escape vector: " + v);
    }

    public Vector findVectorToClosestCatcher(Runner r) {
        ListIterator<Catcher> it = process.getCatchers().listIterator();

        Vector v, shortest = computeVectorToCatcher( it.next(), r );

        while(it.hasNext()) {
            v = computeVectorToCatcher( it.next(), r );

            if(v.getLength() < shortest.getLength())
                shortest = v;
        }

        return shortest;
    }

    public Vector computeVectorToCatcher(Catcher c, Runner r) {
        /* Person::getVector returns coordinates of a person packed in Vector class for convinience */
        return c.getVector().add( r.getVector().negate() );
    }
}
