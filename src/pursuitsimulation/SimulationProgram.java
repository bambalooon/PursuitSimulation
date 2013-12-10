package pursuitsimulation;

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
    //GUI
    private SimulationProcess process;
    private XmlParser parser;
    public SimulationProgram() {
        process = new SimulationProcess();
        parser = new XmlParser(this);
    }
    public void setGraphFile(String filename) throws FileNotFoundException, XMLStreamException {
        parser.chooseFile(filename);
        parser.parse();
    }
    void setGraph(Map<Long, Crossing> graph) {
        process.setGraph(graph);
    }

    public static void main(String[] args) throws FileNotFoundException, XMLStreamException, IOException  {
        SimulationProgram program = new SimulationProgram();
        program.setGraphFile("./src/files/map.osm");



    }
}
