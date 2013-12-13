package org.neo4j.extensions.java.common;

/**
 * Node types.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
public enum NodeTypes {

    USER;

    public String className() {
        switch (this) {
            case USER:
                return "com.neo4j.enterprise.extensions.domain.User";
            default:
                return null;
        }
    }

    public String simpleClassName() {
        switch (this) {
            case USER:
                return "User";
            default:
                return null;
        }
    }

}
