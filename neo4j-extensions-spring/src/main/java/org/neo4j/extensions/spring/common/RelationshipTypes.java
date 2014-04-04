package org.neo4j.extensions.spring.common;

/**
 * Relationship types.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
public enum RelationshipTypes {

    FRIEND_OF(RelationshipConstants.FRIEND_OF);

    private String type;

    private RelationshipTypes(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

}
