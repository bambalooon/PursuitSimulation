package pursuitsimulation.People;

import pursuitsimulation.Crossing;

import java.util.LinkedList;

/**
 * Created by mike on 12/15/13.
 */
public class Catcher extends Person {
    private LinkedList<Catcher> team;

    public Catcher(Crossing current) {
        super(current);
        team = new LinkedList<Catcher>();
    }

    public void addTeammate(Catcher c) {
        team.add(c);
    }
}
