package pursuitsimulation;

import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 21:58
 * To change this template use File | Settings | File Templates.
 */
public class SimulationProcess {
    private Time time = new Time();
    private SimulationGraph graph = new SimulationGraph();
    private LinkedList<Catcher> catchers = new LinkedList<Catcher>();
    private LinkedList<Runner> runners = new LinkedList<Runner>();
    private Map<Catcher, CatchingStategy> cStrategies = new HashMap<Catcher, CatchingStategy>();
    private Map<Runner, RunningStrategy> rStrategies = new HashMap<Runner, RunningStrategy>();
    void setGraph(Map<Long, Crossing> graph) {
        this.graph.setGraph(graph);
    }

}
