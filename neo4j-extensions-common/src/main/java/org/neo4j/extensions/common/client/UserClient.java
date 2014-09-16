package org.neo4j.extensions.common.client;

import org.codehaus.jackson.map.annotate.JsonView;

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
    @JsonView(EntityView.class)
    public Response create(@QueryParam("indexingOn") @DefaultValue("true") Boolean indexingOn,
                           @QueryParam("count") @DefaultValue("3") Integer count);

    /**
     * @return Status 200 on success.
     */
    @GET
    @Path("/pages")
    @Produces({
            MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML
    })
    @JsonView(EntityView.class)
    public Response findUsers(@QueryParam("page") @DefaultValue("0") Integer page,
                              @QueryParam("page.size") @DefaultValue("10") Integer pageSize,
                              @QueryParam("pages") @DefaultValue("1") Integer pages);

}
