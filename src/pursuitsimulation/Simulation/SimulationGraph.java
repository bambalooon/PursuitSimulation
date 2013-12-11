package pursuitsimulation.Simulation;

import pursuitsimulation.Crossing;
import pursuitsimulation.util.Graph;
import pursuitsimulation.Vertex;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 22:04
 * To change this template use File | Settings | File Templates.
 */
public class SimulationGraph implements Graph {
    Map<Long, Crossing> vertexes = new HashMap<Long, Crossing>();
    public void addVertex(Vertex v) {
        Crossing c = (Crossing) v; //jesli to nie jest Crossing to error bo spodziewamy sie crossinga!
        vertexes.put(c.getID(), c);
    }
    public void setGraph(Map<Long,Crossing> graph) {
        vertexes = graph;
    }
    public Crossing getRandomVertex() {
         return (Crossing) (vertexes.values().toArray()[(int) (Math.random()*(vertexes.size()-1))]);
    }
    public void cleanGraph() { //delete Vertexes without neighbours
        LinkedList<Long> ids = new LinkedList<Long>();
        for(Map.Entry<Long, Crossing> entry : vertexes.entrySet()) {
            Long ID = (Long) entry.getKey();
            Crossing c = (Crossing) entry.getValue();
            if(c.getNeighbours().size()==0) {
                ids.add(ID);
            }
        }
        System.out.println(ids.size());
        for(Long id : ids) {
            vertexes.remove(id);
        }
    }
}
