package org.neo4j.extensions.spring.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.neo4j.extensions.spring.common.RelationshipConstants;
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
import java.util.List;
import java.util.Set;

/**
 * A User has been authenticated and owns and has access to information.
 *
 * @author bradnussbaum
 * @version 1.0.0
 * @since 1.0.0
 */
@NodeEntity
@XmlRootElement
@JsonAutoDetect
public class User implements Serializable {

    private static final long serialVersionUID = -1040756095656494942L;

    /**
     * The Node ID is volatile.
     */
    @GraphId
    private Long id;

    /**
     * The version always starts at 1.
     */
    private Integer version = 1;

    /**
     * Auto-generated unique identifier.
     */
    @Indexed(indexType = IndexType.UNIQUE, indexName = "uids", numeric = false)
    private String uid;

    @CreatedDate
    private Long createdTime;

    @CreatedBy
    private String createdBy;

    @LastModifiedDate
    @Indexed(indexType = IndexType.SIMPLE, indexName = "user_exact")
    private Long lastModifiedTime;

    @LastModifiedBy
    private String lastModifiedBy;

    private String type;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String username;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String email;

    private String password;

    @Indexed(indexType = IndexType.FULLTEXT, indexName = "user_fulltext")
    private String name;

    private Boolean active;

    private Boolean validated;

    @Indexed(indexType = IndexType.UNIQUE, indexName = "identifiers", numeric = false)
    private String identifier;

    @RelatedTo(type = RelationshipConstants.FRIEND_OF, direction = Direction.OUTGOING)
    private Set<User> friends;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public Set<User> getFriends() {
        return friends;
    }

    public void setFriends(Set<User> friends) {
        this.friends = friends;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;

        User user = (User) o;

        if (active != null ? !active.equals(user.active) : user.active != null) return false;
        if (createdBy != null ? !createdBy.equals(user.createdBy) : user.createdBy != null) return false;
        if (createdTime != null ? !createdTime.equals(user.createdTime) : user.createdTime != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        if (friends != null ? !friends.equals(user.friends) : user.friends != null) return false;
        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (identifier != null ? !identifier.equals(user.identifier) : user.identifier != null) return false;
        if (lastModifiedBy != null ? !lastModifiedBy.equals(user.lastModifiedBy) : user.lastModifiedBy != null)
            return false;
        if (lastModifiedTime != null ? !lastModifiedTime.equals(user.lastModifiedTime) : user.lastModifiedTime != null)
            return false;
        if (name != null ? !name.equals(user.name) : user.name != null) return false;
        if (password != null ? !password.equals(user.password) : user.password != null) return false;
        if (type != null ? !type.equals(user.type) : user.type != null) return false;
        if (uid != null ? !uid.equals(user.uid) : user.uid != null) return false;
        if (username != null ? !username.equals(user.username) : user.username != null) return false;
        if (validated != null ? !validated.equals(user.validated) : user.validated != null) return false;
        if (version != null ? !version.equals(user.version) : user.version != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (version != null ? version.hashCode() : 0);
        result = 31 * result + (uid != null ? uid.hashCode() : 0);
        result = 31 * result + (createdTime != null ? createdTime.hashCode() : 0);
        result = 31 * result + (createdBy != null ? createdBy.hashCode() : 0);
        result = 31 * result + (lastModifiedTime != null ? lastModifiedTime.hashCode() : 0);
        result = 31 * result + (lastModifiedBy != null ? lastModifiedBy.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (active != null ? active.hashCode() : 0);
        result = 31 * result + (validated != null ? validated.hashCode() : 0);
        result = 31 * result + (identifier != null ? identifier.hashCode() : 0);
        return result;
    }

}
