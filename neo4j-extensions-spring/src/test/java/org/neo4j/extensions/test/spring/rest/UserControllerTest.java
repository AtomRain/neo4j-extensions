package org.neo4j.extensions.test.spring.rest;

import java.io.InputStream;
import java.util.logging.Logger;
import javax.ws.rs.core.MediaType;

import com.sun.jersey.api.client.Client;
import com.sun.jersey.api.client.ClientResponse;
import com.sun.jersey.api.client.config.DefaultClientConfig;
import org.codehaus.jackson.jaxrs.JacksonJsonProvider;
import org.codehaus.jackson.map.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.ApplicationContext;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.UsersResult;
import org.neo4j.extensions.test.spring.core.CommunityNeoServerTestInstance;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

/**
 * @author bradnussbaum
 * @since 2014-05-25
 */
@Configurable
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
@ContextConfiguration("classpath:META-INF/spring/test-springContext.xml")
@RunWith(SpringJUnit4ClassRunner.class)
public class UserControllerTest
{

    private static final Logger LOGGER = Logger.getLogger( UserControllerTest.class.getName() );

    @Autowired
    private ApplicationContext ctx;

    private CommunityNeoServerTestInstance communityNeoServer;

    @Before
    public void before()
    {
        communityNeoServer = ctx.getBean( CommunityNeoServerTestInstance.class );
    }

    @Test
    public void testCreateUser() throws Exception
    {

        ClientResponse response = jerseyClient()
                .resource( communityNeoServer.getServer().baseUri().toString() +
                        "extensions-spring/user/create?indexingOn=true" )
                .accept( MediaType.APPLICATION_JSON )
                .post( ClientResponse.class );
        InputStream streamingOutput = response.getEntityInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        FriendResult result = objectMapper.readValue( streamingOutput, FriendResult.class );

        Assert.assertNotNull( result.getUser() );
        Assert.assertNotNull( result.getFriends() );

        try ( Transaction tx = communityNeoServer.getDb().beginTx() )
        {
            Node node = communityNeoServer.getDb().getNodeById( result.getUser().getId() );
            Assert.assertNotNull( node.getProperty( "createdTime" ).toString() );
            Assert.assertEquals( node.getProperty( "createdTime" ).toString(), result.getUser().getCreatedTime()
                    .toString() );
            tx.success();
        }
        catch ( Exception e )
        {
            // intentionally blank
        }
    }

    @Test
    public void testFindUsers() throws Exception
    {

        ClientResponse response = jerseyClient()
                .resource( communityNeoServer.getServer().baseUri().toString() +
                        "extensions-spring/user/pages?page=0&page.size=10&pages=10" )
                .accept( MediaType.APPLICATION_JSON )
                .get( ClientResponse.class );
        InputStream streamingOutput = response.getEntityInputStream();
        ObjectMapper objectMapper = new ObjectMapper();
        UsersResult result = objectMapper.readValue( streamingOutput, UsersResult.class );
        Assert.assertNotNull( result );
    }

    private Client jerseyClient()
    {
        DefaultClientConfig defaultClientConfig = new DefaultClientConfig();
        defaultClientConfig.getClasses().add( JacksonJsonProvider.class );
        return Client.create( defaultClientConfig );
    }

}
