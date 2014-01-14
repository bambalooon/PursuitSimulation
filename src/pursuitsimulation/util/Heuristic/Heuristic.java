package pursuitsimulation.util.Heuristic;

import pursuitsimulation.Crossing;

/**
 * Created by mike on 1/14/14.
 */
public interface Heuristic {
    public double calculateHScore(Crossing from, Crossing to);
}
