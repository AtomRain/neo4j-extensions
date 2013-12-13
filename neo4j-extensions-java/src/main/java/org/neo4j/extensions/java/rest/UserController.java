package org.neo4j.extensions.java.rest;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.extensions.java.domain.User;
import org.neo4j.extensions.java.indexes.UidsIndex;
import org.neo4j.extensions.java.indexes.UserIndex;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.Index;

/**
 * The User controller.
 * 
 * 
 * @author bradnussbaum
 * @version 0.1.0
 * 
 * @since 0.1.0
 * 
 */
@Path("/user")
public class UserController {

    private static final Logger LOGGER = Logger.getLogger(UserController.class.getName());

    @Context
    private GraphDatabaseService db;

    /**
     * @return Status 200 on success.
     */
    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@QueryParam("username") String username, @QueryParam("indexingOn") Boolean indexingOn) {
        LOGGER.info(String.format("/user/create/json?username=%s", username));
        long startTimeTx = System.currentTimeMillis();

        Index<Node> uidsIndex = db.index().forNodes(UidsIndex.uid.getIndexName(), UidsIndex.uid.getIndexType());

        Transaction tx = null;
        User user = new User();
        user.setUsername(username);
        try {
            tx = db.beginTx();

            Node userNode = db.createNode();
            Long userId = userNode.getId();
            user.setId(userId);

            // establish created time
            Long createdTime = System.currentTimeMillis();
            userNode.setProperty("createdTime", createdTime);
            user.setCreatedTime(createdTime);

            // establish UID
            String userUid = UUID.randomUUID().toString();
            // update UID properties
            userNode.setProperty("uid", userUid);
            user.setUid(userUid);
            // update UID index
            if (indexingOn) {
                uidsIndex.putIfAbsent(userNode, UidsIndex.uid.name(), userUid);
            }

            // establish username
            userNode.setProperty("username", username);
            user.setUsername(username);
            if (indexingOn) {
                uidsIndex.putIfAbsent(userNode, UserIndex.username.name(), username);
            }

            // capture times
            long successTimeTx = System.currentTimeMillis();
            long processTimeTx = successTimeTx - startTimeTx;

            // log details
            LOGGER.log(Level.FINE,
                    String.format("UserController: TX: userId=%s, userUid=%s, processTime=%dms", userId, userUid, processTimeTx));

            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            if (tx != null) {
                tx.finish();
            }
        }

        return Response.ok().entity(user).build();
    }

}
