package org.neo4j.extensions.spring.domain;

import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Friend result.
 * 
 * 
 * @author bradnussbaum
 * @version 1.0.0
 * 
 * @since 1.0.0
 * 
 */
@XmlRootElement
public class FriendResult {

    private List<User> friends;

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }

}
