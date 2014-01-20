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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

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
        r.setPath( findEscapePath( r ) );
        long elapsedTime = System.nanoTime() - startTime;
        System.out.println("Path computed in " + (elapsedTime/1000000000.0) + "s");

        if(r.peekNextPathStep() != null) {
            return r.getNextPathStep();
        }

        System.out.println("Moving randomly...");

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

    private LinkedList<Crossing> findEscapePath(Runner r) {
        return pathFinder.getPath( r.getCurr(), getLowestPresenceNode( r ) );
    }

    private Crossing getLowestPresenceNode(Runner r) {
        Iterator< Map.Entry<Long, Crossing> > it = process.getGraph().getVertexes().entrySet().iterator();
        Crossing c, best;
        double presence, bestPresence;

        best = it.next().getValue();
        bestPresence = calculatePresence( best );

        while(it.hasNext()) {
            c = it.next().getValue();
            presence = calculatePresence( c );

            if( presence > bestPresence ) {
                best = c;
                bestPresence = presence;
            }
        }

        return best;
    }

    private double calculatePresence(Crossing c) {
        ListIterator<Catcher> it = process.getCatchers().listIterator();
        double sum = 0;

        while(it.hasNext())
            sum += getPresence(c, it.next().getCurr());

        return sum;
    }

    private double getPresence(Crossing c1, Crossing c2) {
        double pres = c2.getVector().add( c1.getVector().negate() ).getLength();

        return pres * pres;
    }

    private double getDistance(Runner r, Crossing c) {
        return c.getVector().add( r.getVector().negate() ).getLength();
    }
}
