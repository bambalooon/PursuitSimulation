package pursuitsimulation.util;

import pursuitsimulation.Crossing;
import pursuitsimulation.Vertex;

import java.util.*;

/**
 * Created by mike on 12/16/13.
 */
public class Astar {
    /* Key: current Crossing, Value: Crossing we came from */
    private List<CrossingStructure> closedList;
    private List<CrossingStructure> openList;

    public LinkedList<Crossing> findPath(Crossing start, Crossing end) {
        System.out.println("Searching for path!!");

        long counter = 1;

        closedList = new ArrayList<CrossingStructure>();
        openList = new ArrayList<CrossingStructure>();
        CrossingStructure current = new CrossingStructure(start, null);

        current.setgScore(0).sethScore(calculatehScore(start, end));
        addToOpen(current);

        while(!openList.isEmpty()) {
            current = openList.get(0);
            //System.out.println("Checking node: " + current.getCrossing());
            if(counter%1000 == 0)
                System.out.println(counter + " nodes checked");

            openList.remove(current);
            closedList.add(current);

            if( current.getCrossing().equals( end ) ) {
                return reconstructPath( current );
            }

            for(Vertex v : current.getCrossing().getNeighbours()) {
                CrossingStructure neighbour = new CrossingStructure( (Crossing) v );

                if( findInList(closedList, neighbour.getCrossing()) != null ) {
                    continue;
                }

                CrossingStructure found = findInList(openList, neighbour.getCrossing() );
                double gScore = current.getgScore() + current.getCrossing().calcualteDistance( neighbour.getCrossing() );

                if( found == null) {
                    neighbour.setgScore( gScore );
                    neighbour.sethScore( neighbour.getCrossing().calcualteDistance(end) );
                    neighbour.setParent( current.getCrossing() );

                    addToOpen( neighbour );
                } else if( gScore < found.getgScore() ) {
                    found.setParent( current.getCrossing() );
                    found.setgScore( gScore );

                    Collections.sort(openList);
                }
            }
            counter++;
        }

        System.out.println("Path not found!!!!!");
        return null;
    }

    public LinkedList<Crossing> reconstructPath(CrossingStructure crossing) {
        LinkedList<Crossing> path = new LinkedList<Crossing>();

        CrossingStructure current = crossing;

        while( current.getParent() != null ) {
            path.add(0, current.getCrossing());
            current = findInList(closedList, current.getParent());
        }

//        printPath(path);

        return path;
    }

    public void printPath(LinkedList<Crossing> path) {
        ListIterator<Crossing> it = path.listIterator();
        Crossing cur;

        System.out.println("New path");

        while(it.hasNext()) {
            cur = it.next();
            System.out.println(cur);
        }
    }

    public double calculatehScore(Crossing from, Crossing to) {
        return from.calcualteDistance(to);
    }

    public void addToOpen(CrossingStructure c) {
        openList.add(0, c);
        Collections.sort(openList);
    }

    public CrossingStructure findInList(List<CrossingStructure> list, Crossing c) {
        ListIterator<CrossingStructure> it = list.listIterator();
        CrossingStructure current;

        while(it.hasNext()) {
            current = it.next();

            if(current.getCrossing().equals(c))
                return current;
        }

        return null;
    }
}
