package pursuitsimulation;

import pursuitsimulation.util.Position;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class Crossing extends Vertex {
    private LinkedList<Clue> clues = new LinkedList<Clue>();
    private double traceProbability;
    private double callProbability;
    //-------------------------------------------------------------------------------
    public Crossing(long ID, Position pos) { super(ID, pos); }    //Position=lat+lon
    //get
    public LinkedList<Clue> look() { return clues; }
    //set
    public void leaveTrace(Clue c) { clues.add(c); }



}

