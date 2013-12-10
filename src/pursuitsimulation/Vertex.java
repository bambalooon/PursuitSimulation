package pursuitsimulation;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 01:09
 * To change this template use File | Settings | File Templates.
 */
public class Vertex {
    private long ID;
    private Position pos;
    private LinkedList<Vertex> neighbours;
    //---------------------------------------------------
    public Vertex(long ID, Position pos) {
        neighbours = new LinkedList<Vertex>();
        this.ID = ID;
        this.pos = pos;
    }

    public void addNeighbour(Vertex v) {
        neighbours.add(v);
    }
    public long getID() {
        return ID;
    }
    public Position getPos() {
        return pos;
    }
    public LinkedList<Vertex> getNeighbours() {
        return neighbours;
    }
}
