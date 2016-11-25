package org.neo4j.extensions.java.rest;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.Properties;
import java.util.logging.Logger;

import org.neo4j.extensions.common.domain.FriendResult;
import org.neo4j.harness.ServerControls;
import org.neo4j.harness.TestServerBuilders;
import org.neo4j.test.server.HTTP;

/**
 * @author bradnussbaum
 * @since 2014-05-25
 */
public class UserControllerTest
{

    private static final Logger LOGGER = Logger.getLogger( UserControllerTest.class.getName() );

    Properties properties = new Properties();
    InputStream input = null;

    private String dbmsConnectorHttpPort = System.getProperty( "dbms.connector.http.port" );

    private String dbmsConnectorBoltPort = System.getProperty( "dbms.connector.bolt.port" );

    private String dbmsShellPort = System.getProperty( "dbms.shell.port" );

    private String dbmsDirectoriesData = System.getProperty( "dbms.directories.data" );

    private ServerControls server;

    @Before
    public void before() throws IOException
    {
        try
        {
            input = UserControllerTest.class.getClassLoader()
                    .getResourceAsStream( "neo4j-extensions-java-maven.properties" );
            // load the properties file
            properties.load( input );
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
        finally
        {
            if ( input != null )
            {
                try
                {
                    input.close();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                }
            }
        }

        // allow system properties to override
        if ( dbmsConnectorHttpPort == null )
        {
            dbmsConnectorHttpPort = properties.getProperty( "dbms.connector.http.port" );
        }
        if ( dbmsConnectorBoltPort == null )
        {
            dbmsConnectorBoltPort = properties.getProperty( "dbms.connector.bolt.port" );
        }
        if ( dbmsShellPort == null )
        {
            dbmsShellPort = properties.getProperty( "dbms.shell.port" );
        }
        if ( dbmsDirectoriesData == null )
        {
            dbmsDirectoriesData = properties.getProperty( "dbms.directories.data" );
        }

        LOGGER.info( String.format( "dbms.connector.http.port: %s", dbmsConnectorHttpPort ) );
        LOGGER.info( String.format( "dbms.connector.bolt.port: %s", dbmsConnectorBoltPort ) );
        LOGGER.info( String.format( "dbms.shell.port: %s", dbmsShellPort ) );
        LOGGER.info( String.format( "dbms.directories.data: %s", dbmsDirectoriesData ) );

        server = TestServerBuilders.newInProcessBuilder()
                .withExtension( "/extensions-java", "org.neo4j.extensions.java" )
                .withConfig( "dbms.connector.0.address", "localhost:" + String.valueOf( dbmsConnectorBoltPort ) )
                .withConfig( "dbms.connector.0.enabled", Boolean.TRUE.toString() )
                .withConfig( "dbms.connector.0.encryption", "DISABLED" ).withConfig( "dbms.connector.0.type", "BOLT" )

                .withConfig( "dbms.connector.1.address", "localhost:" + String.valueOf( dbmsConnectorHttpPort ) )
                .withConfig( "dbms.connector.1.enabled", Boolean.TRUE.toString() )
                .withConfig( "dbms.connector.1.encryption", "NONE" ).withConfig( "dbms.connector.1.type", "HTTP" )
                .withConfig( "dbms.memory.pagecache.size", "128m" )
                .withConfig( "dbms.shell.port", String.valueOf( dbmsShellPort ) )
                .withConfig( "dbms.directories.data", dbmsDirectoriesData ).newServer();

        LOGGER.info( server.httpURI().toString() );
    }

    @After
    public void after()
    {
        if ( server != null )
        {
            server.close();
        }
    }

    @Test
    public void testCreateUser() throws Exception
    {
        URI requestUri = server.httpURI().resolve( "extensions-java/user/create?indexingOn=true" );

        HTTP.Response response = HTTP.POST( requestUri.toString() );
        FriendResult result = FriendResult.fromJson( response.rawContent() );

        Assert.assertNotNull( result.getUser() );
        Assert.assertNotNull( result.getFriends() );
    }


}
