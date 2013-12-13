package org.neo4j.extensions.java.common;

import java.util.Date;

/**
 * Model for date range.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class DateRange {

    private long min;

    private long max;

    public DateRange(Date min, Date max) {
        this.min = min.getTime();
        this.max = max.getTime();
    }

    public long getMinimum() {
        return min;
    }

    public long getMaximum() {
        return max;
    }

}
