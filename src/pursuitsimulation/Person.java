package pursuitsimulation;

import pursuitsimulation.Position;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 22:56
 * To change this template use File | Settings | File Templates.
 */
public class Person {
    private Crossing prev, next=null;
    private Position pos;
    private int waiting = 0; //0 = not, else num of iteration
    public Person(Crossing current) {
        prev = current;
        pos = current.getPos();
    }
    public void move(Strategy s) {

    }
    private Crossing getNextDestination(Strategy s) {
        return null;
    }
    private void wait(int timestamp) {}

}
