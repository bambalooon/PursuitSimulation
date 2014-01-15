package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Vector;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by mike on 1/15/14.
 */
public class DistanceRunningStrategy extends RunningStrategy {
    public DistanceRunningStrategy(SimulationProcess process) {
        super(process);
    }
    public Crossing getDestination(Person p) {
        Runner r = (Runner) p;

        long startTime = System.nanoTime();
        findEscapeNode(r);
        long elapsedTime = System.nanoTime() - startTime;
//        System.out.println("Vector computed in " + (elapsedTime/1000000000.0) + "s");

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
