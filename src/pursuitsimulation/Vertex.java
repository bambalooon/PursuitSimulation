package pursuitsimulation;

import pursuitsimulation.util.Position;

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
    private LinkedList<Vertex> outNeighbours;
    private LinkedList<Vertex> inNeighbours;
    //---------------------------------------------------
    public Vertex(long ID, Position pos) {
        outNeighbours = new LinkedList<Vertex>();
        inNeighbours = new LinkedList<Vertex>();
        this.ID = ID;
        this.pos = pos;
    }

    public void addNeighbour(Vertex v) {
        outNeighbours.add(v);
    }
    public void addInNeighbour(Vertex v) {
        inNeighbours.add(v);
    }
    public void rmNeighbour(Vertex v) {
        System.out.print(ID+": "+outNeighbours.size()+"-");
        outNeighbours.remove(v);
        System.out.println(outNeighbours.size());
    }
    public long getID() {
        return ID;
    }
    public Position getPos() {
        return pos;
    }
    public LinkedList<Vertex> getNeighbours() {
        return outNeighbours;
    }
    public LinkedList<Vertex> getInNeighbours() {
        return inNeighbours;
    }
}
