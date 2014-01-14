package pursuitsimulation.util.Heuristic;

import pursuitsimulation.Crossing;

/**
 * Created by mike on 1/14/14.
 */
public class CrowsDistanceHeuristic implements Heuristic {
    @Override
    public double calculateHScore(Crossing from, Crossing to) {
        return from.calcualteDistance(to);
    }
}
