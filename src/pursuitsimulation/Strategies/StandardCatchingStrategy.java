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
    PathFinder pathFinder;

    public StandardCatchingStrategy(SimulationProcess process) {
        super(process);
        pathFinder = new PathFinder( new CrowsDistanceHeuristic());
    }

    public Crossing getDestination(Person p) {
        Catcher c = (Catcher) p;
        Clue clue = process.getClue();

        if( clue == null ) {
            if(c.peekNextPathStep() == null) {
                System.out.println(c + " is going to random Crossing...");
                c.setPath( pathFinder.getPath(c.getCurr(), process.getGraph().getRandomVertex()) );
            }
        } else if( !clue.getDestination().equals( c.getDestination() ) ) {
            c.setPath( pathFinder.getPath(c.getCurr(), clue.getDestination()) );
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
