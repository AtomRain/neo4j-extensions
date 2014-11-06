package org.neo4j.extensions.common.types;

import org.neo4j.extensions.common.domain.User;

/**
 * Node types.
 *
 * @author bradnussbaum
 * @since 2014.05.25
 */
public enum NodeTypes
{

    USER( User.class.getName(), User.class.getSimpleName() );

    private String className;

    private String simpleClassName;

    private NodeTypes( String className, String simpleClassName )
    {
        this.className = className;
        this.simpleClassName = simpleClassName;
    }

    public String getClassName()
    {
        return className;
    }

    public String getSimpleClassName()
    {
        return simpleClassName;
    }

}
