package pursuitsimulation.Simulation;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.Exceptions.NoGuiException;
import pursuitsimulation.GUI.SimulationGUI;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Strategies.CatchingStrategy;
import pursuitsimulation.Strategies.RunningStrategy;
import pursuitsimulation.util.Heuristic.CrowsDistanceHeuristic;
import pursuitsimulation.util.PathFinder;
import pursuitsimulation.util.ClueList;
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
public class SimulationProcess extends Thread {
    public static final int MIN_CATCHERS = 1;
    public static final int MAX_CATCHERS = 50;
    public static final int INIT_CATCHERS = 3;
    public static final int STEP_CATCHERS = 1;
    public static int catchersNumber = INIT_CATCHERS;

    //Local Clue Probability
    public static final double LCP_MIN=0.01;
    public static final double LCP_MAX=1.0;
    public static final double LCP_INIT=0.3;
    public static final double LCP_STEP=0.01;

    //Global Clue Probability
    public static final double GCP_MIN=0.001;
    public static final double GCP_MAX=1.0;
    public static final double GCP_INIT=0.01;
    public static final double GCP_STEP=0.001;



    private SimulationGUI simulationGUI=null;
    private SimulationGraph graph;

    private LinkedList<Catcher> catchers = new LinkedList<Catcher>();
    private Runner runner=null;
    private Map<Catcher, CatchingStrategy> cStrategies = new HashMap<Catcher, CatchingStrategy>();
    private RunningStrategy rStrategy=null;
    private Clue clue = null;

    private PathFinder pathFinder = new PathFinder(new CrowsDistanceHeuristic());

    private boolean running = false;
    private int iterationCount;
    //util
    private Time time = new Time();
    private long startTime;

    SimulationProcess() {
        graph = new SimulationGraph();
    }
    public SimulationProcess(SimulationProcess proc) {
        simulationGUI = proc.simulationGUI;
        graph = proc.graph;
    }
    public void attachGUI(SimulationGUI gui) {
        simulationGUI = gui;
    }
    void setGraph(Map<Long, Crossing> graph) {
        this.graph.setGraph(graph);
        this.graph.cleanGraph();
    }
    public SimulationGraph getGraph() { return graph; }
    public void addCatcher(CatchingStrategy s, String name) {
        Catcher c = new Catcher(this.graph.getRandomVertex(), this, name); //some better way to do it..
        catchers.add(c);
        cStrategies.put(c, s);
    }
    public void setRunner(RunningStrategy s, String name) {
        runner = new Runner(this.graph.getRandomVertex(), this, name); //some better way to do it..
        rStrategy = s;
    }

    public LinkedList<Catcher> getCatchers() {
        return catchers;
    }
    public Runner getRunner() {
        return runner;
    }
    public Clue getClue() { return clue; }
    public boolean setClue(Clue clue) {
        if( this.clue == null || clue.getTime().getTimeStamp() > this.clue.getTime().getTimeStamp() ) {
            this.clue = clue;
            return true;
        }

        return false;
    }
    public void clearClue() { clue = null; }

    public void run() {
        time = new Time();
        iterationCount=0;
        while(running) {
            System.out.println("Iteracja: "+iterationCount);
            time.move();
            System.out.println("Global clue: "+clue);

            runner.getDestination(rStrategy);
            for(Catcher c : catchers)
                c.getDestination(cStrategies.get(c));
            synchronized(this) {
                runner.move();
                for(Catcher c : catchers) {
                    endCheck(c);
                    eyesOnTargetCheck(c);
                    c.move();
                    endCheck(c);
                }
            }
            iterationCount++;
        }
    }
    public void simulationStart() throws NoGuiException {
        if(simulationGUI==null) throw new NoGuiException();
        running = true;
//        startTime = System.nanoTime();
    }
    public void simulationStop() {
        running = false;
    }

    private void pursuitEnd() {
        System.out.println("Pościg trwał "+iterationCount+" iteracji.");
    }

    public void eyesOnTargetCheck(Catcher c) {
        try {
            if(distanceToRunner(c) <= 5) {
                System.out.println(c + " has eyes on target!");
//                setClue( new Clue(
//                        getTime(),
//                        runner.getCurr().cloneForClue()
//                ));
            }
        } catch(Exception e) {} //Exception is thrown when there's no path to the Runner -> no distance -> we do nothing
    }

    public int distanceToRunner(Catcher c) throws Exception {
        return pathFinder.getDistance( c.getCurr(), runner.getCurr() );
    }

    public void endCheck(Catcher c) {
        if(caughtRunner(c)) {
            pursuitEnd();
            simulationStop();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    simulationGUI.simulationEnd();
                }
            });
        }
    }

    public Boolean caughtRunner(Catcher c) {
        return c.getCurr().equals(runner.getCurr());
    }

    public Time getTime() {
        return time.getCurrentTime();
    }
    public boolean isRunning() {
        return running;
    }
    public void changeCatchersNumber(int num) {   //for next simulation
        if((num<=SimulationProcess.MAX_CATCHERS) && (num>=SimulationProcess.MIN_CATCHERS)) {
            SimulationProcess.catchersNumber = num;
        }
    }
}
