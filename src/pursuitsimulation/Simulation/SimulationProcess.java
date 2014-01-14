package pursuitsimulation.Simulation;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
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
public class SimulationProcess {
    private SimulationProgram main;
    private Time time = new Time();
    private long startTime;
    private SimulationGraph graph = new SimulationGraph();
    private LinkedList<Catcher> catchers = new LinkedList<Catcher>();
    private Runner runner;
    private ClueList clueList = new ClueList();
    private Map<Catcher, CatchingStrategy> cStrategies = new HashMap<Catcher, CatchingStrategy>();
    private RunningStrategy rStrategy;
    private boolean running = false;
    private Timer timer;
    private PathFinder pathFinder = new PathFinder(new CrowsDistanceHeuristic());

    SimulationProcess(SimulationProgram program) {
        main = program;
    }
    void setGraph(Map<Long, Crossing> graph) {
        this.graph.setGraph(graph);
        this.graph.cleanGraph();
    }
    public void reset() {
        running = false;
        catchers.clear();
        runner = null;
        cStrategies.clear();
        rStrategy = null;
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
    public ClueList getClueList() { return clueList; }

    public void setSimulationTimer() { //5s miedzy iteeracjami - przeskok z wezla do wezla
        if(timer!=null) {
            timer.stop();
        }
        timer = new Timer(Time.timeInterval, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                time.move();

                runner.getDestination(rStrategy);
                for(Catcher c : catchers)
                    c.getDestination(cStrategies.get(c));

                runner.move();
                for(Catcher c : catchers) {
                    endCheck(c);
                    eyesOnTargetCheck(c);
                    c.move();
                    endCheck(c);
                }


                SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        main.updateGuiMap();
                    }
                });
            }
        });
    }
    public void simulationStart() {
        running = true;
        startTime = System.nanoTime();
        timer.start();
    }
    public void simulationStop() {
        timer.stop();
    }

    private void pursuitEnd() {
        long timeElapsed = System.nanoTime() - startTime;
        System.out.println("Pościg zakończył się po upływie " + (int)(timeElapsed/1000000000.0) + " sekund");
    }

    public void eyesOnTargetCheck(Catcher c) {
        try {
            if(distanceToRunner(c) <= 5)
                System.out.println("Catcher #" + c + " has eyes on target!");
                clueList.add( new Clue(
                        getTime(),
                        runner.getPrev(),
                        runner.getCurr(),
                        runner
                ));
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
                    reset();
                    main.getGui().setRunnerHandle(null);
                    main.showEndAlert();
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
    public void updateTimer() {
        timer.setDelay(Time.timeInterval);
    }
}
