package pursuitsimulation.GUI;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Catcher;
import pursuitsimulation.People.Runner;
import pursuitsimulation.Simulation.SimulationProcess;
import pursuitsimulation.util.Time;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 18.01.14
 * Time: 13:50
 * To change this template use File | Settings | File Templates.
 */
public class SimulationPlayer implements ActionListener {
    private SimulationGUI gui;
    private SimulationProcess process;

    private Runner runner;
    private LinkedList<Catcher> catchers;

    private Iterator<Crossing> rIterator;
    private LinkedList<Iterator<Crossing>> cIterators;

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
        if(timer!=null) {
            timer.stop();
        }
        rIterator = runner.getRoute().iterator();
        cIterators = new LinkedList<Iterator<Crossing>>();
        for(Catcher c : catchers) {
            cIterators.add(c.getRoute().iterator());
        }
        timer = new Timer(Time.timeInterval, this);
        System.out.println("setting timer finish");
    }

    public void actionPerformed(ActionEvent e) {
        System.out.print("a");
        if(!rIterator.hasNext()) {
            if(!process.isRunning()) {
                stop();
            }
            return;
        }
        if(rIterator.hasNext()) {
            Crossing c = rIterator.next();
            gui.setRunnerCrossing(c);
        }
        LinkedList<Crossing> c = new LinkedList<Crossing>();
        for(Iterator<Crossing> iter : cIterators) {
            if(iter.hasNext()) {
                c.add(iter.next());
            }
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
        timer.stop();
        timer=null;
    }
    public void pause() {
        if(initialized && playing) {
            playing = false;
            timer.stop();
        }
    }
    public void updateTimer() {
        timer.setDelay(Time.timeInterval);
    }
}
