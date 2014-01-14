package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Heuristic.DistanceHeuristic;
import pursuitsimulation.util.PathFinder;
import pursuitsimulation.util.Position;
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

        PathFinder pf = new PathFinder(new DistanceHeuristic());
        //r.setPath( pf.getPath( r.getCurr(), findEscapeNode(r), 100 ) );

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

        return new Crossing(0, new Position(v.getX(), v.getY()));
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
