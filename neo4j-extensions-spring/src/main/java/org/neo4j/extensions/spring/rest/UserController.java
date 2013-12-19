package org.neo4j.extensions.spring.rest;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.neo4j.extensions.spring.domain.User;
import org.neo4j.graphdb.GraphDatabaseService;
import org.springframework.data.neo4j.support.Neo4jTemplate;

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

    @Context
    private GraphDatabaseService db;

    @Context
    private Neo4jTemplate template;

    /**
     * @return Status 200 on success.
     */
    @GET
    @Path("/json")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create() {
        User u = new User();
        u.setUsername("bradnussbaum");
        u.setName("Brad Nussbaum");
        u.setEmail("brad@atomrain.com");
        u.setPassword("atomrain");

        u = template.save(u);

        return Response.ok().entity(u).build();
    }

}
