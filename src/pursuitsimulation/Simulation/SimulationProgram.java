package pursuitsimulation.Simulation;

import pursuitsimulation.Crossing;
import pursuitsimulation.GUI.SimulationGUI;
import pursuitsimulation.Strategies.StandardCatchingStrategy;
import pursuitsimulation.Strategies.StandardRunningStrategy;
import pursuitsimulation.util.Position;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;
import java.util.Random;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */
public class SimulationProgram {
    public static Random randomGenerator = new Random();
    private SimulationGUI gui = null;
    private SimulationProcess process;
    private XmlParser parser;
    private String xmlFile;
    private String mapFile;
    public SimulationProgram() {
        process = new SimulationProcess(this);
        parser = new XmlParser(this);
    }
    public void setXmlFile(String filename) throws FileNotFoundException, XMLStreamException {
        parser.chooseFile(filename);
        parser.parse();
    }
    public void setMapFile(String filename) {
        try {
            gui.chooseMapFile(filename);
        } catch(IOException e) {} //Handle Ex
    }
    public SimulationGUI getGui() {
        return gui;
    }
    void updateGuiMap() {
        gui.showEditedMap();
    }
    void setGraph(Map<Long, Crossing> graph) {
        process.setGraph(graph);
//        System.out.println("Created graph with " + process.getGraph().getGraphSize() + " nodes");
    }
    void showEndAlert() {
        gui.showEndAlert();
    }
    void setGUI(SimulationGUI gui) {
        this.gui = gui;
        setMapFile("./PursuitSimulation/src/files/ny.png"); //map.png normalnie wywolywane przez GUI
//        gui.setULpos(new Position(19.8988, 50.0684));
//        gui.setDRpos(new Position(19.9307, 50.0524));
        gui.setULpos(new Position(-73.9828, 40.7913));
        gui.setDRpos(new Position(-73.8522, 40.7147));
    }


    public static void main(String[] args) throws FileNotFoundException, XMLStreamException, IOException  {
        final SimulationProgram program = new SimulationProgram();
        program.setXmlFile("./PursuitSimulation/src/files/ny.osm"); //map.osm

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                program.setGUI(new SimulationGUI(program.process));
                program.process.setSimulationTimer();
            }
        });

    }
}
