package pursuitsimulation.Simulation;

import pursuitsimulation.Crossing;
import pursuitsimulation.util.Graph;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Heuristic.BlankHeuristic;
import pursuitsimulation.util.PathFinder;

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
    public Map<Long, Crossing> getVertexes() {return vertexes; }
    public void setGraph(Map<Long,Crossing> graph){
        vertexes = eliminateIslands(graph);
        System.out.println("Graph size: " + vertexes.size());
    }
    public Crossing getRandomVertex() {
        return (Crossing) (vertexes.values().toArray()[SimulationProgram.randomGenerator.nextInt(vertexes.size()-1)]);
    }
    public int getGraphSize() { return vertexes.size(); }
    public void cleanGraph() { //delete Vertexes without neighbours
        LinkedList<Long> ids = new LinkedList<Long>();
        for(Map.Entry<Long, Crossing> entry : vertexes.entrySet()) {
            Long ID = (Long) entry.getKey();
            Crossing c = (Crossing) entry.getValue();
            if(c.getNeighbours().size()==0) {
                ids.add(ID);
                for(Vertex v : c.getInNeighbours()) {
                    v.rmNeighbour(c);
                }
            }
        }
        int size = ids.size();
        for(Long id : ids) {
            vertexes.remove(id);
        }
        if(size!=0)
            cleanGraph();
        //System.out.println(vertexes.size());
    }

    public Map<Long,Crossing> eliminateIslands(Map<Long,Crossing> graph) {
        PathFinder pf = new PathFinder( new BlankHeuristic() );
        Crossing c = null;

        for(Map.Entry<Long, Crossing> entry : graph.entrySet()) {
            if(entry.getKey() == 42876815) {
                return pf.getGraphComponentAroundCrossing( entry.getValue() );
            }
        }

        return null;
    }
}
