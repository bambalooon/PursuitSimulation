package pursuitsimulation.util.Heuristic;

import pursuitsimulation.Crossing;
import pursuitsimulation.util.Vector;

/**
 * Created by mike on 1/14/14.
 */
public class DistanceHeuristic implements Heuristic {
    @Override
    public double calculateHScore(Crossing from, Crossing to) {
        double distance = new Vector(from).add( new Vector(to).negate() ).getLength();

        return distance * distance * distance;
    }
}
