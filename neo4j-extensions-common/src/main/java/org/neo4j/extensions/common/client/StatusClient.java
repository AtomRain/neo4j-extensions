package org.neo4j.extensions.common.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * The status client.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 2014.05.25
 */
@Path("/status")
public interface StatusClient
{

    /**
     * @return Status 200 on success.
     */
    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response status();

}
