package org.neo4j.extensions.spring.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.neo4j.extensions.common.types.RelationshipConstants;
import org.neo4j.graphdb.Direction;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.Indexed;
import org.springframework.data.neo4j.annotation.NodeEntity;
import org.springframework.data.neo4j.annotation.RelatedTo;
import org.springframework.data.neo4j.support.index.IndexType;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collection;

/**
 * A User has been authenticated and owns and has access to information.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
@XmlRootElement
@JsonAutoDetect
@NodeEntity
public class User implements Serializable, Comparable<User> {

    private static final long serialVersionUID = 678183622990845243L;

    @GraphId
    private Long id;

    @CreatedDate
    private Long createdTime;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @Indexed(indexType = IndexType.LABEL)
    private Long lastModifiedTime;

    @LastModifiedBy
    private String lastModifiedBy;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String username;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String email;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String name;

    @RelatedTo(type = RelationshipConstants.FRIEND_OF, direction = Direction.OUTGOING)
    private Collection<User> friends;

    /**
     * The version always starts at 1.
     */
    private Integer version = 1;

    private String type;

    private String password;

    private Boolean active;

    private Boolean validated;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Long createdTime) {
        this.createdTime = createdTime;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Long getLastModifiedTime() {
        return lastModifiedTime;
    }

    public void setLastModifiedTime(Long lastModifiedTime) {
        this.lastModifiedTime = lastModifiedTime;
    }

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Collection<User> getFriends() {
        return friends;
    }

    public void setFriends(Collection<User> friends) {
        this.friends = friends;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getValidated() {
        return validated;
    }

    public void setValidated(Boolean validated) {
        this.validated = validated;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (obj.getClass() != getClass()) {
            return false;
        }
        User rhs = (User) obj;
        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(this.id, rhs.id)
                .append(this.version, rhs.version)
                .append(this.createdTime, rhs.createdTime)
                .append(this.createdBy, rhs.createdBy)
                .append(this.lastModifiedTime, rhs.lastModifiedTime)
                .append(this.lastModifiedBy, rhs.lastModifiedBy)
                .append(this.type, rhs.type)
                .append(this.username, rhs.username)
                .append(this.email, rhs.email)
                .append(this.password, rhs.password)
                .append(this.name, rhs.name)
                .append(this.active, rhs.active)
                .append(this.validated, rhs.validated)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder()
                .append(id)
                .append(version)
                .append(createdTime)
                .append(createdBy)
                .append(lastModifiedTime)
                .append(lastModifiedBy)
                .append(type)
                .append(username)
                .append(email)
                .append(password)
                .append(name)
                .append(active)
                .append(validated)
                .toHashCode();
    }

    @Override
    public int compareTo(User rhs) {
        if (this.id > rhs.id) {
            return 1;
        } else if (this.id < rhs.id) {
            return -1;
        } else {
            return 0;
        }
    }
}
