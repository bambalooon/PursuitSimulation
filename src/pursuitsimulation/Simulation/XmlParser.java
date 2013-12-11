package pursuitsimulation.Simulation;

import com.sun.xml.internal.stream.events.XMLEventAllocatorImpl;
import pursuitsimulation.Crossing;
import pursuitsimulation.Simulation.SimulationProgram;
import pursuitsimulation.util.Position;

import javax.xml.namespace.QName;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import javax.xml.stream.util.XMLEventAllocator;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 22:38
 * To change this template use File | Settings | File Templates.
 */
public class XmlParser {
    private SimulationProgram parent;
    private XMLInputFactory factory = XMLInputFactory.newInstance();
    private XMLStreamReader reader;
    private static XMLEventAllocator allocator;
    String filename;
    public XmlParser(SimulationProgram parent) {
        this.parent = parent;
    }
    void chooseFile(String filename) throws FileNotFoundException, XMLStreamException { //package-private
        this.filename = filename;
        setReader();
    }
    private void setReader() throws FileNotFoundException, XMLStreamException {
        factory.setEventAllocator(new XMLEventAllocatorImpl());
        allocator = factory.getEventAllocator();
        reader = factory.createXMLStreamReader(filename, new FileInputStream(filename));
    }
    private static XMLEvent getXMLEvent(XMLStreamReader reader) throws XMLStreamException {
        return allocator.allocate(reader);
    }
    void parse() throws XMLStreamException {
        HashMap<Long, Position> nodes = new HashMap<Long, Position>();
        HashMap<Long, Crossing> vertexes = new HashMap<Long, Crossing>();
        int eventType;
        LinkedList<Long> ids = new LinkedList<Long>();
        boolean way = false;
        boolean highway = false;
        boolean oneway = false;
        Set<String> roads = new TreeSet<String>();
        roads.addAll(Arrays.asList("motorway", "motorway_link", "trunk", "trunk_link", "primary", "primary_link",
                "secondary", "secondary_link", "tertiary", "tertiary_link", "living_street", "residential", "unclassified",
                "track")); //"service

        while (reader.hasNext()) {
            eventType = reader.next();
            if (eventType == XMLStreamConstants.START_ELEMENT
                    && reader.getLocalName().equals("way")) {
                way = true;
                highway = false;
                oneway = false;
                // get immutable XMLEvent
                StartElement event = getXMLEvent(reader).asStartElement();
            }
            else if(eventType == XMLStreamConstants.END_ELEMENT
                    && reader.getLocalName().equals("way")) {
                if(highway) {
                    Crossing prev=null;
                    for(long id : ids) {
                        Crossing c = vertexes.get(id);
                        if(c==null) {
                            Position pos = nodes.get(id);
                            if(pos==null); //throw?
                            c = new Crossing(id, pos);
                            vertexes.put(id, c);
                        }
                        if(prev!=null) {
                            prev.addNeighbour(c);
                            c.addInNeighbour(prev);
                            if(!oneway) {
                                c.addNeighbour(prev);
                                prev.addInNeighbour(c);
                            }
                        }
                        prev = c;
                    }
                }
                ids.clear();
                way = false;
            }
            else if(way && eventType == XMLStreamConstants.START_ELEMENT
                    && reader.getLocalName().equals("nd")) {
                StartElement event = getXMLEvent(reader).asStartElement();
                ids.add(Long.parseLong(event.getAttributeByName(new QName("ref")).getValue()));
            }
            else if(way && eventType == XMLStreamConstants.START_ELEMENT
                    && reader.getLocalName().equals("tag")) {
                StartElement event = getXMLEvent(reader).asStartElement();
                String k = event.getAttributeByName(new QName("k")).getValue();
                String v = event.getAttributeByName(new QName("v")).getValue();
                if(k.equalsIgnoreCase("highway") && roads.contains(v.toLowerCase()))
                    highway = true;
                else if(k.equalsIgnoreCase("oneway") && v.equalsIgnoreCase("yes"))
                    oneway = true;
            }
            else if (eventType == XMLStreamConstants.START_ELEMENT
                    && reader.getLocalName().equals("node")) {
                StartElement event = getXMLEvent(reader).asStartElement();
                Long id = Long.parseLong(event.getAttributeByName(new QName("id")).getValue());
                double lon = Double.parseDouble(event.getAttributeByName(new QName("lon")).getValue());
                double lat = Double.parseDouble(event.getAttributeByName(new QName("lat")).getValue());
                nodes.put(id, new Position(lon, lat));
            }
        }

         parent.setGraph(vertexes);
    }
}
