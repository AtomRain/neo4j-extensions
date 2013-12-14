package org.neo4j.extensions.java.rest;

import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.neo4j.extensions.java.domain.User;
import org.neo4j.extensions.java.indexes.UidsIndex;
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

        Index<Node> uidsIndex = db.index().forNodes(UidsIndex.uid.getIndexName(), UidsIndex.uid.getIndexType());

        Transaction tx = null;
        User user = new User();
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

            // capture times
            long successTimeTx = System.currentTimeMillis();
            long processTimeTx = successTimeTx - startTimeTx;

            tx.success();

            // log details
            LOGGER.log(Level.INFO,
                    String.format("UserController: TX: userId=%s, userUid=%s, processTime=%dms", userId, userUid, processTimeTx));

            return Response.status(Status.CREATED).entity(user).build();
        } catch (Exception e) {
            user = null;
            tx.failure();
            // log details
            LOGGER.log(Level.SEVERE, e.getMessage());
        } finally {
            if (tx != null) {
                tx.finish();
            }
        }
        return Response.serverError().build();
    }

}
