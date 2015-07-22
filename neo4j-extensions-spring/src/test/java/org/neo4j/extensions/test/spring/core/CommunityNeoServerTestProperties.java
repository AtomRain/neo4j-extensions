package org.neo4j.extensions.test.spring.core;

import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * @author bradnussbaum
 * @since 2014.09.17
 */
@Component
@Configurable
public class CommunityNeoServerTestProperties
{

    @Value( "${neo4j.server.port}" )
    private Integer neo4jServerPort;

    @Value( "${neo4j.remoteShell.port}" )
    private Integer neo4jRemoteShellPort;

    @Value( "${neo4j.graph.db}" )
    private String neo4jGraphDb;

    public Integer getNeo4jServerPort()
    {
        return neo4jServerPort;
    }

    public void setNeo4jServerPort( Integer neo4jServerPort )
    {
        this.neo4jServerPort = neo4jServerPort;
    }

    public Integer getNeo4jRemoteShellPort()
    {
        return neo4jRemoteShellPort;
    }

    public void setNeo4jRemoteShellPort( Integer neo4jRemoteShellPort )
    {
        this.neo4jRemoteShellPort = neo4jRemoteShellPort;
    }

    public String getNeo4jGraphDb()
    {
        return neo4jGraphDb;
    }

    public void setNeo4jGraphDb( String neo4jGraphDb )
    {
        this.neo4jGraphDb = neo4jGraphDb;
    }
}
