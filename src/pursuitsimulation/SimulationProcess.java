package pursuitsimulation;

import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Strategies.CatchingStrategy;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.util.Time;

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
public class SimulationProcess extends Thread {
    private Time time = new Time();
    private SimulationGraph graph = new SimulationGraph();
    private LinkedList<Catcher> catchers = new LinkedList<Catcher>();
    private LinkedList<Runner> runners = new LinkedList<Runner>();
    private Map<Catcher, CatchingStrategy> cStrategies = new HashMap<Catcher, CatchingStrategy>();
    private Map<Runner, RunningStrategy> rStrategies = new HashMap<Runner, RunningStrategy>();
    private boolean running = false;
    void setGraph(Map<Long, Crossing> graph) {
        this.graph.setGraph(graph);
//        for(int i=0; i<10; i++) {
//            Catcher c = new Catcher(this.graph.getRandomVertex());
//            System.out.println("C"+i+": "+c.getPos().getX()+"x"+c.getPos().getY());
//            catchers.add(c);
//        }
//        for(int i=0; i<10; i++) {
//            Runner r = new Runner(this.graph.getRandomVertex());
//            System.out.println("R" + i + ": " + r.getPos().getX() + "x" + r.getPos().getY());
//            runners.add(r);
//        }
    }
    void addCatcher(CatchingStrategy s) {
        Catcher c = new Catcher(this.graph.getRandomVertex()); //some better way to do it..
        catchers.add(c);
        cStrategies.put(c, s);
    }
    void addRunner(RunningStrategy s) {
        Runner r = new Runner(this.graph.getRandomVertex()); //some better way to do it..
        runners.add(r);
        rStrategies.put(r, s);
    }
    LinkedList<Catcher> getCatchers() {
        return catchers;
    }
    LinkedList<Runner> getRunners() {
        return runners;
    }

    public void run() { //5s miedzy iteracjami - przeskok z wezla do wezla
        running = true;

        while(true) {
            try {
                sleep(Time.timeInterval);
            } catch(InterruptedException e) {
                //handle ex
            }
            time.move();
            for(Runner r : runners)
                r.getDestination(rStrategies.get(r));
            for(Catcher c : catchers)
                c.getDestination(cStrategies.get(c));
            for(Runner r : runners)
                r.move();
            for(Catcher c : catchers)
                c.move();
        }
    }
}
