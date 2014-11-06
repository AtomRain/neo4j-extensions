package org.neo4j.extensions.common.types;

import org.neo4j.graphdb.RelationshipType;

/**
 * Relationship types.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
public enum RelationshipTypes implements RelationshipType
{

    FRIEND_OF( RelationshipConstants.FRIEND_OF );

    private String type;

    private RelationshipTypes( String type )
    {
        this.type = type;
    }

    public String getType()
    {
        return this.type;
    }

}
