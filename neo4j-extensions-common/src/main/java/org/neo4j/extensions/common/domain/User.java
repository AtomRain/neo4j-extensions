package org.neo4j.extensions.common.domain;

import java.io.Serializable;
import java.util.Collection;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.annotate.JsonAutoDetect;

/**
 * A User has been authenticated and owns and has access to information.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@XmlRootElement
@JsonAutoDetect
public class User implements Serializable, Comparable<User>
{

    private static final long serialVersionUID = 678183622990845243L;

    /**
     * The Node ID is volatile.
     */
    private Long id;

    /**
     * The version always starts at 1.
     */
    private Integer version = 1;

    private Long createdTime;

    private String createdBy;

    private Long lastModifiedTime;

    private String lastModifiedBy;

    private String type;

    private String username;

    private String email;

    private String password;

    private String name;

    private Boolean active;

    private Boolean validated;

    private Collection<User> friends;

    public Long getId()
    {
        return id;
    }

    public void setId( Long id )
    {
        this.id = id;
    }

    public Integer getVersion()
    {
        return version;
    }

    public void setVersion( Integer version )
    {
        this.version = version;
    }

    public Long getCreatedTime()
    {
        return createdTime;
    }

    public void setCreatedTime( Long createdTime )
    {
        this.createdTime = createdTime;
    }

    public String getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy( String createdBy )
    {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedTime()
    {
        return lastModifiedTime;
    }

    public void setLastModifiedTime( Long lastModifiedTime )
    {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedBy()
    {
        return lastModifiedBy;
    }

    public void setLastModifiedBy( String lastModifiedBy )
    {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getType()
    {
        return type;
    }

    public void setType( String type )
    {
        this.type = type;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername( String username )
    {
        this.username = username;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail( String email )
    {
        this.email = email;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword( String password )
    {
        this.password = password;
    }

    public String getName()
    {
        return name;
    }

    public void setName( String name )
    {
        this.name = name;
    }

    public Boolean getActive()
    {
        return active;
    }

    public void setActive( Boolean active )
    {
        this.active = active;
    }

    public Boolean getValidated()
    {
        return validated;
    }

    public void setValidated( Boolean validated )
    {
        this.validated = validated;
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
        User rhs = (User) obj;
        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append( this.id, rhs.id )
                .append( this.version, rhs.version )
                .append( this.createdTime, rhs.createdTime )
                .append( this.createdBy, rhs.createdBy )
                .append( this.lastModifiedTime, rhs.lastModifiedTime )
                .append( this.lastModifiedBy, rhs.lastModifiedBy )
                .append( this.type, rhs.type )
                .append( this.username, rhs.username )
                .append( this.email, rhs.email )
                .append( this.password, rhs.password )
                .append( this.name, rhs.name )
                .append( this.active, rhs.active )
                .append( this.validated, rhs.validated )
                .isEquals();
    }

    @Override
    public int hashCode()
    {
        return new org.apache.commons.lang3.builder.HashCodeBuilder()
                .append( id )
                .append( version )
                .append( createdTime )
                .append( createdBy )
                .append( lastModifiedTime )
                .append( lastModifiedBy )
                .append( type )
                .append( username )
                .append( email )
                .append( password )
                .append( name )
                .append( active )
                .append( validated )
                .toHashCode();
    }

    @Override
    public int compareTo( User o )
    {
        if ( this.getId() > o.getId() )
        {
            return 1;
        }
        else if ( this.getId() < o.getId() )
        {
            return -1;
        }
        else
        {
            return 0;
        }
    }
}
