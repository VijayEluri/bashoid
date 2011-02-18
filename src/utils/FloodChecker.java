package utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ListIterator;


public class FloodChecker {

    private static final long ONE_MINUTE;
    private static final long MAX_SERVES_PER_TIME;
    private static HashMap<String, ArrayList<Long>> records;

    static {
        ONE_MINUTE = 60;
        MAX_SERVES_PER_TIME = 5;
        records = new HashMap<String, ArrayList<Long>>();
    }

    public static boolean canBeServed(String hostname) {
        if ( !isInLog(hostname) )
            return true;

        ArrayList<Long> times = records.get(hostname);
        times = removeOldRecords(times);
        records.put(hostname, times);
        return ( times.size() < MAX_SERVES_PER_TIME );
    }

    private static boolean isInLog(String hostname) {
        return records.containsKey(hostname);
    }

    private static ArrayList<Long> removeOldRecords(ArrayList<Long> times) {
        ListIterator it = times.listIterator();
        while ( it.hasNext() )
            if ( isOld( (Long) it.next() ))
                it.remove();

        return times;
    }

    private static boolean isOld(Long time) {
        return ( CurrentTime.inSeconds() - time > ONE_MINUTE );
    }

    public static void logServed(String hostname) {
        ArrayList<Long> times = getTimesServed(hostname);
        times.add( CurrentTime.inSeconds() );
        records.put(hostname, times);
    }

    private static ArrayList<Long> getTimesServed(String hostname) {
        ArrayList<Long> times = records.get(hostname);
        return (times == null) ? new ArrayList<Long>() : times;
    }

}
