package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
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
public class StandardCatchingStrategy implements CatchingStrategy {
    public Crossing getDestination(Person c) {
        c = (Catcher) c;
        Crossing v = c.getCurr();
        LinkedList<Vertex> nhood = v.getNeighbours();
        nhood.remove(c.getPrev());
        if(nhood.size()==0) {
            return c.getPrev();
        }
        return (Crossing) (nhood.get((int) (Math.random()*(nhood.size()-1))));
    }
}
