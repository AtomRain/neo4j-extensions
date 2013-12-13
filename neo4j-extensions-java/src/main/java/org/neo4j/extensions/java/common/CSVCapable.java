package org.neo4j.extensions.java.common;

/**
 * Defines contract for converting data objects to and from CSV.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public interface CSVCapable {

    String[] getValues();

}
