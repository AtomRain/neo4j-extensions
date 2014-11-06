package org.neo4j.extensions.examples.social.domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import org.neo4j.extensions.common.types.RelationshipConstants;
import org.neo4j.graphdb.RelationshipType;

/**
 * Friend of User relationship.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@XmlRootElement
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@RelationshipEntity(type = RelationshipConstants.FRIEND_OF)
public class FriendOfUser implements RelationshipType, Serializable
{

    private static final long serialVersionUID = -5433623217015754491L;

    @GraphId
    private Long id;

    @StartNode
    private User user;

    @EndNode
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
