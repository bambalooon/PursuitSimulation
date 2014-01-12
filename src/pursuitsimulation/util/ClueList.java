package pursuitsimulation.util;

import pursuitsimulation.Clue;
import pursuitsimulation.People.Person;

import java.util.Collections;
import java.util.LinkedList;
import java.util.ListIterator;

/**
 * Created by mike on 12/15/13.
 */
public class ClueList {
    LinkedList<Clue> clues = new LinkedList<Clue>();

    /* adds new clues to the list, sorted by time, descending (i.e. newest clue is at head while oldest is at tail) */
    public void add(Clue clue) {
        clues.add(clue);
        Collections.sort(clues);

//        ListIterator<Clue> it = clues.listIterator();
//        Clue c = null;
//
//        while(it.hasNext()) {
//            c = it.next();
//
//            if(clue.getTime().getTimeStamp() > c.getTime().getTimeStamp()) {
//                it.previous();
//                it.add(clue);
//
//                return;
//            }
//        }
//
//        it.add(clue);

    }

    /* get the head of the list without removing it */
    public Clue getFreshClue() {
        return clues.peek();
    }

    public ListIterator<Clue> listIterator() {
        return clues.listIterator();
    }

    public void clear() {
        clues.clear();
    }
}
