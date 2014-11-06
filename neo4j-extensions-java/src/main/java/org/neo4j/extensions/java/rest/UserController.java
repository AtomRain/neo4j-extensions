package org.neo4j.extensions.java.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.neo4j.extensions.common.client.UserClient;
import org.neo4j.extensions.common.domain.FriendResult;
import org.neo4j.extensions.common.domain.User;
import org.neo4j.extensions.common.schema.indexes.UserIndex;
import org.neo4j.extensions.common.types.LabelTypes;
import org.neo4j.extensions.common.types.RelationshipTypes;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

/**
 * The User controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/user")
public class UserController implements UserClient
{

    private static final Logger LOGGER = Logger.getLogger( UserController.class.getName() );

    @Context
    private GraphDatabaseService db;

    public Response create( Boolean indexingOn, Integer count )
    {
        LOGGER.info( String.format( "POST /user/create?indexingOn=%s&count=%d", indexingOn, count ) );
        long startTimeTx = System.currentTimeMillis();

        // create user
        User user = new User();
        // create friend 1
        User friend1 = new User();
        // create friend 2
        User friend2 = new User();

        try ( Transaction tx = db.beginTx() )
        {
            // establish created time
            Long createdTime = System.currentTimeMillis();

            // create user node
            Node userNode = db.createNode( LabelTypes.User );
            Long userId = userNode.getId();
            user.setId( userId );
            // establish created time
            userNode.setProperty( "createdTime", createdTime );
            user.setCreatedTime( createdTime );
            // establish username
            String username = UUID.randomUUID().toString();
            userNode.setProperty( "username", username );
            user.setUsername( username );

            // create friend 1 node
            Node friend1Node = db.createNode( LabelTypes.User );
            Long friend1Id = friend1Node.getId();
            friend1.setId( friend1Id );
            // establish created time
            friend1Node.setProperty( "createdTime", createdTime );
            friend1.setCreatedTime( createdTime );
            // establish username
            String friend1Username = UUID.randomUUID().toString();
            friend1Node.setProperty( "username", friend1Username );
            friend1.setUsername( friend1Username );

            // create friend 2 node
            Node friend2Node = db.createNode( LabelTypes.User );
            Long friend2Id = friend2Node.getId();
            friend2.setId( friend2Id );
            // establish created time
            friend2Node.setProperty( "createdTime", createdTime );
            friend2.setCreatedTime( createdTime );
            // establish username
            String friend2Username = UUID.randomUUID().toString();
            friend2Node.setProperty( "username", friend2Username );
            friend2.setUsername( friend2Username );

            // user friend of friend1
            userNode.createRelationshipTo( friend1Node, RelationshipTypes.FRIEND_OF );
            // user friend of friend2
            userNode.createRelationshipTo( friend2Node, RelationshipTypes.FRIEND_OF );

            // check for indexing
            if ( indexingOn )
            {
                // put to index
                Index<Node> usernameIndex = db.index().forNodes( UserIndex.username.getIndexName(),
                        UserIndex.username.getIndexType() );
                usernameIndex.putIfAbsent( userNode, UserIndex.username.name(), username );
                usernameIndex.putIfAbsent( friend1Node, UserIndex.username.name(), friend1Username );
                usernameIndex.putIfAbsent( friend2Node, UserIndex.username.name(), friend2Username );
            }

            // capture times
            long successTimeTx = System.currentTimeMillis();
            long processTimeTx = successTimeTx - startTimeTx;

            tx.success();

            // log details
            LOGGER.log( Level.INFO,
                    String.format( "UserController: TX: userId=%s, username=%s, processTime=%dms", userId, username,
                            processTimeTx ) );

            // establish friends for user
            List<User> friends = new ArrayList<>();
            friends.add( friend1 );
            friends.add( friend2 );
            user.setFriends( friends );

            // assemble result package
            FriendResult result = new FriendResult();
            result.setUser( user );
            result.setFriends( friends );

            return Response.status( Status.CREATED ).entity( result ).build();
        }
        catch ( Exception e )
        {
            // log details
            LOGGER.log( Level.SEVERE, e.getMessage() );
            return Response.serverError().build();
        }
    }

    @Override
    public Response findUsers( Integer page, Integer pageSize, Integer pages )
    {
        return Response.ok().build();
    }
}
