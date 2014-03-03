package org.neo4j.extensions.spring.common;

import org.neo4j.extensions.spring.domain.User;

/**
 * Node types.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
public enum NodeTypes {

    USER(User.class.getName(), User.class.getSimpleName());

    private String className;

    private String simpleClassName;

    private NodeTypes(String className, String simpleClassName) {
        this.className = className;
        this.simpleClassName = simpleClassName;
    }

    public String getClassName() {
        return className;
    }

    public String getSimpleClassName() {
        return simpleClassName;
    }

}
