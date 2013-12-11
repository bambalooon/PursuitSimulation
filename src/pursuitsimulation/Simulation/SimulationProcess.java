package pursuitsimulation.Simulation;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationGraph;
import pursuitsimulation.Strategies.CatchingStrategy;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.util.Time;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
    private SimulationProgram main;
    private Time time = new Time();
    private SimulationGraph graph = new SimulationGraph();
    private LinkedList<Catcher> catchers = new LinkedList<Catcher>();
    private LinkedList<Runner> runners = new LinkedList<Runner>();
    private Map<Catcher, CatchingStrategy> cStrategies = new HashMap<Catcher, CatchingStrategy>();
    private Map<Runner, RunningStrategy> rStrategies = new HashMap<Runner, RunningStrategy>();
    private boolean running = false;
    private Timer timer;
    SimulationProcess(SimulationProgram program) {
        main = program;
    }
    void setGraph(Map<Long, Crossing> graph) {
        this.graph.setGraph(graph);
        this.graph.cleanGraph();
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

    public void setSimulationTimer() { //5s miedzy iteracjami - przeskok z wezla do wezla
        if(timer!=null) {
            timer.stop();
        }
        timer = new Timer(100 * Time.timeInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                time.move();
                for(Runner r : runners)
                    r.getDestination(rStrategies.get(r));
                for(Catcher c : catchers)
                    c.getDestination(cStrategies.get(c));
                for(Runner r : runners)
                    r.move();
                for(Catcher c : catchers)
                    c.move();
                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        main.updateGuiMap();
                    }
                });
            }
        });
    }
    public void simulationStart() {
        timer.start();
    }
    public void simulationStop() {
        timer.stop();
    }
}
