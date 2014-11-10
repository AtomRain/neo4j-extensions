package org.neo4j.extensions.spring.domain;

import java.io.Serializable;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.annotate.JsonView;

import org.neo4j.extensions.common.client.UserTinyView;

/**
 * Friend result.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@JsonAutoDetect
@JsonIgnoreProperties(ignoreUnknown = true)
@XmlRootElement
public class FriendResult implements Serializable
{

    private static final long serialVersionUID = 1539779889606280663L;

    @JsonView(UserTinyView.class)
    private User user;

    @JsonView(UserTinyView.class)
    private Set<User> friends;

    public User getUser()
    {
        return user;
    }

    public void setUser( User user )
    {
        this.user = user;
    }

    public Set<User> getFriends()
    {
        return friends;
    }

    public void setFriends( Set<User> friends )
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

    @Override
    public String toString()
    {
        return new ToStringBuilder( this )
                .append( "user", user )
                .append( "friends", friends )
                .toString();
    }

}
