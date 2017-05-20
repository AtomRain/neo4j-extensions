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

        LOGGER.info( String.format( "dbms.connector.http.port: %d", properties.getDbmsConnectorHttpPort() ) );
        LOGGER.info( String.format( "dbms.connector.bolt.port: %d", properties.getDbmsConnectorBoltPort() ) );
        LOGGER.info( String.format( "dbms.shell.port: %d", properties.getDbmsShellPort() ) );
        LOGGER.info( String.format( "dbms.directories.data: %s", properties.getDbmsDirectoriesData() ) );

        server = TestServerBuilders.newInProcessBuilder()
                .withExtension( "/extensions-spring", "org.neo4j.extensions.spring" )
                .withConfig( "dbms.connector.bolt.address",
                        "localhost:" + String.valueOf( properties.getDbmsConnectorBoltPort() ) )
                .withConfig( "dbms.connector.bolt.enabled", Boolean.TRUE.toString() )

                .withConfig( "dbms.connector.http.address",
                        "localhost:" + String.valueOf( properties.getDbmsConnectorHttpPort() ) )
                .withConfig( "dbms.connector.http.enabled", Boolean.TRUE.toString() )
                .withConfig( "dbms.memory.pagecache.size", "128m" )
                .withConfig( "dbms.shell.port", String.valueOf( properties.getDbmsShellPort() ) )
                .withConfig( "dbms.directories.data", properties.getDbmsDirectoriesData() ).newServer();

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
