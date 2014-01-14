package pursuitsimulation.util;

import pursuitsimulation.Crossing;

/**
 * Created by mike on 12/16/13.
 */
public class CrossingStructure implements Comparable<CrossingStructure> {
    private Crossing crossing;
    private Crossing parent;
    private double gScore;
    private double hScore;
    private int parentStep;

    public CrossingStructure(Crossing crossing) {
        this.crossing = crossing;
    }

    public CrossingStructure(Crossing crossing, Crossing parent) {
        this.crossing = crossing;
        this.parent = parent;
    }

    public Crossing getCrossing() { return crossing; }
    public CrossingStructure setCrossing(Crossing crossing) { this.crossing = crossing; return this; }

    public Crossing getParent() {
        return parent;
    }
    public CrossingStructure setParent(Crossing parent) { this.parent = parent; return this; }

    public double getgScore() {
        return gScore;
    }
    public CrossingStructure setgScore(double gScore) { this.gScore = gScore; return this; }

    public double gethScore() { return hScore; }
    public CrossingStructure sethScore(double hScore) { this.hScore = hScore; return this; }

    public double getfScore() { return getgScore() + gethScore(); }

    public int getParentStep() {
        return parentStep;
    }

    public CrossingStructure setParentStep(int parentStep) {
        this.parentStep = parentStep;

        return this;
    }

    public int compareTo(CrossingStructure C) {
        if(getfScore() < C.getfScore())
            return -1;
        if(getfScore() > C.getfScore())
            return 1;
        return 0;
    }
}
