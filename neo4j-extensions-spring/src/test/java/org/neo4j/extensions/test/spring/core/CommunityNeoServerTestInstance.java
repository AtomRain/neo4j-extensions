package org.neo4j.extensions.test.spring.core;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;

import java.io.IOException;
import java.util.logging.Logger;

import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;

/**
 * @author bradnussbaum
 * @since 2014.09.17
 */
@Configurable
public class CommunityNeoServerTestInstance implements InitializingBean, DisposableBean
{

    private static final Logger LOGGER = Logger.getLogger( CommunityNeoServerTestInstance.class.getName() );

    private ServerControls server;

    @Autowired
    private CommunityNeoServerTestProperties properties;

    public ServerControls getServer()
    {
        return server;
    }

    public void start() throws IOException
    {
        if ( server != null )
        {
            return;
        }

        LOGGER.info( String.format( "neo4jServerPort: %d", properties.getNeo4jServerPort() ) );
        LOGGER.info( String.format( "neo4jRemoteShellPort: %d", properties.getNeo4jRemoteShellPort() ) );
        LOGGER.info( String.format( "neo4jGraphDb: %s", properties.getNeo4jGraphDb() ) );

        server = TestServerBuilders.newInProcessBuilder()
                .withExtension( "/extensions-spring", "org.neo4j.extensions.spring" )
                .withConfig( "org.neo4j.server.webserver.port", String.valueOf( properties.getNeo4jServerPort() ) )
                .withConfig( "org.neo4j.server.webserver.https.enabled", Boolean.FALSE.toString() )
                .withConfig( "remote_shell_port", String.valueOf( properties.getNeo4jRemoteShellPort() ) )
                .withConfig( "org.neo4j.server.database.location", properties.getNeo4jGraphDb() )
                .newServer();

        LOGGER.info( server.httpURI().toString() );
    }

    public void stop()
    {
        if ( server != null )
        {
            server.close();
        }
    }

    @Override
    public void afterPropertiesSet() throws Exception
    {
        start();
    }

    @Override
    public void destroy() throws Exception
    {
        stop();
    }
}
