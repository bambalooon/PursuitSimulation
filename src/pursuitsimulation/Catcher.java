package pursuitsimulation;

import java.util.LinkedList;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 23:04
 * To change this template use File | Settings | File Templates.
 */
public class Catcher extends Person {
    private LinkedList<Catcher> team;
    //database;
    public Catcher(Crossing current) {
        super(current);
        team = new LinkedList<Catcher>();
    }

}
