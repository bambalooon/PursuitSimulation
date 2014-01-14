package pursuitsimulation.util;

import pursuitsimulation.Crossing;
import pursuitsimulation.Vertex;
import pursuitsimulation.util.Heuristic.Heuristic;

import java.util.*;

/**
 * Created by mike on 12/16/13.
 */
public class PathFinder {
    private List<CrossingStructure> closedList;
    private List<CrossingStructure> openList;
    private Heuristic heuristic;

    public PathFinder(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public void setHeuristic(Heuristic heuristic) {
        this.heuristic = heuristic;
    }

    public LinkedList<Crossing> getPath(Crossing start, Crossing end, int pathLength) {
        return reconstructPath( findPath(start, end, pathLength) );
    }

    public int getDistance(Crossing start, Crossing end) throws Exception {
        return pathLength( findPath(start, end, -1) );
    }

    public CrossingStructure findPath(Crossing start, Crossing end, int pathLength) {
//        System.out.println("Searching for path!!");

        closedList = new ArrayList<CrossingStructure>();
        openList = new ArrayList<CrossingStructure>();
        CrossingStructure current = new CrossingStructure(start, null);
        current.setParentStep(-1);

        current.setgScore(0).sethScore( heuristic.calculateHScore(start, end) );
        addToOpen(current);

        while(!openList.isEmpty()) {
            current = openList.get(0);
            //System.out.println("Checking node: " + current.getCrossing());

            openList.remove(current);
            closedList.add(current);

            if( current.getCrossing().equals( end ) || (pathLength != -1 && current.getParentStep()+1 == pathLength)) {
                return current;
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
                    neighbour.setParentStep( current.getParentStep() + 1 );

                    addToOpen( neighbour );
                } else if( gScore < found.getgScore() ) {
                    found.setParent( current.getCrossing() );
                    found.setgScore( gScore );
                    found.setParentStep( current.getParentStep() + 1 );

                    Collections.sort(openList);
                }
            }
        }

//        System.out.println("Path not found!!!!!");
        return null;
    }

    public int pathLength(CrossingStructure crossing) throws Exception { //when path has not been found
        int length = 0;

        CrossingStructure current = crossing;

        while( current.getParent() != null ) {
            length++;
            current = findInList(closedList, current.getParent());
        }

        return length;
    }

    public LinkedList<Crossing> reconstructPath(CrossingStructure crossing) {
        if(crossing == null) // no path found
            return null;

        LinkedList<Crossing> path = new LinkedList<Crossing>();

        CrossingStructure current = crossing;

        while( current.getParent() != null ) {
            path.add(0, current.getCrossing());
            current = findInList(closedList, current.getParent());
        }

//        printPath(path);

        return path;
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
