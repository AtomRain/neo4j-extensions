package org.neo4j.extensions.test.spring.rest;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;

import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.UsersResult;
import org.neo4j.extensions.test.spring.core.CommunityNeoServerTestFactory;
import org.neo4j.extensions.test.spring.core.CommunityNeoServerTestInstance;
import org.neo4j.test.server.HTTP;

/**
 * @author bradnussbaum
 * @since 2014-05-25
 */
@Configurable
@ContextConfiguration( "classpath:META-INF/spring/test-springContext.xml" )
@RunWith( SpringJUnit4ClassRunner.class )
public class UserControllerTest
{

    private CommunityNeoServerTestInstance neo;

    @Before
    public void before()
    {
        neo = CommunityNeoServerTestFactory.getInstance();
    }

    @Test
    public void testCreateUser() throws Exception
    {
        URI requestUri = neo.getServer().httpURI().resolve( "extensions-spring/user/create?indexingOn=true" );

        HTTP.Response response = HTTP.POST( requestUri.toString() );

        FriendResult result = FriendResult.fromJson( response.rawContent() );

        Assert.assertNotNull( result.getUser() );
        Assert.assertNotNull( result.getFriends() );
    }

    @Test
    public void testFindUsers() throws Exception
    {
        URI requestUri =
                neo.getServer().httpURI().resolve( "extensions-spring/user/pages?page=0&page.size=10&pages=10" );

        HTTP.Response response = HTTP.GET( requestUri.toString() );

        UsersResult result = UsersResult.fromJson( response.rawContent() );

        Assert.assertNotNull( result );
    }

}
