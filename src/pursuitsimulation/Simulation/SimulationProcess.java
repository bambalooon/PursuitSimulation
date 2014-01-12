package pursuitsimulation.Simulation;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Person;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationGraph;
import pursuitsimulation.Strategies.CatchingStrategy;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.util.Astar;
import pursuitsimulation.util.ClueList;
import pursuitsimulation.util.Time;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedList;
import java.util.Map;
import java.util.HashMap;
import java.util.Random;

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
    private Runner runner;
    private ClueList clueList = new ClueList();
    private Map<Catcher, CatchingStrategy> cStrategies = new HashMap<Catcher, CatchingStrategy>();
    private Map<Runner, RunningStrategy> rStrategies = new HashMap<Runner, RunningStrategy>();
    private boolean running = false;
    private Timer timer;
    private Astar pathAlgorithm = new Astar();

    SimulationProcess(SimulationProgram program) {
        main = program;
    }
    void setGraph(Map<Long, Crossing> graph) {
        this.graph.setGraph(graph);
        this.graph.cleanGraph();
    }
    public SimulationGraph getGraph() { return graph; }
    void addCatcher(CatchingStrategy s, String name) {
        Catcher c = new Catcher(this.graph.getRandomVertex(), this, name); //some better way to do it..
        catchers.add(c);
        cStrategies.put(c, s);
    }
    void setRunner(RunningStrategy s, String name) {
        runner = new Runner(this.graph.getRandomVertex(), this, name); //some better way to do it..
        rStrategies.put(runner, s);
    }

    public LinkedList<Catcher> getCatchers() {
        return catchers;
    }
    public Runner getRunner() {
        return runner;
    }
    public ClueList getClueList() { return clueList; }

    public void setSimulationTimer() { //5s miedzy iteeracjami - przeskok z wezla do wezla
        if(timer!=null) {
            timer.stop();
        }
        timer = new Timer(100 * Time.timeInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                time.move();

                runner.getDestination(rStrategies.get(runner));
                for(Catcher c : catchers)
                    c.getDestination(cStrategies.get(c));

                runner.move();
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

    public Astar getPathAlgorithm() {
        return pathAlgorithm;
    }

    public Time getTime() {
        return time.getCurrentTime();
    }
}
