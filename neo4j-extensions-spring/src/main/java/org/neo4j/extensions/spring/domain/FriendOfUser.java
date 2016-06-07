package org.neo4j.extensions.spring.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonView;

import java.io.Serializable;

import org.neo4j.extensions.common.client.UserTinyView;
import org.neo4j.extensions.common.types.RelationshipConstants;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.ogm.annotation.EndNode;
import org.neo4j.ogm.annotation.GraphId;
import org.neo4j.ogm.annotation.RelationshipEntity;
import org.neo4j.ogm.annotation.StartNode;

/**
 * Friend of User relationship.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@JsonAutoDetect
@JsonIgnoreProperties( ignoreUnknown = true, value = {"nodeId", "template", "entityState", "persistentState"} )
@RelationshipEntity( type = RelationshipConstants.FRIEND_OF )
public class FriendOfUser implements RelationshipType, Serializable
{

    private static final long serialVersionUID = -6051465472873463907L;

    @GraphId
    @JsonView( UserTinyView.class )
    private Long id;

    @StartNode
    @JsonView( UserTinyView.class )
    private User user;

    @EndNode
    @JsonView( UserTinyView.class )
    private User friend;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public User getFriend()
    {
        return friend;
    }

    public void setFriend( User friend )
    {
        this.friend = friend;
    }

    public String name()
    {
        return RelationshipConstants.FRIEND_OF;
    }

}
