package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Heuristic.BlankHeuristic;
import pursuitsimulation.util.Heuristic.DistanceHeuristic;
import pursuitsimulation.util.PathFinder;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Map;

/**
 * Created by mike on 1/21/14.
 */
public class RadiusEscapeStrategy extends RunningStrategy {
    private final static double avgDistance = 0.000868727; //averade distance between 2 Crossings
    private final static double radiusStep = 10*avgDistance;

    private int stepCounter = 0;
    private PathFinder pathFinder;

    public RadiusEscapeStrategy(SimulationProcess process) {
        super(process);
        pathFinder = new PathFinder(new DistanceHeuristic());
    }

    @Override
    public Crossing getDestination(Person p) {
        Runner r = (Runner) p;

        if(stepCounter++ % 5 == 0)
            r.setPath( findEscapePath( r ) );

        if(r.peekNextPathStep() != null) {
            return r.getNextPathStep();
        }

        System.out.println(r + " is moving randomly...");

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
        return pathFinder.getPath( r.getCurr(), getLowestPresenceNodeInSurrounding(r) );
    }

    private Crossing getLowestPresenceNodeInSurrounding(Runner r) {
        LinkedList<Crossing> surrounding = getSurroundingNodes(r, 50);
        LinkedList<Catcher> catchers = getCatchersInRadius(r);

        ListIterator<Crossing> it = surrounding.listIterator();

        Crossing c, best;
        double presence, bestPresence;

        best = it.next();
        bestPresence = calculatePresence( best, catchers );

        while(it.hasNext()) {
            c = it.next();
            presence = calculatePresence( c, catchers );

            if( presence > bestPresence ) {
                best = c;
                bestPresence = presence;
            }
        }

        return best;
    }

    public LinkedList<Crossing> getSurroundingNodes(Runner r, int limit) {
        PathFinder pf = new PathFinder( new BlankHeuristic() );

        return pf.getSurrounding(r.getCurr(), limit);
    }

    public LinkedList<Catcher> getCatchersInRadius(Runner r) {
        LinkedList<Catcher> catchers = new LinkedList<Catcher>();
        double radius = radiusStep;

        while( catchers.isEmpty() ) {
            for(Catcher c : process.getCatchers()) {
                if( r.getVector().add( c.getVector().negate() ).getLength() <= radius )
                    catchers.add(c);
            }

            radius += radiusStep;
        }
        System.out.println("Found " + catchers.size() + " catchers in radius");
        return catchers;
    }

    private double calculatePresence(Crossing c, LinkedList<Catcher> catchers) {
        ListIterator<Catcher> it = catchers.listIterator();
        double pres, bestPres = getPresence(c, it.next().getCurr());

        while(it.hasNext()) {
            pres = getPresence(c, it.next().getCurr());
            if(pres < bestPres)
                bestPres = pres;
        }

        return bestPres;
    }

    private double getPresence(Crossing c1, Crossing c2) {
        double pres = c2.getVector().add( c1.getVector().negate() ).getLength();

        return pres;
    }
}
