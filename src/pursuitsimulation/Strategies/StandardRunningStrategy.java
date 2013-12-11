package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.Vertex;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 11.12.13
 * Time: 00:30
 * To change this template use File | Settings | File Templates.
 */
public class StandardRunningStrategy implements RunningStrategy {
    public Crossing getDestination(Person r) {
        r = (Runner) r;
        Crossing v = r.getCurr();
        LinkedList<Vertex> nhood = v.getNeighbours();
        nhood.remove(r.getPrev());
        if(nhood.size()==0) {
            return r.getPrev();
        }
        return (Crossing) (nhood.get((int) (Math.random()*(nhood.size()-1))));
    }
}
