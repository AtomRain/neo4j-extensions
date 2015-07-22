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

    private String neo4jServerPort = System.getProperty( "neo4j.server.port" );

    private String neo4jRemoteShellPort = System.getProperty( "neo4j.remoteShell.port" );

    private String neo4jGraphDb = System.getProperty( "neo4j.graph.db" );

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
        if ( neo4jServerPort == null )
        {
            neo4jServerPort = properties.getProperty( "neo4j.server.port" );
        }
        if ( neo4jRemoteShellPort == null )
        {
            neo4jRemoteShellPort = properties.getProperty( "neo4j.remoteShell.port" );
        }
        if ( neo4jGraphDb == null )
        {
            neo4jGraphDb = properties.getProperty( "neo4j.graph.db" );
        }

        LOGGER.info( String.format( "neo4jServerPort: %s", neo4jServerPort ) );
        LOGGER.info( String.format( "neo4jRemoteShellPort: %s", neo4jRemoteShellPort ) );
        LOGGER.info( String.format( "neo4jGraphDb: %s", neo4jGraphDb ) );

        server = TestServerBuilders.newInProcessBuilder()
                .withExtension( "/extensions-java", "org.neo4j.extensions.java" )
                .withConfig( "org.neo4j.server.webserver.port", neo4jServerPort )
                .withConfig( "org.neo4j.server.webserver.https.enabled", Boolean.FALSE.toString() )
                .withConfig( "remote_shell_port", neo4jRemoteShellPort )
                .withConfig( "org.neo4j.server.database.location", neo4jGraphDb )
                .newServer();

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
