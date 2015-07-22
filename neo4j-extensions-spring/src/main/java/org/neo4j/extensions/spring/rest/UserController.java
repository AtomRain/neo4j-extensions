package org.neo4j.extensions.spring.rest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Set;
import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.neo4j.extensions.common.client.UserClient;
import org.neo4j.extensions.common.client.UserFullView;
import org.neo4j.extensions.common.client.UserTinyView;
import org.neo4j.extensions.spring.domain.FriendResult;
import org.neo4j.extensions.spring.domain.User;
import org.neo4j.extensions.spring.domain.UsersResult;
import org.neo4j.extensions.spring.service.UserService;

/**
 * The User controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Controller
@Path( "/user" )
public class UserController implements UserClient
{

    private static final Logger LOGGER = Logger.getLogger( UserController.class.getName() );

    @Context
    private UserService userService;

    /**
     * @return Status 201 on success.
     */
    @Transactional
    public Response create( Boolean indexingOn, Integer count )
    {
        LOGGER.info( String.format( "POST /user/create?indexingOn=%s&count=%d", indexingOn, count ) );
        long startTimeTx = System.currentTimeMillis();

        User user = userService.createUser( indexingOn, count );

        FriendResult result = new FriendResult();
        result.setUser( user );
        result.setFriends( user.getFriends() );

        // capture times
        long successTimeTx = System.currentTimeMillis();
        long processTimeTx = successTimeTx - startTimeTx;

        // log details
        LOGGER.info( String.format( "UserController: TX: userId=%s, processTime=%dms", user.getId(), processTimeTx ) );

        return Response.status( Response.Status.CREATED ).entity( writeEntity( result, new Class[]{UserFullView
                .class} ) ).build();
    }

    /**
     * @return Status 200 on success.
     */
    @Transactional
    public Response findUsers( Integer page, Integer pageSize, Integer pages )
    {
        LOGGER.info( String.format( "POST /user/pages?page=%d&page.size=%d&pages=%d", page, pageSize, pages ) );
        try
        {
            Set<User> users = userService.findUsers( page, pageSize, pages );
            UsersResult result = new UsersResult();
            result.setUsers( users );
            return Response.ok( writeEntity( result, new Class[]{UserTinyView.class} ) ).build();
        }
        catch ( Exception e )
        {
            throw new WebApplicationException( e );
        }
    }

    @Transactional
    private StreamingOutput writeEntity( final Object entity, final Class[] views )
    {
        return new StreamingOutput()
        {
            @Transactional
            @Override
            public void write( OutputStream output ) throws IOException, WebApplicationException
            {
                ObjectMapper mapper = new ObjectMapper();
                SerializationConfig config = mapper.getSerializationConfig();
                for ( Class view : views )
                {
                    config = config.withView( view );
                }
                mapper.setSerializationConfig( config );
                mapper.writeValue( output, entity );
            }
        };
    }

}
