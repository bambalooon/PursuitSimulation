package pursuitsimulation;

import pursuitsimulation.GUI.SimulationGUI;
import pursuitsimulation.SimulationProgram;
import pursuitsimulation.XmlParser;

import javax.xml.stream.XMLStreamException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 21:55
 * To change this template use File | Settings | File Templates.
 */
public class SimulationProgram {
    private SimulationGUI gui = null;
    private SimulationProcess process;
    private XmlParser parser;
    private String xmlFile;
    private String mapFile;
    public SimulationProgram() {
        process = new SimulationProcess();
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
    void setGraph(Map<Long, Crossing> graph) {
        process.setGraph(graph);
    }
    void setGUI(SimulationGUI gui) {
        this.gui = gui;
        setMapFile("./src/files/map.png"); //normalnie wywolywane przez GUI
        gui.setULpos(new Position(19.8988, 50.0684));
        gui.setDRpos(new Position(19.9307, 50.0524));
        gui.setCatchersHandle(process.getCatchers());
        gui.setRunnersHandle(process.getRunners());
        gui.showEditedMap();
    }


    public static void main(String[] args) throws FileNotFoundException, XMLStreamException, IOException  {
        final SimulationProgram program = new SimulationProgram();
        program.setXmlFile("./src/files/map.osm");

        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                program.setGUI(new SimulationGUI());
            }
        });

    }
}
