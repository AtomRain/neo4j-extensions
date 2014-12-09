package org.neo4j.extensions.test.spring.core;

import java.io.IOException;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.server.CommunityNeoServer;
import org.neo4j.server.helpers.CommunityServerBuilder;

/**
 * @author bradnussbaum
 * @since 2014.09.17
 */
@Configurable
public class CommunityNeoServerTestInstance
{

    private static final Logger LOGGER = Logger.getLogger( CommunityNeoServerTestInstance.class.getName() );

    private GraphDatabaseService db;
    private CommunityNeoServer server;

    @Autowired
    private CommunityNeoServerTestProperties properties;

    public GraphDatabaseService getDb()
    {
        return db;
    }

    public CommunityNeoServer getServer()
    {
        return server;
    }

    public void start() throws IOException
    {
        LOGGER.info( String.format( "neo4jServerPort: %d", properties.getNeo4jServerPort() ) );
        LOGGER.info( String.format( "neo4jRemoteShellPort: %d", properties.getNeo4jRemoteShellPort() ) );
        LOGGER.info( String.format( "neo4jGraphDb: %s", properties.getNeo4jGraphDb() ) );

        server = CommunityServerBuilder
                .server()
                .usingDatabaseDir( properties.getNeo4jGraphDb() )
                .onPort( properties.getNeo4jServerPort() )
                .withProperty( "remote_shell_port", String.valueOf( properties.getNeo4jRemoteShellPort() ) )
                .withDefaultDatabaseTuning()
                .withThirdPartyJaxRsPackage( "org.neo4j.extensions.spring", "/extensions-spring" )
                .build();
        server.start();
        db = server.getDatabase().getGraph();
    }

    public void stop()
    {
        if ( server != null )
        {
            server.stop();
        }
    }

}
