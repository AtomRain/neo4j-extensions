package org.neo4j.extensions.java.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Transaction;

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
    public Response create() {
        LOGGER.info("Create");
        Transaction tx = null;
        try {
            tx = db.beginTx();

            Node user = db.createNode();
            user.setProperty("username", "bradnussbaum");
            user.setProperty("name", "Brad Nussbaum");
            user.setProperty("email", "brad@atomrain.com");
            user.setProperty("password", "atomrain");

            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            if (tx != null) {
                tx.finish();
            }
        }

        return Response.ok().build();
    }

}
