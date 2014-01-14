package pursuitsimulation.util;

import pursuitsimulation.Crossing;
import sun.management.resources.agent_pt_BR;

import static java.lang.Math.pow;
import static java.lang.Math.sqrt;

/**
 * Created by mike on 1/14/14.
 */
public class Vector {
    private double x;
    private double y;

    public Vector() {
        x = 0;
        y = 0;
    }

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector(Vector v) {
        x = v.x;
        y = v.y;
    }

    public Vector(Crossing c) {
        x = c.getPos().getX();
        y = c.getPos().getY();
    }

    public Vector add(Vector v) {
        x += v.x;
        y += v.y;

        return this;
    }

    public Vector negate() {
        x = -x;
        y = -x;

        return this;
    }

    public double getLength() {
        return sqrt(pow((x),2) + pow((y), 2));
    }

    public double getX() {
        return x;
    }

    public Vector setX(double x) {
        this.x = x;

        return this;
    }

    public double getY() {
        return y;
    }

    public Vector setY(double y) {
        this.y = y;

        return this;
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }
}