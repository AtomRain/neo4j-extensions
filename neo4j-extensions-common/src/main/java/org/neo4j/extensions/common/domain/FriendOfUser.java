package org.neo4j.extensions.common.domain;

import java.io.Serializable;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAutoDetect;

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
public class FriendOfUser implements RelationshipType, Serializable
{

    private static final long serialVersionUID = -5433623217015754491L;

    private Long id;

    private User user;

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

    @Override
    public boolean equals( Object obj )
    {
        if ( obj == null )
        {
            return false;
        }
        if ( obj == this )
        {
            return true;
        }
        if ( obj.getClass() != getClass() )
        {
            return false;
        }
        FriendOfUser rhs = (FriendOfUser) obj;
        return new EqualsBuilder()
                .append( this.id, rhs.id )
                .append( this.user, rhs.user )
                .append( this.friend, rhs.friend )
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( id )
                .append( user )
                .append( friend )
                .toHashCode();
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder( this )
                .append( "id", id )
                .append( "user", user )
                .append( "friend", friend )
                .toString();
    }
}
