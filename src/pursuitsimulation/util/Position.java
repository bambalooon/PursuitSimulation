package pursuitsimulation.util;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 05.12.13
 * Time: 23:00
 * To change this template use File | Settings | File Templates.
 */
public class Position {
    private double x, y;
    public Position(double x, double y) { this.x = x; this.y = y; }
    public double getX() { return x; }
    public double getY() { return y; }

    public double calculateDistance(Position p) {
        return sqrt(pow((x-p.getX()),2) + pow((y-p.getY()), 2));
    }
}
