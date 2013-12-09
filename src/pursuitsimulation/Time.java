package pursuitsimulation;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 01:04
 * To change this template use File | Settings | File Templates.
 */
public class Time {
    private static final int min2Hour = 60;
    private static final int hour2Day = 24;
    private int day;
    private int hour;
    private int minute;
    public int getTimeStamp() {
        return (day*hour2Day+hour)*min2Hour+minute;
    }
    public int getHour() {   //pora dnia..
        return hour;
    }
}
