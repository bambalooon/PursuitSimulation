package pursuitsimulation;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:48
 * To change this template use File | Settings | File Templates.
 */
public class Crossing {
    private Position pos;
    private LinkedList<Clue> clues;
    public Crossing(double x, double y) { pos = new Position(x, y); clues = new LinkedList<Clue>(); }    //Position?
    public Position getPos() { return pos; }
    public LinkedList<Clue> look() { return clues; }
    public void leaveTrace(Clue c) { clues.add(c); }

}

