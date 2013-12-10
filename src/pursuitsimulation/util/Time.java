package pursuitsimulation.util;

/**
 * Created with IntelliJ IDEA.
 * User: BamBalooon
 * Date: 09.12.13
 * Time: 01:04
 * To change this template use File | Settings | File Templates.
 */
public class Time {
    private static final int sec2Min = 60;
    private static final int min2Hour = 60;
    private static final int hour2Day = 24;
    public static final int timeInterval = 5;
    private int day=0;
    private int hour=0;
    private int minute=0;
    private int second=0;
    public int getTimeStamp() {
        return ((day*hour2Day+hour)*min2Hour+minute)*sec2Min+second;
    }
    public int getHour() {   //pora dnia..
        return hour;
    }
    public void move() {
        second+=timeInterval;
        minute+=(int) second/sec2Min;
        second%=sec2Min;
        hour+=(int) minute/min2Hour;
        minute%=min2Hour;
        day+=(int) hour/hour2Day;
        hour%=hour2Day;
    }
}
