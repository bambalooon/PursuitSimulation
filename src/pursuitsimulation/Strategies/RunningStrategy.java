package pursuitsimulation.Strategies;

import pursuitsimulation.Simulation.SimulationProcess;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 23:15
 * To change this template use File | Settings | File Templates.
 */
public abstract class RunningStrategy extends Strategy {
    public static Class[] runningStrategies = {StandardRunningStrategy.class};
    public RunningStrategy(SimulationProcess process) {
        super(process);
    }
}
