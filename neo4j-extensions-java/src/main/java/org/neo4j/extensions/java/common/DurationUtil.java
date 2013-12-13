package org.neo4j.extensions.java.common;

import java.util.Collections;
import java.util.LinkedList;

/**
 * Utility for determining duration.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class DurationUtil {

    /**
     * Parses a duration timecode and returns the total number of seconds in the duration.
     * 
     * @param  timecode
     * @return seconds
     */
    public static long timecodeToSeconds(String timecode) {

        if (timecode == null) {
            return 0L;
        }

        long black = 0;
        long hours = 0;
        long mins = 0;
        long secs = 0;

        try {
            String[] parts = timecode.split("/");
            if (parts.length == 2) {
                black = Long.parseLong(parts[1].trim());
            }

            String base = parts[0].trim();
            if (base.endsWith(":")) {
                base = base + "0";
            }

            LinkedList<String> tokens = new LinkedList<String>();
            String[] hms = base.split(":");
            for (int i = 0; i < hms.length; i++) {
                tokens.add(hms[i].isEmpty() ? "0" : hms[i]);
            }

            Collections.reverse(tokens);

            int count = tokens.size();

            if (count > 2) {
                hours = Long.parseLong(tokens.get(2));
            }
            if (count > 1) {
                mins = Long.parseLong(tokens.get(1));
            }
            if (count > 0) {
                secs = Long.parseLong(tokens.get(0));
            }

            return black + secs + (mins * 60) + (hours * 3600);
        }
        catch (NumberFormatException e) {
            return 0L;
        }
    }

    public static String secondsToTimecode(long seconds) {
        long hours = 0;
        long mins = 0;
        long secs = 0;

        hours = seconds / 3600L;
        seconds -= (hours * 3600);

        mins = seconds / 60L;
        secs = seconds - (mins * 60L);

        String result = String.format(":%02d", Long.valueOf(secs));
        if ((hours > 0L) || (mins > 0L)) {
            String fmt = (hours > 0L) ? "%02d" : "%d";
            result = String.format(fmt + "%s", Long.valueOf(mins), result);
        }
        if (hours > 0L) {
            result = String.format("%d:%s", Long.valueOf(hours), result);
        }
        return result;
    }

    public static String getRoundedTimecode(String timecode, long interval) {
        long seconds = timecodeToSeconds(timecode);
        return secondsToTimecode(getRoundedSeconds(seconds, interval));
    }

    public static long getRoundedSeconds(long seconds, long interval) {
        long mod = seconds % interval;
        if (mod == 0) {
            return seconds;
        }
        if (mod > (interval / 2)) {
            return (seconds - mod) + interval;
        }
        return seconds - mod;
    }

}

