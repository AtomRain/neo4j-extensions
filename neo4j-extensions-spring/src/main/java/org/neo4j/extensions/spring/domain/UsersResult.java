package org.neo4j.extensions.spring.domain;

import java.io.Serializable;
import java.util.Set;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.map.annotate.JsonView;

import org.neo4j.extensions.common.client.EntityView;

/**
 * Friend result.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@XmlRootElement
@JsonAutoDetect(JsonMethod.FIELD)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UsersResult implements Serializable
{

    private static final long serialVersionUID = 1539779889606280663L;

    @JsonView(EntityView.class)
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

}
