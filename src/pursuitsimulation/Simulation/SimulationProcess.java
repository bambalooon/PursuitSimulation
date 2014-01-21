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
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
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
    //Simulation Limits
    public static int iterationsMax = 1000;

    private SimulationGUI simulationGUI=null;
    private SimulationGraph graph;

    private LinkedList<Catcher> catchers = new LinkedList<Catcher>();
    private Runner runner=null;
    private Map<Catcher, CatchingStrategy> cStrategies = new HashMap<Catcher, CatchingStrategy>();
    private RunningStrategy rStrategy=null;
    private Clue clue = null;
    private int clueTimestamp = 0;

    private PathFinder pathFinder = new PathFinder(new CrowsDistanceHeuristic());

    private boolean running = false;
    private int iterationCount;
    //util
    private Time time = new Time();
    private long startTime;
    private Random rand = new Random();

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
    public Boolean setClue(Clue clue) {
        if( this.clue == null || clue.getTime().getTimeStamp() > this.clueTimestamp ) {
            this.clue = clue;
            clueTimestamp = clue.getTime().getTimeStamp();
            return true;
        }

        return false;
    }
    public void clearClue() { clue = null; }

    public void run() {
        iterationCount=1;
        while(running) {
            System.out.println("Iteracja #" + iterationCount);
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    simulationGUI.changeIterationCount(iterationCount);
                }
            });
            time.move();

            runner.getDestination(rStrategy);
            for(Catcher c : catchers)
                c.getDestination(cStrategies.get(c));

            synchronized(this) {
                runner.move();
                for(Catcher c : catchers) {
                    if(endCheck(c))
                        return;
                    eyesOnTargetCheck(c);
                    c.move();
                    if(endCheck(c))
                        return;
                }
            }

            iterationCount++;
            if(iterationCount >= iterationsMax)
            {
               endOnDemand();
            }
        }
    }
    public void saveResult()
    {
        FileWriter pursuitResult = null;
        try {
            pursuitResult = new FileWriter("result.txt", true);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Date data = new Date();
        try {
            String tmp =   tmp = cStrategies.get(catchers.getFirst()).toString();
            pursuitResult.write(data.toString() + ";" + rStrategy.toString() + ";" + tmp  + ";" + iterationCount + ";" + iterationsMax + ";" + catchers.size() + ";" +  LCP_INIT + ";" + LCP_INIT  + "\r\n");
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            pursuitResult.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void endOnDemand() {
        pursuitEnd();
        simulationStop();
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                simulationGUI.simulationEnd();
            }
        });

    }
    public void simulationStart() throws NoGuiException {
        if(simulationGUI==null) throw new NoGuiException();
            running = true;
    }
    public void simulationStop() {
        running = false;
    }

    private void pursuitEnd() {
        System.out.println("Pościg trwał " + iterationCount + " iteracji.");
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                simulationGUI.iterationEnd(iterationCount);
            }
        });
        saveResult();
    }

    public void eyesOnTargetCheck(Catcher c) {
        try {
            if(distanceToRunner(c) <= 5 && rand.nextDouble() < 0.9) {
                System.out.println(c + " has eyes on target!");
                setClue( new Clue(
                        getTime(),
                        runner.getCurr()
                ));
            }
        } catch(Exception e) {} //Exception is thrown when there's no path to the Runner -> no distance -> we do nothing
    }

    public int distanceToRunner(Catcher c) throws Exception {
        return pathFinder.getDistance( c.getCurr(), runner.getCurr() );
    }

    public Boolean endCheck(Catcher c) {
        if(caughtRunner(c)) {
            pursuitEnd();
            simulationStop();
            SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    simulationGUI.simulationEnd();
                }
            });
            return true;
        }
        return false;
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
