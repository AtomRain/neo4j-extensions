package org.neo4j.extensions.spring.domain;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.annotate.JsonView;

import java.io.Serializable;
import java.util.Set;

import org.neo4j.extensions.common.client.UserTinyView;

/**
 * Friend result.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@JsonAutoDetect
@JsonIgnoreProperties( ignoreUnknown = true )
public class UsersResult implements Serializable
{

    private static final long serialVersionUID = 1539779889606280663L;

    @JsonView( UserTinyView.class )
    private Set<User> users;

    public Set<User> getUsers()
    {
        return users;
    }

    public void setUsers( Set<User> users )
    {
        this.users = users;
    }

    @Override
    public String toString()
    {
        return new ToStringBuilder( this )
                .append( "users", users )
                .toString();
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
        UsersResult rhs = (UsersResult) obj;
        return new EqualsBuilder()
                .append( this.users, rhs.users )
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new HashCodeBuilder()
                .append( users )
                .toHashCode();
    }

    public String toJson() throws Exception
    {
        return new ObjectMapper().writeValueAsString( this );
    }

    public static String toJsonArray( UsersResult[] objs ) throws Exception
    {
        return new ObjectMapper().writeValueAsString( objs );
    }

    public static UsersResult fromJson( String json ) throws Exception
    {
        return new ObjectMapper().readValue( json, UsersResult.class );
    }

    public static UsersResult[] fromJsonArray( String json ) throws Exception
    {
        return new ObjectMapper().readValue( json, UsersResult[].class );
    }

}
