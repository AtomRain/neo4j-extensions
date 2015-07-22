package org.neo4j.extensions.spring.rest;

import java.util.logging.Logger;
import javax.ws.rs.Path;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.springframework.stereotype.Controller;

import org.neo4j.extensions.common.client.StatusClient;
import org.neo4j.graphdb.GraphDatabaseService;
import org.neo4j.graphdb.Transaction;

/**
 * The status controller.
 *
 * @author bradnussbaum
 * @version 0.1.0
 * @since 0.1.0
 */
@Controller
@Path("/status")
public class StatusController implements StatusClient
{

    private static final Logger LOGGER = Logger.getLogger( StatusController.class.getName() );

    @Context
    private GraphDatabaseService db;

    public Response status()
    {
        try ( Transaction txn = db.beginTx() )
        {
            txn.success();
            LOGGER.info( "STATUS: success" );
        }
        catch ( Exception e )
        {
            LOGGER.info( "STATUS: failure" );
            throw new WebApplicationException( e );
        }
        return Response.ok().entity( "ok" ).build();
    }

}
