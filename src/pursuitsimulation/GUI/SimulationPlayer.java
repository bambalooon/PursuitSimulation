package pursuitsimulation.GUI;

import pursuitsimulation.Clue;
import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.util.Time;
import pursuitsimulation.util.Pair;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 18.01.14
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class SimulationPlayer implements ActionListener {
    public static final int INTERVAL_MIN = 0;
    public static final int INTERVAL_MAX = 2000;
    public static final int INTERVAL_INIT = 500;

    private SimulationGUI gui;
    private SimulationProcess process;

    private Runner runner;
    private LinkedList<Catcher> catchers;

    private Iterator<Pair<Crossing,Crossing>> rIterator;
    private LinkedList< ListIterator< Pair<Crossing,Crossing> > > cIterators;
    private Iterator<Clue> gcIterator; //globalClues

    private Timer timer=null;
    private boolean initialized=false;
    private boolean playing=false;

    public SimulationPlayer(SimulationGUI gui) {
        this.gui = gui;
    }
    public void attachProcess(SimulationProcess proc) {
        process = proc;
        runner = process.getRunner();
        catchers = process.getCatchers();
    }

    private void setSimulationTimer() {
        initialized = true;
        gui.resetLocalClues();
        if(timer!=null) {
            timer.stop();
        }
        rIterator = runner.getRoute().iterator();

        cIterators = new LinkedList< ListIterator< Pair<Crossing,Crossing> > >();
        for(Catcher c : catchers) {
            cIterators.add(c.getRoute().listIterator());
        }
        gcIterator = runner.getGlobalClues().iterator();

        timer = new Timer(Time.timeInterval, this);
    }

    public void actionPerformed(ActionEvent e) {
        boolean hasNext = rIterator.hasNext();
        if(!hasNext) {
            if(!process.isRunning()) {
                stop();
            }
            return;
        }
        if(hasNext) {
            Pair<Crossing, Crossing> pair = rIterator.next();
            gui.setRunnerCrossing(pair.getKey());
            gui.setRunnerDestination(pair.getValue());
        }

        LinkedList<Crossing> c = new LinkedList<Crossing>();
        for(ListIterator<Pair<Crossing,Crossing>> iter : cIterators) {
            if(iter.hasNext()) {
                c.add(iter.next().getKey());
            } else if(hasNext) {
                if(iter.hasPrevious()) {
                    iter.previous();
                    c.add(iter.next().getKey());
                }
            }

        }
        if(gcIterator.hasNext()) {
            Clue clue = gcIterator.next();
            gui.setGlobalClue(clue);
        }
        gui.setCatchersCrossings(c);
        gui.showEditedMap();
    }

    public void play() {
        if(initialized && !playing) {
            timer.start();
        } else if(!initialized) {
            setSimulationTimer();
            timer.start();
        }
        playing = true;
    }
    public void stop() {
        initialized = false;
        playing = false;
        if(timer!=null) {
            timer.stop();
            timer=null;
        }
        gui.playingEnd();
    }
    public void pause() {
        if(initialized && playing) {
            playing = false;
            timer.stop();
        }
    }
    public void updateTimer() {
        if(timer!=null) {
            timer.setDelay(Time.timeInterval);
        }
    }
}
