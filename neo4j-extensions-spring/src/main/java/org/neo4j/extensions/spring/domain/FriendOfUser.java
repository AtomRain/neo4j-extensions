package org.neo4j.extensions.spring.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.neo4j.extensions.spring.common.RelationshipConstants;
import org.neo4j.graphdb.RelationshipType;
import org.springframework.data.neo4j.annotation.EndNode;
import org.springframework.data.neo4j.annotation.GraphId;
import org.springframework.data.neo4j.annotation.RelationshipEntity;
import org.springframework.data.neo4j.annotation.StartNode;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

/**
 * Friend of User relationship.
 *
 * @author bradnussbaum
 * @version 1.0.0
 * @since 1.0.0
 */
@XmlRootElement
@JsonAutoDetect
@RelationshipEntity(type = RelationshipConstants.FRIEND_OF)
public class FriendOfUser implements RelationshipType, Serializable {

    private static final long serialVersionUID = -5433623217015754491L;

    @GraphId
    private Long id;

    @StartNode
    private User user;

    @EndNode
    private User friend;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getFriend() {
        return friend;
    }

    public void setFriend(User friend) {
        this.friend = friend;
    }

    public String name() {
        return RelationshipConstants.FRIEND_OF;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof FriendOfUser)) return false;

        FriendOfUser that = (FriendOfUser) o;

        if (friend != null ? !friend.equals(that.friend) : that.friend != null) return false;
        if (id != null ? !id.equals(that.id) : that.id != null) return false;
        if (user != null ? !user.equals(that.user) : that.user != null) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (friend != null ? friend.hashCode() : 0);
        return result;
    }
}
