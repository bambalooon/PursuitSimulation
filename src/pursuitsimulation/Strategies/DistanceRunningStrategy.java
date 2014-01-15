package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Heuristic.DistanceHeuristic;
import pursuitsimulation.util.PathFinder;
import pursuitsimulation.util.Position;
import pursuitsimulation.util.Vector;

import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by mike on 1/15/14.
 */
public class DistanceRunningStrategy extends RunningStrategy {
    private PathFinder pathFinder;

    public DistanceRunningStrategy(SimulationProcess process) {
        super(process);
        pathFinder = new PathFinder(new DistanceHeuristic());
    }
    public Crossing getDestination(Person p) {
        Runner r = (Runner) p;

        long startTime = System.nanoTime();
        Crossing escapeNode = findEscapeNode(r);
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Vector computed in " + (elapsedTime/1000000000.0) + "s");

        r.setPath( pathFinder.getPath( r.getCurr(), escapeNode, 100 ) );

        if(r.peekNextPathStep() != null) {
            return r.getNextPathStep();
        }

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

    public Crossing findEscapeNode(Runner r) {
        Vector shortest, v = new Vector();
        ListIterator<Catcher> it = process.getCatchers().listIterator();

        while(it.hasNext()) {
            v.add( computeVectorToCatcher( it.next(), r ) );
        }

        shortest = findVectorToClosestCatcher(r);

        v.negate().add( shortest.negate() );

        return new Crossing(-1, new Position(v.getX(), v.getY()));
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
        Vector v = c.getVector().add( r.getVector().negate() );
        return v;
    }
}
