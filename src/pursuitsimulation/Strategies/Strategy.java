package pursuitsimulation.Strategies;

import pursuitsimulation.Crossing;
import pursuitsimulation.People.Person;
import pursuitsimulation.Simulation.SimulationProcess;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 23:13
 * To change this template use File | Settings | File Templates.
 */
public abstract class Strategy {
    protected SimulationProcess process;

    public Strategy(SimulationProcess process) {
        this.process = process;
    }

    public abstract Crossing getDestination(Person p);
}
