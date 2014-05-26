package org.neo4j.extensions.common.client;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The user client.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 2014.05.25
 */
@Path("/user")
public interface UserClient {

    /**
     * @return Status 201 on success.
     */
    @POST
    @Path("/create")
    @Produces({
            MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
    })
    public Response create(@QueryParam("indexingOn") @DefaultValue("true") Boolean indexingOn,
                           @QueryParam("count") @DefaultValue("3") Integer count);


}
