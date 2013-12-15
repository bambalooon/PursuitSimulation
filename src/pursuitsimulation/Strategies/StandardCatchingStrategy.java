package pursuitsimulation.Strategies;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.Strategies.CatchingStrategy;
import pursuitsimulation.Vertex;

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

    public Crossing getDestination(Person c) {
        Clue bestClue = null;
        for(Runner runner : process.getRunners()) { // loop checks for any known clues and if there are any - chooses the closest suspected position
            try {
                Clue clue = process.getClueList(runner).getFreshClue();

                if(clue != null ) {
                    if(bestClue == null) {
                        bestClue = clue;
                    } else if(clue.getDestination().calcualteDistance(c.getCurr()) < bestClue.getDestination().calcualteDistance(c.getCurr())) {
                        bestClue = clue;
                    }
                }
            } catch(NullPointerException e) {}
        }

        if(bestClue != null) {//TODO: funkcja znajdujaca droge do zadanego wierzcholka i zwracajaca aktualnego sasiada prowadzacego do celu
            return bestClue.getDestination();
        }

        Crossing v = c.getCurr();
        LinkedList<Vertex> nhood = v.getNeighbours();
        /*if(nhood.size()==0)
            System.out.println("C: ślepy zaułek: "+v.getID());
        nhood.remove(c.getPrev());
        if(nhood.size()==0) {
            System.out.println("C: powrót");
            return c.getPrev();
        }*/

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
