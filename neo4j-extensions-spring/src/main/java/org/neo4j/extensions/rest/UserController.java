package org.neo4j.extensions.rest;

import java.util.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

import org.neo4j.extensions.common.NodeWrapper;
import org.neo4j.extensions.domain.User;
import org.neo4j.extensions.repository.UserRepository;

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
    @Path(".json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(@Context UserRepository userRepository) {
        User u = new User();
        u.setUsername("bradnussbaum");
        u.setName("Brad Nussbaum");
        u.setEmail("brad@atomrain.com");
        u.setPassword("atomrain");

        u = userRepository.save(u);

        return Response.ok().entity(u).build();
    }

}
