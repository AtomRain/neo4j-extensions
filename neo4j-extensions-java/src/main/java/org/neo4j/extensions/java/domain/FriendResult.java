package org.neo4j.extensions.java.domain;

import java.util.List;

import javax.xml.bind.annotation.XmlRootElement;

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
