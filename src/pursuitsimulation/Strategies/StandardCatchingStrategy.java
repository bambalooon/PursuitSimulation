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
import java.util.Map;

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

        if( clue == null && c.peekNextPathStep() == null ) {
            System.out.println(c + " is going to random Crossing...");
            c.setPath( pathFinder.getPath(c.getCurr(), process.getGraph().getRandomVertex()) );
        } else if( clue != null && !clue.getCrossing().equals( c.getDestination() ) ) {
            System.out.println(c + " is going to clue");
            c.setPath( pathFinder.getPath(c.getCurr(), clue.getCrossing()) );
        }

//        if(c.peekNextPathStep() == null) {
//            findCrossingInGraph(clue.getCrossing());
//            System.out.println(c + " next step: NULL");
//            System.out.println("curr: " + c.getCurr());
//            System.out.println("clue: " + clue.getCrossing());
//        }

        if(c.peekNextPathStep() == null)
            return c.getNextPathStep();

        /* if there's no particular Crossing we want to get to - choose way at random */
        System.out.println(c + " is moving randomly...");

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

    private void findCrossingInGraph(Crossing c) {
        for(Map.Entry<Long, Crossing> e : process.getGraph().getVertexes().entrySet()) {
            if(e.getValue().equals( c )) {
                System.out.println("Found Crossing in graph");
                return;
            }
        }

        System.out.println("Crossing not found in graph");
    }
}
