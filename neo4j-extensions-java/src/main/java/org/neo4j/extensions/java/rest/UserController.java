package org.neo4j.extensions.java.rest;

import org.neo4j.extensions.java.common.LabelTypes;
import org.neo4j.extensions.java.common.RelationshipTypes;
import org.neo4j.extensions.java.domain.FriendResult;
import org.neo4j.extensions.java.domain.User;
import org.neo4j.extensions.java.indexes.UidsIndex;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The User controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Path("/user")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Context
    private GraphDatabaseService db;

    /**
     * @return Status 201 on success.
     */
    @POST
    @Path("/create")
    @Produces({
            MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
    })
    public Response create(@QueryParam("indexingOn") @DefaultValue("true") Boolean indexingOn) {
        LOGGER.info(String.format("POST /user/create?indexingOn=%s", indexingOn));
        long startTimeTx = System.currentTimeMillis();

        User friend1 = new User();
        User friend2 = new User();
        FriendResult friends = new FriendResult();
        List<User> friendsList = new ArrayList<User>();
        friendsList.add(friend1);
        friendsList.add(friend2);
        friends.setFriends(friendsList);

        try (Transaction tx = db.beginTx()) {
            // create friend 1 node
            Node friend1Node = db.createNode(LabelTypes.User);
            Long friend1Id = friend1Node.getId();
            friend1.setId(friend1Id);

            // create friend 2 node
            Node friend2Node = db.createNode(LabelTypes.User);
            Long friend2Id = friend2Node.getId();
            friend2.setId(friend2Id);

            // friend1 likes friend2
            friend1Node.createRelationshipTo(friend2Node, RelationshipTypes.FRIEND_OF);
            // friend2 likes friend1
            friend2Node.createRelationshipTo(friend1Node, RelationshipTypes.FRIEND_OF);

            // establish created time
            Long createdTime = System.currentTimeMillis();
            friend1Node.setProperty("createdTime", createdTime);
            friend1.setCreatedTime(createdTime);
            friend2Node.setProperty("createdTime", createdTime);
            friend2.setCreatedTime(createdTime);

            // establish friend 1 UID
            String friend1Uid = UUID.randomUUID().toString();
            // update UID properties
            friend1Node.setProperty("uid", friend1Uid);
            friend1.setUid(friend1Uid);

            // establish friend 2 UID
            String friend2Uid = UUID.randomUUID().toString();
            // update UID properties
            friend2Node.setProperty("uid", friend2Uid);
            friend2.setUid(friend2Uid);

            // update indexes
            if (indexingOn) {
                Index<Node> uidsIndex = db.index().forNodes(UidsIndex.uid.getIndexName(), UidsIndex.uid.getIndexType());
                uidsIndex.putIfAbsent(friend1Node, UidsIndex.uid.name(), friend1Uid);
                uidsIndex.putIfAbsent(friend2Node, UidsIndex.uid.name(), friend2Uid);
            }

            // capture times
            long successTimeTx = System.currentTimeMillis();
            long processTimeTx = successTimeTx - startTimeTx;

            tx.success();

            // log details
            LOGGER.log(Level.INFO,
                    String.format("UserController: TX: userId=%s, userUid=%s, processTime=%dms", friend1Id, friend1Uid, processTimeTx));

            return Response.status(Status.CREATED).entity(friends).build();
        } catch (Exception e) {
            // log details
            LOGGER.log(Level.SEVERE, e.getMessage());
            return Response.serverError().build();
        }
    }

}
