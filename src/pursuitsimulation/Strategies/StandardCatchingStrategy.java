package pursuitsimulation.Strategies;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Heuristic.CrowsDistanceHeuristic;
import pursuitsimulation.util.PathFinder;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 11.12.13
 * Time: 00:29
 * To change this template use File | Settings | File Templates.
 */
public class StandardCatchingStrategy extends CatchingStrategy {
    public StandardCatchingStrategy(SimulationProcess process) {
        super(process);
    }

    public Crossing getDestination(Person p) {
        Catcher c = (Catcher) p;
        Clue bestClue = null;
        // checks for any known clues and if there are any - chooses the closest suspected position
            try {
                Runner runner = process.getRunner();
                Clue clue = process.getClueList().getFreshClue();

                if(clue != null) {
                    bestClue = clue;
                }
            } catch(NullPointerException e) {}


        if( bestClue != null && !bestClue.getDestination().equals( c.getDestination() ) ) {
            PathFinder pathFinder = new PathFinder( new CrowsDistanceHeuristic());
            long timeStart;
            long timeElapsed;

            timeStart = System.nanoTime();

            c.setPath( pathFinder.getPath(c.getCurr(), bestClue.getDestination()) );
            timeElapsed = System.nanoTime() - timeStart;

            System.out.println("A* time for " + c + ": " + (timeElapsed/1000000000.0) + "s");
        }

        if(c.peekNextPathStep() != null) {
            return c.getNextPathStep();
        }

        /* if there's no particular Crossing we want to get to - choose way at random */

        Crossing v = c.getCurr();
        LinkedList<Vertex> nhood = v.getNeighbours();


        if(nhood.size() < 2) {
            return (Crossing) nhood.get(0);
        }

        if(nhood.size() == 2 && c.getPrev() != null) {
            nhood.remove(c.getPrev());
            return (Crossing) nhood.get(0);
        }
        //return (Crossing) (nhood.get(nhood.size()>1?1:0));
        return (Crossing) (nhood.get(SimulationProgram.randomGenerator.nextInt(nhood.size() - 1)));
    }
}
