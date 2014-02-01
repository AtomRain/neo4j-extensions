package org.neo4j.extensions.java.common;

import org.neo4j.graphdb.RelationshipType;

/**
 * Relationship types.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public enum RelationshipTypes implements RelationshipType {

    MEMBER,

    FRIEND_OF;

}
