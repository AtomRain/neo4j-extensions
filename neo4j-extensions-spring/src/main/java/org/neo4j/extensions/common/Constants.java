package org.neo4j.extensions.common;

/**
 * Constants.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public class Constants {

    public static final String PROPERTY_NODE_TYPE = "__type__";

    public static final String PROPERTY_RELS_TYPE = "__rels_type__";

    public static final String QUERY_NODE_ALL = "start n=node(*) return n";

    public static final String QUERY_REL_ALL = "start r=relationship(*) return r";

}
