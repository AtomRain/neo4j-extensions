package org.neo4j.extensions.common.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonView;

import java.io.Serializable;
import java.util.Collection;

import org.neo4j.extensions.common.client.UserTinyView;

/**
 * Friend result.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@JsonAutoDetect
@JsonIgnoreProperties( ignoreUnknown = true )
public class FriendResult implements Serializable
{

    private static final long serialVersionUID = 1539779889606280663L;

    @JsonView( UserTinyView.class )
    private User user;

    @JsonView( UserTinyView.class )
    private Collection<User> friends;

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public Collection<User> getFriends()
    {
        return friends;
    }

    public void setFriends( Collection<User> friends )
    {
        this.friends = friends;
    }

    @Override
    public boolean equals( Object o )
    {
        if ( this == o )
        {
            return true;
        }
        if ( !(o instanceof FriendResult) )
        {
            return false;
        }

        FriendResult that = (FriendResult) o;

        if ( friends != null ? !friends.equals( that.friends ) : that.friends != null )
        {
            return false;
        }
        if ( user != null ? !user.equals( that.user ) : that.user != null )
        {
            return false;
        }

        return true;
    }

    @Override
    public int hashCode()
    {
        int result = user != null ? user.hashCode() : 0;
        result = 31 * result + (friends != null ? friends.hashCode() : 0);
        return result;
    }

    public String toJson() throws Exception
    {
        return new ObjectMapper().writeValueAsString( this );
    }

    public static String toJsonArray( FriendResult[] objs ) throws Exception
    {
        return new ObjectMapper().writeValueAsString( objs );
    }

    public static FriendResult fromJson( String json ) throws Exception
    {
        return new ObjectMapper().readValue( json, FriendResult.class );
    }

    public static FriendResult[] fromJsonArray( String json ) throws Exception
    {
        return new ObjectMapper().readValue( json, FriendResult[].class );
    }

}
