package pursuitsimulation;

import pursuitsimulation.People.Person;
import pursuitsimulation.util.ClueList;
import pursuitsimulation.util.Position;
import pursuitsimulation.util.Time;

import java.util.LinkedList;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class Crossing extends Vertex {
    private ClueList clues = new ClueList();
    private double traceProbability;
    private double callProbability;
    private double spottingProbability;
    private Random rand = new Random();
    //-------------------------------------------------------------------------------
    public Crossing(long ID, Position pos) {
        super(ID, pos);    //Position=lat+lon
        traceProbability = 0.2;
        callProbability = 0.2;
        spottingProbability = 0.2;
    }

    /* calculates chance of leaving clue and, if leaving, adds it to the list */
    public void calcualteClue(Person person, Time time) {
        if(rand.nextDouble() <= getSpottingProbability() ||    // person has been spotted and reported
          (rand.nextDouble() <= getCallProbability() && rand.nextDouble() <= getTraceProbability())) { // person has called and been traced

            leaveTrace(new Clue(
                time,
                this,
                person.getNext(),
                person
            ));
        }
    }

    //get
    public ClueList look() { return clues; }
    //set
    public void leaveTrace(Clue c) { clues.add(c); }
    //clear
    public void clearClues() { clues.clear(); }

    public double getTraceProbability() {
        return traceProbability;
    }

    public double getCallProbability() {
        return callProbability;
    }

    public double getSpottingProbability() {
        return spottingProbability;
    }

    public long getId() { return ID; }

    public boolean equals(Crossing crossing) {
        if( crossing == null )
            return false;

        return ID == crossing.getId();
    }
}

