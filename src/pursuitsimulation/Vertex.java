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
    private LinkedList<Vertex> neighbours;
    public Vertex() {
        neighbours = new LinkedList<Vertex>();
    }
    public LinkedList<Vertex> getNeighbours() {
        return neighbours;
    }
    public void addNeighbour(Vertex v) {
        neighbours.add(v);
    }
}
